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
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class SimulationThread extends Thread {

	private EventGenerator eventGenerator;
	private ArrayList<SimulationEvent> events = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {

		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(initialDate);
		project.setFinalDate(nextDate);

		SimulationThread st = new SimulationThread(project);
		st.start();
		
//		ProbabilisticDistribution pd = new ProbabilisticDistribution();
//		pd.WeibullDistribution(1.55075, 601.44131);
//		
//		for (int i = 0; i < 2980 ; i++) {
//			System.out.println((int)pd.getSample());
//		}
	}

	private Project project;

	public SimulationThread(Project project) {
		this.project = project;
		this.eventGenerator = new EventGenerator();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();
		
		ProbabilisticDistribution pd = new ProbabilisticDistribution();
		pd.WeibullDistribution(1.55075, 601.44131);
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.WeibullDistribution(5,7);
		
		Queue<Date> passengersTime = new LinkedList<Date>();
		
		// ==========================================
		// simulation of passengers
		// ==========================================
		
		int simulationTime =  (int) ((lastDate.getTime()-initialDate.getTime())/1000)/60;
		//System.out.println("simulationTime "+simulationTime);
		
		for (int i = 1; i <= simulationTime+60; i++) {
			
			int numPassenger = (int) Math.round(passenger.getSample());
			//System.out.println("numPassenger "+numPassenger);
			
			for (int j = 0; j < numPassenger; j++) {
				Date passengerArrivetime = new Date(initialDate.getTime() + (i * 60 * 1000));
				passengersTime.offer(passengerArrivetime);
				//System.out.println(time.toGMTString());
			}
			
		}
		
		while (initialDate.getTime() < lastDate.getTime()) {

			// ==========================================
			// simulation of first station
			// ==========================================

			Event simulationEvents[] = (Event[]) eventGenerator.generateFirstStation(project,pd,pd);
			SimulationEvent arrive = (SimulationEvent) simulationEvents[0];
			SimulationEvent leave = (SimulationEvent) simulationEvents[1];
			events.add(arrive);
			events.add(leave);
			
			
//			Date passengerArrivetime = passengersTime.poll();
//			int numPassengersPerBus = 0;
//			
//			while(!passengersTime.isEmpty() && passengerArrivetime.getTime()<=leave.getDate().getTime()) {
//				numPassengersPerBus ++;
//				long Hobsp = (leave.getDate().getTime() - passengerArrivetime.getTime())/1000;
//				Hobsps.add(Hobsp);
//				passengerArrivetime = passengersTime.poll();
//			}
//			
//			System.out.println("Passengers per Bus "+numPassengersPerBus);
			
			
			System.out.println("O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
			System.out.println("X: " + leave.getDate().toGMTString()+" Buses " + leave.getBusId());
			System.out.println(" ");
			
			project.setInitialDate(arrive.getDate());
			project.setNextDate(leave.getDate());
			initialDate = leave.getDate();
			

			// ==========================================
			// simulation of next stations
			// ==========================================
			
			String tab = "";
			
			for (int i = 0; i < 2; i++) {
				
				tab += "	";
				
				simulationEvents = (Event[]) eventGenerator.generateNextStation(leave,pd,pd);
				arrive = (SimulationEvent) simulationEvents[0];
				leave = (SimulationEvent) simulationEvents[1];
				events.add(arrive);
				events.add(leave);
				
				System.out.println(tab + "O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
				System.out.println(tab + "X: " + leave.getDate().toGMTString()+" Buses " + leave.getBusId());
				System.out.println(" ");
			}
		}
//		allEvents();
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
}
