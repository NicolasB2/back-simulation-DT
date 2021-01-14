package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.analytics.SimulationAnalytics;
import com.simulationFrameworkDT.model.PassangerEvent;
import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.model.StopDistribution;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

import lombok.Getter;

@Getter
public class SimulationThread extends Thread {

	private int headwayDesigned;
	private int numberOfBuses;
	private StopDistribution[] stations;
	private Project project;
	private EventGenerator eventGenerator;

	private ArrayList<Long> ids = new ArrayList<>();
	private ArrayList<Event> events = new ArrayList<>();

	private HashMap<Long, Queue<PassangerEvent>> passengersTime = new HashMap<Long, Queue<PassangerEvent>>();
	private HashMap<Long, ArrayList<Double>> hobspList = new HashMap<Long, ArrayList<Double>>();// passengers
	private HashMap<Long, ArrayList<Double>> hobssList = new HashMap<Long, ArrayList<Double>>();// buses-stop
	
	public SimulationThread(Project project, StopDistribution[] stations, int headwayDesigned) {
		this.project = project;
		this.stations = stations;
		this.headwayDesigned = headwayDesigned;
		this.eventGenerator = new EventGenerator();
		initializeStructures(this.stations);
	}

	public void initializeStructures(StopDistribution[] stations) {
		for (int i = 0; i < stations.length; i++) {
			hobspList.put(stations[i].getStopId(), new ArrayList<>());
			hobssList.put(stations[i].getStopId(), new ArrayList<>());
		}
	}

