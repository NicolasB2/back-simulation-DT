package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventGenerator;
import com.simulationFrameworkDT.simulation.state.Project;

public class SimulationThread extends Thread {

	private EventGenerator eventGenerator;
	
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
//		eventGenerator.generateTest(project);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {

		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();
		
		//Queue<SimulationEvent> queueBus = new LinkedList<SimulationEvent>();

		while (initialDate.getTime() < lastDate.getTime()) { // while between initial and final time

			// ==========================================
			// simulation of first station
			// ==========================================

			SimulationEvent simulationEvent = (SimulationEvent) eventGenerator.generateFirstStation(project);
			
			project.setInitialDate(simulationEvent.getArriveDate());
			project.setNextDate(simulationEvent.getLeaveDate());
			
			System.out.println("O: "+simulationEvent.getArriveDate().toGMTString()+" Buses "+simulationEvent.getBusId());
			System.out.println("X: "+simulationEvent.getLeaveDate().toGMTString()+" Buses "+simulationEvent.getBusId());
			System.out.println(" ");
			
			simulationEvent = (SimulationEvent) eventGenerator.generateNextStation(simulationEvent);
			
			System.out.println("	O: "+simulationEvent.getArriveDate().toGMTString()+" Buses "+simulationEvent.getBusId());
			System.out.println("	X: "+simulationEvent.getLeaveDate().toGMTString()+" Buses "+simulationEvent.getBusId());
			System.out.println(" ");
			
			initialDate = simulationEvent.getArriveDate();
		}

	}
}
