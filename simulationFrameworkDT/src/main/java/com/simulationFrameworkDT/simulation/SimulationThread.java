package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class SimulationThread extends Thread {

	private int numberOfStations;
	private EventGenerator eventGenerator;
	private ArrayList<Long> ids = new ArrayList<>();
	private ArrayList<SimulationEvent> events = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {

		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(initialDate);
		project.setFinalDate(nextDate);

		SimulationThread st = new SimulationThread(project,2);
		st.start();
		
	}

	private Project project;

	public SimulationThread(Project project,int numberOfStations) {
		this.project = project;
		this.numberOfStations = numberOfStations;
		this.eventGenerator = new EventGenerator();
	}

	@SuppressWarnings({ "deprecation"})
	@Override
	public void run() {
		
		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();
		
		ArrayList<ArrayList<Long>> HobspList = new  ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Long>> HobssList = new  ArrayList<ArrayList<Long>>();
		
		ArrayList<Queue<Date>> passengersTime = new  ArrayList<Queue<Date>>();
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.WeibullDistribution(5,7);
		
		ProbabilisticDistribution pd = new ProbabilisticDistribution();
		pd.WeibullDistribution(1.55075, 601.44131);
		
		Queue<SimulationEvent> station = new LinkedList<SimulationEvent>();
		Queue<SimulationEvent> middle = new LinkedList<SimulationEvent>();
		
		for (int i = 0; i < numberOfStations; i++) {
			Queue<Date> pt = eventGenerator.generateUsers(initialDate, lastDate, passenger);
			passengersTime.add(pt);
			HobspList.add(new ArrayList<>());
			HobssList.add(new ArrayList<>());
		}
		
		for (int i = 0; i < numberOfStations; i++) { // iterate between stations

			Date lastLeave = null; //initialize auxiliary variable which represent the last time that a bus leave the station
			Date lastArrive = null; //initialize auxiliary variable which represent the last time that a bus arrive the station
			
			if(i==0) { // arrive the first station
				
				while (initialDate.getTime() < lastDate.getTime()) { // full the queue with the buses that arrive between the simulation time
					SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(initialDate, pd, generateId());
					System.out.println("O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
					initialDate = arrive.getDate();
					station.offer(arrive);
					events.add(arrive);
				}
				
			}else { 	
				while(!middle.isEmpty()) { // generate the arrive time in next stations, clear the middle queue with the buses in it
					
					SimulationEvent leave = middle.poll();
					Date currently = leave.getDate();
					
					// use the lasted time to generate the arrive time in this case the last arrive time at the station 
					// or the time that the bus leave the previews station
					if(lastArrive!=null && currently.getTime()<lastArrive.getTime()) { 
						currently = lastArrive;
					}
					
					SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(currently, pd, leave.getBusId());
					System.out.println("O: " + arrive.getDate().toGMTString()+" Bus " + arrive.getBusId());
					station.offer(arrive);
					lastArrive = arrive.getDate();
					events.add(arrive);
				}	
			}

			lastArrive=null;
			System.out.println(" ");
			
			while(!station.isEmpty()) { // generate the leave time, clear the station queue and enqueue in middle queue
				
				SimulationEvent arrive = station.poll();
				Date currently = arrive.getDate();
				
				//****************************************************************************************************
				if(lastArrive!=null){
					HobssList.get(i).add(arrive.getDate().getTime()-lastArrive.getTime());
				}
				lastArrive = arrive.getDate();
				//****************************************************************************************************
				
				if(lastLeave != null && currently.getTime()<lastLeave.getTime()) {
					currently = lastLeave;
				}
				
				// use the lasted time to generate the leave time in this case the last leave time at the station 
				// or the time that the bus arrive the station
				SimulationEvent leave = (SimulationEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId());
				
				
				//***************************************************************************************************
				Date passengerArrivetime = passengersTime.get(i).poll();
				int numPassengersPerBus = 0;
				
				while(!passengersTime.get(i).isEmpty() && passengerArrivetime.getTime()<=leave.getDate().getTime()) {
					numPassengersPerBus ++;
					long hobsp = (leave.getDate().getTime() - passengerArrivetime.getTime())/1000;
					passengerArrivetime = passengersTime.get(i).poll();
					HobspList.get(i).add(hobsp);
				}
				//****************************************************************************************************
				
				System.out.println("X: " + leave.getDate().toGMTString()+" Bus " + arrive.getBusId() + " Passengers "+numPassengersPerBus);
				lastLeave = leave.getDate();
				middle.offer(leave);
				events.add(leave);
			}
			
			System.out.println(" ");
		}
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
	}
	
	private long generateId() {
		
		long id = (long) (Math.random()*(9999-1000+1)+1000);
		
		while(ids.contains(id)) {
			id = (long) (Math.random()*(9999-1000+1)+1000);
		}
		
		return id;
	}
	
}