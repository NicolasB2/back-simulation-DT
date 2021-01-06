package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.IDistribution;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class SimulationThread extends Thread {

	private int headwayDesigned;
	private SITMStop[] stations;
	private Project project;
	private EventGenerator eventGenerator;

	private ArrayList<Long> ids = new ArrayList<>();
	private ArrayList<SimulationEvent> events = new ArrayList<>();

	private HashMap<Long, Queue<Date>> passengersTime = new HashMap<Long, Queue<Date>>();
	private HashMap<Long, ArrayList<Double>> HobspList = new HashMap<Long, ArrayList<Double>>();// passengers
	private HashMap<Long, ArrayList<Double>> HobssList = new HashMap<Long, ArrayList<Double>>();// buses-stop

	public static void main(String[] args) throws Exception {

		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(initialDate);
		project.setFinalDate(nextDate);

		SITMStop[] stops = new SITMStop[2];
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(9.459459459);
		
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.WeibullDistribution(1.55075, 601.44131);
		
		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.WeibullDistribution(1.55075, 601.44131);
		
		SITMStop stop1 = new SITMStop(500250, passenger, ai, si);
		SITMStop stop2 = new SITMStop(500250, passenger, ai, si);
		
		stops[0]=stop1;
		stops[1]=stop2;
		
		SimulationThread st = new SimulationThread(project, stops ,360);
		st.start();
	}

	public SimulationThread(Project project, SITMStop[] stations, int headwayDesigned) {
		this.project = project;
		this.stations = stations;
		this.headwayDesigned = headwayDesigned;
		this.eventGenerator = new EventGenerator();
		initializeStructures(this.stations);
	}

	public void initializeStructures(SITMStop[] stations) {
		for (int i = 0; i < stations.length; i++) {
			HobspList.put(stations[i].getStopId(), new ArrayList<>());
			HobssList.put(stations[i].getStopId(), new ArrayList<>());
		}
	}

	@Override
	public void run() {

		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();

		

		LinkedList<SimulationEvent> stationQueue = new LinkedList<SimulationEvent>();
		LinkedList<SimulationEvent> middleQueue = new LinkedList<SimulationEvent>();

		for (int i = 0; i < stations.length; i++) { // iterate between stations

			Queue<Date> pt = eventGenerator.generateUsers(initialDate, lastDate, stations[i].getPassengersDistribution());
			passengersTime.put(stations[i].getStopId(),  pt);
			
			if (i == 0) { // arrive the first station
				stationQueue = arriveFirstStation(initialDate, lastDate, stations[i].getStopId());

			} else { // arrive the next stations 
				stationQueue = arriveNextStation(middleQueue, stations[i].getInterArrivalDistribution(),stations[i].getStopId());
			}

			// leave the station
			hobsp(stations[i].getStopId());
			hobss(stationQueue, stations[i].getStopId());
			middleQueue = leaveStation(stationQueue,stations[i].getServiceDistribution(), stations[i].getStopId());
		}

		allEvents(); // print all events order by time
		calculatingExcessWaitingTimeatBusStops(); // calculating Excess Waiting Time at Bus Stops
	}

	@SuppressWarnings("deprecation")
	public LinkedList<SimulationEvent> arriveFirstStation(Date initialDate, Date lastDate, long stopId) {

		LinkedList<SimulationEvent> station = new LinkedList<SimulationEvent>();

		while (initialDate.getTime() < lastDate.getTime()) { // full the queue with the buses that arrive between the simulation time
			SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(initialDate, this.headwayDesigned, generateId());
			System.out.println("O: " + arrive.getDate().toGMTString() + " BuseId " + arrive.getBusId());
			initialDate = arrive.getDate();
			station.offer(arrive);
			events.add(arrive);
		}

		System.out.println(" ");
		return station;
	}

	@SuppressWarnings("deprecation")
	public LinkedList<SimulationEvent> arriveNextStation(Queue<SimulationEvent> middleQueue,IDistribution pd, long stopId) {

		Date lastArrive = null; // initialize auxiliary variable which represent the last time that a bus arrive the station
		LinkedList<SimulationEvent> station = new LinkedList<SimulationEvent>();

		while (!middleQueue.isEmpty()) { // generate the arrive time in next stations, clear the middle queue with the buses in it

			SimulationEvent leave = middleQueue.poll();
			Date currently = leave.getDate();

			if (lastArrive != null && currently.getTime() < lastArrive.getTime()) {// use the lasted time to generate the arrive time
				currently = lastArrive;
			}

			SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(currently, pd, leave.getBusId());
			System.out.println("O: " + arrive.getDate().toGMTString() + " BusId " + arrive.getBusId());
			station.offer(arrive);
			lastArrive = arrive.getDate();
			events.add(arrive);
		}

		System.out.println(" ");
		return station;
	}

	@SuppressWarnings("deprecation")
	public LinkedList<SimulationEvent> leaveStation(Queue<SimulationEvent> stationQueue, IDistribution pd, long stopId) {

		Date lastLeave = null; // initialize auxiliary variable which represent the last time that a bus leave the station
		LinkedList<SimulationEvent> middle = new LinkedList<SimulationEvent>();

		while (!stationQueue.isEmpty()) { // generate the leave time, clear the station queue and enqueue in middle queue

			SimulationEvent arrive = stationQueue.poll();
			Date currently = arrive.getDate();

			if (lastLeave != null && currently.getTime() < lastLeave.getTime()) {// use the lasted time to generate the leave time
				currently = lastLeave;
			}

			SimulationEvent leave = (SimulationEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId());
			int numPassengersPerBus = passengersPerBus(leave, stopId);
			leave.setPassengers(numPassengersPerBus);

			System.out.println("X: " + leave.getDate().toGMTString() + " Bus " + arrive.getBusId() + " Passengers "+ numPassengersPerBus);
			lastLeave = leave.getDate();
			middle.offer(leave);
			events.add(leave);
		}

		System.out.println(" ");
		return middle;
	}

	// headway observed per bus
	public void hobss(LinkedList<SimulationEvent> stationQueue, long stopId) {

		SimulationEvent lastArrive = null;
		for (SimulationEvent item: stationQueue) {
			
			if(lastArrive==null) {
				lastArrive = item;
			}else {
				double headway = (item.getDate().getTime() - lastArrive.getDate().getTime()) / 1000;
				HobssList.get(stopId).add(headway);
				lastArrive = item;
			}
        }
	}

	// headway observed per passenger
	public void hobsp(long stopId) {
		
		Date userArrive = null;
		for (Date item: passengersTime.get(stopId)) {
			
			if(userArrive==null) {
				userArrive = item;
			}else {
				double headway = (item.getTime()-userArrive.getTime())/1000;
				HobspList.get(stopId).add(headway);
				userArrive = item;
			}
        }
	}

	public int passengersPerBus(SimulationEvent leave, long stopId) {

		Date passengerArrivetime = passengersTime.get(stopId).poll();
		int numPassengersPerBus = 0;

		while (!passengersTime.get(stopId).isEmpty() && passengerArrivetime.getTime() <= leave.getDate().getTime()) {
			numPassengersPerBus++;
			passengerArrivetime = passengersTime.get(stopId).poll();
		}

		return numPassengersPerBus;
	}
	private long generateId() {

		long id = (long) (Math.random() * (9999 - 1000 + 1) + 1000);
		while (ids.contains(id)) {
			id = (long) (Math.random() * (9999 - 1000 + 1) + 1000);
		}

		return id;
	}

	public void allEvents() {

		Collections.sort(events, new Comparator<SimulationEvent>() {
			public int compare(SimulationEvent o1, SimulationEvent o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		for (int i = 0; i < events.size(); i++) {
			System.out.println(events.get(i));
		}

		System.out.println("");
	}

	public void calculatingExcessWaitingTimeatBusStops() {

		for (int i = 0; i < stations.length; i++) {

			System.out.println("Stop Id " + stations[i].getStopId());
//			ArrayList<Double> Hobss = HobssList.get(stations[i].getStopId());
			ArrayList<Double> Hobsp = HobspList.get(stations[i].getStopId());
			ArrayList<Double> hrs = new ArrayList<Double>();

//			System.out.println("==================> Hobss");
//			for (int j = 0; j < Hobss.size(); j++) {
//				System.out.println(Hobss.get(j));
//			}
//			
//			System.out.println("==================> Hobsp");
//			for (int j = 0; j < Hobsp.size(); j++) {
//				System.out.println(Hobsp.get(j));
//			}
			
			System.out.println("==================> Hr");
			for (int j = 0; j < Hobsp.size(); j++) {
				double hr = ((double) Hobsp.get(j) / headwayDesigned) * 100;
				hrs.add(hr);
//				System.out.println(hr);
			}

			double meanHobss = mean(HobssList.get(stations[i].getStopId()));
			double meanHobsp = mean(HobspList.get(stations[i].getStopId()));
			double meanHr = mean(hrs);
			double varianceHr = variance(hrs);
			double EWTaBS = (varianceHr / (meanHobss*meanHr*100))*meanHobsp;
			
			System.out.println("MeanHobss : "+meanHobss);
			System.out.println("MeanHobsp : "+meanHobsp);
			System.out.println("Mean Hr : " + meanHr);
			System.out.println("variance Hr : " + varianceHr);
			System.out.println("EWTaBS : "+EWTaBS+"\n");
		}
	}

	public double variance(ArrayList<Double> v) {
		double m = mean(v);
		double sum = 0;
		for (int i = 0; i < v.size(); i++) {
			sum += Math.pow(v.get(i), 2.0);
		}

		return sum / v.size() - Math.pow(m, 2.0);
	}

	public double mean(ArrayList<Double> v) {
		double res = 0;
		for (int i = 0; i < v.size(); i++) {
			res += v.get(i);
		}

		return res / v.size();
	}
}