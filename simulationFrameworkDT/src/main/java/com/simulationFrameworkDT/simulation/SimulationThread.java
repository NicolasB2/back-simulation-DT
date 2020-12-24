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

		SimulationThread st = new SimulationThread(project);
		st.start();
		
	}

	private Project project;

	public SimulationThread(Project project) {
		this.project = project;
		this.eventGenerator = new EventGenerator();
	}
	
	public void test() {
//		Date passengerArrivetime = passengersTime.poll();
//		int numPassengersPerBus = 0;
//		
//		while(!passengersTime.isEmpty() && passengerArrivetime.getTime()<=leave.getDate().getTime()) {
//			numPassengersPerBus ++;
//			long Hobsp = (leave.getDate().getTime() - passengerArrivetime.getTime())/1000;
//			Hobsps.add(Hobsp);
//			passengerArrivetime = passengersTime.poll();
//		}
//		
//		System.out.println("Passengers per Bus "+numPassengersPerBus);
	}

	@SuppressWarnings({ "deprecation"})
	@Override
	public void run() {
		
		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.WeibullDistribution(5,7);
		eventGenerator.generateUsers(initialDate, lastDate, passenger);
		
		ProbabilisticDistribution pd = new ProbabilisticDistribution();
		pd.WeibullDistribution(1.55075, 601.44131);
		
		Queue<SimulationEvent> station = new LinkedList<SimulationEvent>();
		Queue<SimulationEvent> middle = new LinkedList<SimulationEvent>();
		
		
		for (int i = 0; i < 2; i++) {
			
			Date lastLeave = null;
			Date lastArrive = null;
			
			if(i==0) {
				
				while (initialDate.getTime() < lastDate.getTime()) {
					SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(initialDate, pd, generateId());
					System.out.println("O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
					initialDate = arrive.getDate();
					station.offer(arrive);
					events.add(arrive);
				}
				
			}else {
				
				while(!middle.isEmpty()) {
					
					SimulationEvent leave = middle.poll();
					Date currently = leave.getDate();
					
					if(lastArrive!=null && currently.getTime()<lastArrive.getTime()) {
						currently = lastArrive;
					}
					
					SimulationEvent arrive = (SimulationEvent) eventGenerator.generateAi(currently, pd, leave.getBusId());
					System.out.println("O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
					station.offer(arrive);
					lastArrive = arrive.getDate();
					events.add(arrive);
				}
				
			}

			System.out.println(" ");
			
			while(!station.isEmpty()) {
				
				SimulationEvent arrive = station.poll();
				Date currently = arrive.getDate();
				
				if(lastLeave != null && currently.getTime()<lastLeave.getTime()) {
					currently = lastLeave;
				}
				
				SimulationEvent leave = (SimulationEvent) eventGenerator.generateSi(currently, pd, arrive.getBusId());
				System.out.println("X: " + leave.getDate().toGMTString()+" Buses " + arrive.getBusId());
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