	@Override
	public void run() {
		
		simulation(); // generate the simulation
		allEvents(); // all events order by time
		
		int usersStop1 = 0;
		int usersStop2 = 0;
		int busesStop1 = 0;
		int busesStop2 = 0;
		int busesRoad  = 0;
		
		for (int i = 0; i < events.size(); i++) {
			
			
			if (events.get(i) instanceof PassangerEvent) {
				
				PassangerEvent item = (PassangerEvent) events.get(i);
				
				if(item.getStopId()==stations[0].getStopId()) {
					usersStop1++;
				}
				
				if(item.getStopId()==stations[1].getStopId()) {
					usersStop2++;
				}	
			}

			if (events.get(i) instanceof SimulationEvent) {
				
				SimulationEvent item = (SimulationEvent) events.get(i);
				
				if(item.getStopId()==stations[0].getStopId()) {
					if(item.isArrive()) {
						busesStop1++;
					}else {
						busesStop1--;
						busesRoad++;
					}
					usersStop1-=item.getPassengers();
				}
				
				if(item.getStopId()==stations[1].getStopId()) {
					if(item.isArrive()) {
						busesStop2++;
						busesRoad--;
					}else {
						busesStop2--;
					}
					usersStop2-=item.getPassengers();
				}
				
				Timestamp dateTime= new Timestamp(events.get(i).getDate().getTime());
				System.out.println(dateTime);
				System.out.println("Passenger stop A "+usersStop1);
				System.out.println("Passenger stop B "+usersStop2);
				System.out.println("Buses stop A "+busesStop1);
				System.out.println("Buses - Road "+busesRoad);
				System.out.println("Buses stop B "+busesStop2);
				System.out.println();
				
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}
		
		evaluationMetrics(); // calculating Excess Waiting Time at Bus Stops
	}
	
	public void simulation() {

		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();

		LinkedList<SimulationEvent> stationQueue = new LinkedList<SimulationEvent>();
		LinkedList<SimulationEvent> middleQueue = new LinkedList<SimulationEvent>();

		for (int i = 0; i < stations.length; i++) { // iterate between stations

			Queue<PassangerEvent> pt = eventGenerator.generateUsers(initialDate, lastDate, stations[i].getPassengersDistribution(),stations[i].getStopId());
			passengersTime.put(stations[i].getStopId(),  pt);

			if (i == 0) { // arrive the first station
				stationQueue = arriveFirstStation(initialDate, lastDate, stations[i].getStopId());

			} else { // arrive the next stations 
				stationQueue = arriveNextStation(middleQueue, stations[i].getInterArrivalDistribution(),stations[i].getStopId());
			}

			// leave the station
			hobss(stationQueue, stations[i].getStopId());
			middleQueue = leaveStation(stationQueue,stations[i].getServiceDistribution(), stations[i].getStopId());
		}
	}

	public LinkedList<SimulationEvent> arriveFirstStation(Date initialDate, Date lastDate, long stopId) {

		long timeOfTravel = 2 * 60 * 60 * 1000; //time a bus takes to make the journey
		LinkedList<SimulationEvent> station = new LinkedList<SimulationEvent>();
		ArrayList<Long> idsAux = new ArrayList<Long>(); 
		long currentDate = initialDate.getTime();
		int aux = 0;
		
		while (currentDate < lastDate.getTime()) { // full the queue with the buses that arrive between the simulation time
			
			long id = 0;
			
			//Select id for the bus
			if(currentDate<initialDate.getTime()+timeOfTravel) {//if the current time is less than time of travel, generate a new id
				id = generateId();
				idsAux.add(id);
			}else {//if the current time is after of time of travel, use the id of the previews buses 
				if(idsAux.size()>aux) {
					id = idsAux.get(aux);
					aux++;
				}else {
					id = idsAux.get(0);
					aux=1;
				}
			}
			
			SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(new Date(currentDate), this.headwayDesigned, id, stopId);
			System.out.println("Arrive: " + arrive.getDateFormat() + " BuseId " + arrive.getBusId());
			currentDate = arrive.getDate().getTime();
			station.offer(arrive);
			events.add(arrive);
		}

		numberOfBuses=idsAux.size();
		System.out.println(" ");
		return station;
	}

	public LinkedList<SimulationEvent> arriveNextStation(Queue<SimulationEvent> middleQueue,IDistribution pd, long stopId) {

		Date lastArrive = null; // initialize auxiliary variable which represent the last time that a bus arrive the station
		LinkedList<SimulationEvent> station = new LinkedList<SimulationEvent>();

		while (!middleQueue.isEmpty()) { // generate the arrive time in next stations, clear the middle queue with the buses in it

			SimulationEvent leave = middleQueue.poll();
			Date currently = leave.getDate();

			if (lastArrive != null && currently.getTime() < lastArrive.getTime()) {// use the lasted time to generate the arrive time
				currently = lastArrive;
			}

			SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(currently, pd, leave.getBusId(),stopId);
			System.out.println("Arrive: " + arrive.getDateFormat() + " BusId " + arrive.getBusId());
			station.offer(arrive);
			lastArrive = arrive.getDate();
			events.add(arrive);
		}

		System.out.println(" ");
		return station;
	}

	public LinkedList<SimulationEvent> leaveStation(Queue<SimulationEvent> stationQueue, IDistribution pd, long stopId) {

		Date lastLeave = null; // initialize auxiliary variable which represent the last time that a bus leave the station
		LinkedList<SimulationEvent> middle = new LinkedList<SimulationEvent>();

		while (!stationQueue.isEmpty()) { // generate the leave time, clear the station queue and enqueue in middle queue

			SimulationEvent arrive = stationQueue.poll();
			Date currently = arrive.getDate();

			if (lastLeave != null && currently.getTime() < lastLeave.getTime()) {// use the lasted time to generate the leave time
				currently = lastLeave;
			}

			SimulationEvent simulationLeaveEvent = (SimulationEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId(),stopId);
			int numPassengersPerBus = passengersPerBus(simulationLeaveEvent, stopId);
			simulationLeaveEvent.setPassengers(numPassengersPerBus);

			System.out.println("Leave: " + simulationLeaveEvent.getDateFormat() + " Bus " + arrive.getBusId() + " Passengers "+ numPassengersPerBus);
			lastLeave = simulationLeaveEvent.getDate();
			middle.offer(simulationLeaveEvent);
			events.add(simulationLeaveEvent);
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
				hobssList.get(stopId).add(headway);
				lastArrive = item;
			}
        }
	}

	//number of passengers and headway observed per passengers
	public int passengersPerBus(SimulationEvent leave, long stopId) {

		PassangerEvent passangerEvent = passengersTime.get(stopId).poll();
		Date passengerArrivetime = passangerEvent!=null?passangerEvent.getDate():null;
		int numPassengersPerBus = 0;

		// the number of passengers exceed the bus capacity
		while (!passengersTime.get(stopId).isEmpty() && passengerArrivetime.getTime() < leave.getDate().getTime() && numPassengersPerBus<160) {
			
			numPassengersPerBus++;
			passangerEvent = passengersTime.get(stopId).poll();
			
			if(passangerEvent!=null) {
				double headway = (leave.getDate().getTime()-passengerArrivetime.getTime())/1000; //headway observed per passenger
				hobspList.get(stopId).add(headway);// added headway
				events.add(passangerEvent);//added user event
				passengerArrivetime = passangerEvent.getDate(); //update actual time
			}
			
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

		Collections.sort(events, new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}

	public void evaluationMetrics() {
		SimulationAnalytics analytics = new SimulationAnalytics(numberOfBuses,headwayDesigned, hobspList, hobssList);
		analytics.headwayCoefficientOfVariation();
		analytics.excessWaitingTimeatBusStops();
		analytics.fitness();
	}


}