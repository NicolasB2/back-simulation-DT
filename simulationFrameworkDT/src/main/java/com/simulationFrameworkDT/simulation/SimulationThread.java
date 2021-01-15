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
	private Operation operation;
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
		this.operation = new Operation(headwayDesigned);
		initializeStructures(this.stations);
	}

	public void initializeStructures(StopDistribution[] stations) {
		for (int i = 0; i < stations.length; i++) {
			hobspList.put(stations[i].getStopId(), new ArrayList<>());
			hobssList.put(stations[i].getStopId(), new ArrayList<>());
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
				if(item.getStopId()==stations[0].getStopId()) {usersSalomia++;}
				if(item.getStopId()==stations[1].getStopId()) {usersFloraInd++;}	
			}

			if (events.get(i) instanceof SimulationEvent) {
				
				SimulationEvent item = (SimulationEvent) events.get(i);
				
				if(item.getStopId()==stations[0].getStopId()) {
					if(item.isArrive()) {
						busesSalomia++;
					}else {
						usersSalomia-=160;
						if (usersSalomia<0) {
							usersSalomia=0;
						}
						busesSalomia--;
						busesRoad++;
					}
				}
				
				if(item.getStopId()==stations[1].getStopId()) {
					if(item.isArrive()) {
						busesFloraInd++;
						busesRoad--;
					}else {
						usersFloraInd-=160;
						if (usersFloraInd<0) {
							usersFloraInd=0;
						}
						busesFloraInd--;
					}
				}
			}
			
			this.operation.update(events.get(i).getDate(), usersSalomia, busesSalomia, busesRoad, usersFloraInd, busesFloraInd);
			
			if(currentDate.getTime()-lastDate.getTime()>minute) {
				try {
					sleep(0);
					lastDate=currentDate;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}	
		}
		evaluationMetrics(this.operation); // calculating Excess Waiting Time at Bus Stops
	}
	
	public void simulation() {

		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();

		LinkedList<SimulationEvent> stationQueue = new LinkedList<SimulationEvent>();
		LinkedList<SimulationEvent> middleQueue = new LinkedList<SimulationEvent>();

		for (int i = 0; i < stations.length; i++) { // iterate between stations

			Queue<PassangerEvent> pt = eventGenerator.generateUsers(initialDate, lastDate, stations[i].getPassengersDistribution(),stations[i].getStopId());
			addPassengersToEvents(pt);
			passengersTime.put(stations[i].getStopId(),  pt);

			if (i == 0) { // arrive the first station
				stationQueue = arriveFirstStation(initialDate, lastDate, stations[i].getStopId());

			} else { // arrive the next stations 
				stationQueue = arriveNextStation(lastDate,middleQueue, stations[i].getInterArrivalDistribution(),stations[i].getStopId());
			}

			// leave the station
			hobss(stationQueue, stations[i].getStopId());
			middleQueue = leaveStation(lastDate, stationQueue,stations[i].getServiceDistribution(), stations[i].getStopId());
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
//			System.out.println("Arrive: " + arrive.getDateFormat() + " BuseId " + arrive.getBusId());
			currentDate = arrive.getDate().getTime();
			station.offer(arrive);
			events.add(arrive);
		}
		return station;
	}

	public LinkedList<SimulationEvent> arriveNextStation(Date lastDate, Queue<SimulationEvent> middleQueue,IDistribution pd, long stopId) {

		Date lastArrive = middleQueue.peek().getDate(); // initialize auxiliary variable which represent the last time that a bus arrive the station
		LinkedList<SimulationEvent> station = new LinkedList<SimulationEvent>();

		while (!middleQueue.isEmpty()&&lastArrive.getTime()<=lastDate.getTime()) { // generate the arrive time in next stations, clear the middle queue with the buses in it

			SimulationEvent leave = middleQueue.poll();
			Date currently = leave.getDate();

			if (lastArrive != null && currently.getTime() < lastArrive.getTime()) {// use the lasted time to generate the arrive time
				currently = lastArrive;
			}

			SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(currently, pd, leave.getBusId(),stopId);
//			System.out.println("Arrive: " + arrive.getDateFormat() + " BusId " + arrive.getBusId());
			station.offer(arrive);
			lastArrive = arrive.getDate();
			events.add(arrive);
		}

		return station;
	}

	public LinkedList<SimulationEvent> leaveStation(Date lastDate, Queue<SimulationEvent> stationQueue, IDistribution pd, long stopId) {

		Date lastLeave = stationQueue.peek().getDate(); // initialize auxiliary variable which represent the last time that a bus leave the station
		LinkedList<SimulationEvent> middle = new LinkedList<SimulationEvent>();

		while (!stationQueue.isEmpty() && lastLeave.getTime() <= lastDate.getTime()) { // generate the leave time, clear the station queue and enqueue in middle queue

			SimulationEvent arrive = stationQueue.poll();
			Date currently = arrive.getDate();

			if (lastLeave != null && currently.getTime() < lastLeave.getTime()) {// use the lasted time to generate the leave time
				currently = lastLeave;
			}

			SimulationEvent simulationLeaveEvent = (SimulationEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId(),stopId);
			int numPassengersPerBus = passengersPerBus(simulationLeaveEvent, stopId);
			simulationLeaveEvent.setPassengers(numPassengersPerBus);

//			System.out.println("Leave: " + simulationLeaveEvent.getDateFormat() + " Bus " + arrive.getBusId() + " Passengers "+ numPassengersPerBus);
			lastLeave = simulationLeaveEvent.getDate();
			middle.offer(simulationLeaveEvent);
			events.add(simulationLeaveEvent);
		}

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
		events.add(passangerEvent);//added user event
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

		Collections.sort(events, new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}

	public void evaluationMetrics(Operation operation) {
		
		SimulationAnalytics analytics = new SimulationAnalytics(headwayDesigned, hobspList, hobssList);
		operation.setHeadwayCoefficientOfVariation(analytics.headwayCoefficientOfVariation());
		operation.setExcessWaitingTime(analytics.excessWaitingTime());
		operation.setBusesImpact(analytics.fitnessOperation());
		operation.setPassengerSatisfaction(analytics.fitnessUsers());
		
		HashMap<Long, Double> meansHOBus = analytics.meansHOBus();
		HashMap<Long, Double> meansHOPassengers = analytics.meansHOPassengers();
		
		long flora = 500250;
		long salomia = 500300;
		
		operation.setMeanHOBusFloraInd(meansHOBus.get(flora));
		operation.setMeanHOBusSalomia(meansHOBus.get(salomia));
		
		operation.setMeanHOUsersFloraInd(meansHOPassengers.get(flora));
		operation.setMeanHOUsersSalomia(meansHOPassengers.get(salomia));
		
		System.out.println(operation);
	}


}