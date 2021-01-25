package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.analytics.SimulationAnalytics;
import com.simulationFrameworkDT.model.Operation;
import com.simulationFrameworkDT.model.events.BusEvent;
import com.simulationFrameworkDT.model.events.PassangerEvent;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimulationThread extends Thread {

	private int headwayDesigned;
	private int sleepTime;
	private long lineId;
	
	private Operation operation;
	private ArrayList<SITMStop> stations;
	private Project project;
	private EventGenerator eventGenerator;

	private ArrayList<Long> ids = new ArrayList<>();
	private ArrayList<Event> events = new ArrayList<>();

	private HashMap<Long, Queue<PassangerEvent>> passengersTime = new HashMap<Long, Queue<PassangerEvent>>();
	private HashMap<Long, ArrayList<Double>> hobspList = new HashMap<Long, ArrayList<Double>>();// passengers
	private HashMap<Long, ArrayList<Double>> hobssList = new HashMap<Long, ArrayList<Double>>();// buses-stop
	
	public SimulationThread(Project project, ArrayList<SITMStop> stations, long lineId, int headwayDesigned) {
		this.project = project;
		this.lineId = lineId;
		this.stations = stations;
		this.headwayDesigned = headwayDesigned;
		this.eventGenerator = new EventGenerator();
		this.operation = new Operation(headwayDesigned);
		initializeStructures(this.stations);
	}

	public void initializeStructures(ArrayList<SITMStop> stations) {
		for (int i = 0; i < stations.size(); i++) {
			hobspList.put(stations.get(i).getStopId(), new ArrayList<>());
			hobssList.put(stations.get(i).getStopId(), new ArrayList<>());
		}
		
		simulation(); // generate the simulation
	}

	@Override
	public void run() {
	
		allEvents(); // all events order by time
		
		int usersSalomia = 0;
		int usersFloraInd = 0;
		int busesSalomia = 0;
		int busesFloraInd = 0;
		int busesRoad  = 0;
		
		long minute = 1000*60;
		
		Date lastDate = events.get(0).getDate();
		Date currentDate = null;
		
		for (int i = 0; i < events.size(); i++) {
			
			currentDate = events.get(i).getDate();
			
			if (events.get(i) instanceof PassangerEvent) {
				
				PassangerEvent item = (PassangerEvent) events.get(i);
				if(item.getStopId()==stations.get(0).getStopId()) {usersSalomia++;}
				if(item.getStopId()==stations.get(1).getStopId()) {usersFloraInd++;}	
			}

			if (events.get(i) instanceof BusEvent) {
				
				BusEvent item = (BusEvent) events.get(i);
				
				if(item.getStopId()==stations.get(0).getStopId()) {
					if(item.isArrive()) {
						busesSalomia++;
						this.operation.update(events.get(i).getDate(), usersSalomia, busesSalomia, busesRoad, usersFloraInd, busesFloraInd);
						try {
							sleep(sleepTime);
							lastDate=currentDate;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						if(usersSalomia<=160) {
							item.setPassengers(usersSalomia);
							usersSalomia=0;
						}else {
							item.setPassengers(160);
							usersSalomia-=160;
						}
						busesSalomia--;
						busesRoad++;
					}
				}
				
				if(item.getStopId()==stations.get(1).getStopId()) {
					if(item.isArrive()) {
						busesFloraInd++;
						busesRoad--;
						this.operation.update(events.get(i).getDate(), usersSalomia, busesSalomia, busesRoad, usersFloraInd, busesFloraInd);
						try {
							sleep(sleepTime);
							lastDate=currentDate;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						if(usersFloraInd<=160) {
							item.setPassengers(usersFloraInd);
							usersFloraInd=0;
						}else {
							item.setPassengers(160);
							usersFloraInd-=160;
						}
						busesFloraInd--;
					}
				}
			}
			
			this.operation.update(events.get(i).getDate(), usersSalomia, busesSalomia, busesRoad, usersFloraInd, busesFloraInd);
			
			if(currentDate.getTime()-lastDate.getTime()>minute) {
				try {
					sleep(sleepTime);
					lastDate=currentDate;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}	
			
			System.out.println(events.get(i));
		}
		this.operation.setFinished(false);
		evaluationMetrics(this.operation); // calculating Excess Waiting Time at Bus Stops
	}
	
	public void simulation() {

		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();

		LinkedList<BusEvent> stationQueue = new LinkedList<BusEvent>();
		LinkedList<BusEvent> middleQueue = new LinkedList<BusEvent>();

		for (int i = 0; i < stations.size(); i++) { // iterate between stations

			Queue<PassangerEvent> pt = eventGenerator.generateUsers(initialDate, lastDate, stations.get(i).getLines().get(this.lineId).getPassengersDistribution(),stations.get(i).getStopId());
			addPassengersToEvents(pt);
			passengersTime.put(stations.get(i).getStopId(),  pt);

			if (i == 0) { // arrive the first station
				stationQueue = arriveFirstStation(initialDate, lastDate, stations.get(i).getStopId());

			} else { // arrive the next stations 
				stationQueue = arriveNextStation(lastDate,middleQueue, stations.get(i).getLines().get(this.lineId).getInterArrivalDistribution(),stations.get(i).getStopId());
			}

			// leave the station
			hobss(stationQueue, stations.get(i).getStopId());
			middleQueue = leaveStation(lastDate, stationQueue,stations.get(i).getLines().get(this.lineId).getServiceDistribution(), stations.get(i).getStopId());
		}
	}

	public LinkedList<BusEvent> arriveFirstStation(Date initialDate, Date lastDate, long stopId) {

		long timeOfTravel = 2 * 60 * 60 * 1000; //time a bus takes to make the journey
		LinkedList<BusEvent> station = new LinkedList<BusEvent>();
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
			
			BusEvent arrive = (BusEvent) eventGenerator.generateAi(new Date(currentDate), this.headwayDesigned, id, stopId);
//			System.out.println("Arrive: " + arrive.getDateFormat() + " BuseId " + arrive.getBusId());
			currentDate = arrive.getDate().getTime();
			station.offer(arrive);
			if(arrive!=null)
				events.add(arrive);
		}
		return station;
	}

	public LinkedList<BusEvent> arriveNextStation(Date lastDate, Queue<BusEvent> middleQueue,IDistribution pd, long stopId) {

		Date lastArrive = middleQueue.peek().getDate(); // initialize auxiliary variable which represent the last time that a bus arrive the station
		LinkedList<BusEvent> station = new LinkedList<BusEvent>();

		while (!middleQueue.isEmpty()&&lastArrive.getTime()<=lastDate.getTime()) { // generate the arrive time in next stations, clear the middle queue with the buses in it

			BusEvent leave = middleQueue.poll();
			Date currently = leave.getDate();

			if (lastArrive != null && currently.getTime() < lastArrive.getTime()) {// use the lasted time to generate the arrive time
				currently = lastArrive;
			}

			BusEvent arrive = (BusEvent) eventGenerator.generateAi(currently, pd, leave.getBusId(),stopId);
//			System.out.println("Arrive: " + arrive.getDateFormat() + " BusId " + arrive.getBusId());
			station.offer(arrive);
			lastArrive = arrive.getDate();
			if(arrive!=null)
				events.add(arrive);
		}

		return station;
	}

	public LinkedList<BusEvent> leaveStation(Date lastDate, Queue<BusEvent> stationQueue, IDistribution pd, long stopId) {

		Date lastLeave = stationQueue.peek().getDate(); // initialize auxiliary variable which represent the last time that a bus leave the station
		LinkedList<BusEvent> middle = new LinkedList<BusEvent>();

		while (!stationQueue.isEmpty() && lastLeave.getTime() <= lastDate.getTime()) { // generate the leave time, clear the station queue and enqueue in middle queue

			BusEvent arrive = stationQueue.poll();
			Date currently = arrive.getDate();

			if (lastLeave != null && currently.getTime() < lastLeave.getTime()) {// use the lasted time to generate the leave time
				currently = lastLeave;
			}

			BusEvent simulationLeaveEvent = (BusEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId(),stopId);
			int numPassengersPerBus = passengersPerBus(simulationLeaveEvent, stopId);
			simulationLeaveEvent.setPassengers(numPassengersPerBus);

//			System.out.println("Leave: " + simulationLeaveEvent.getDateFormat() + " Bus " + arrive.getBusId() + " Passengers "+ numPassengersPerBus);
			lastLeave = simulationLeaveEvent.getDate();
			middle.offer(simulationLeaveEvent);
			if(simulationLeaveEvent!=null)
				events.add(simulationLeaveEvent);
		}

		return middle;
	}

	// headway observed per bus
	public void hobss(LinkedList<BusEvent> stationQueue, long stopId) {

		BusEvent lastArrive = null;
		for (BusEvent item: stationQueue) {
			
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
	public int passengersPerBus(BusEvent leave, long stopId) {

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
				passengerArrivetime = passangerEvent.getDate(); //update actual time
			}
			
		}

		return numPassengersPerBus;
	}
	
	private void addPassengersToEvents(Queue<PassangerEvent>passengers) {
		for (PassangerEvent item: passengers) {
			events.add(item);//added user event
        }
	}
	private long generateId() {

		long id = (long) (Math.random() * (9999 - 1000 + 1) + 1000);
		while (ids.contains(id)) {
			id = (long) (Math.random() * (9999 - 1000 + 1) + 1000);
		}

		return id;
	}

	public void allEvents() {
		if(!events.isEmpty()) {
			Collections.sort(events, new Comparator<Event>() {
				public int compare(Event o1, Event o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});			
		}
	}

	public void evaluationMetrics(Operation operation) {
		SimulationAnalytics analytics = new SimulationAnalytics(headwayDesigned, hobspList, hobssList);
		operation.setHeadwayCoefficientOfVariation(analytics.headwayCoefficientOfVariation());
		operation.setExcessWaitingTime(analytics.excessWaitingTime());
		operation.setBusesImpact(analytics.fitnessOperation());
		operation.setPassengerSatisfaction(analytics.fitnessUsers());
		
		HashMap<Long, Double> meansHOBus = analytics.meansHOBus();
		HashMap<Long, Double> meansHOPassengers = analytics.meansHOPassengers();
		
		long flora = 500300;
		long salomia = 500250;
		
		operation.setMeanHOBusFloraInd(meansHOBus.get(flora));
		operation.setMeanHOBusSalomia(meansHOBus.get(salomia));
		
		operation.setMeanHOUsersFloraInd(meansHOPassengers.get(flora));
		operation.setMeanHOUsersSalomia(meansHOPassengers.get(salomia));
		
	}


}