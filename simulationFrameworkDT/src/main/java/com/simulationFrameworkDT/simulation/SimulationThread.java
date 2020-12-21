package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.Event;
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
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Date initialDate = project.getInitialDate();
		Date lastDate = project.getFinalDate();
		
		while (initialDate.getTime() < lastDate.getTime()) {

			// ==========================================
			// simulation of first station
			// ==========================================

			Event simulationEvents[] = (Event[]) eventGenerator.generateFirstStation(project);		
			SimulationEvent arrive = (SimulationEvent) simulationEvents[0];
			SimulationEvent leave = (SimulationEvent) simulationEvents[1];
			
			System.out.println("O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
			System.out.println("X: " + leave.getDate().toGMTString()+" Buses " + leave.getBusId());
			System.out.println(" ");
			
			project.setInitialDate(arrive.getDate());
			project.setNextDate(leave.getDate());
			initialDate = arrive.getDate();
			

			// ==========================================
			// simulation of next stations
			// ==========================================
			
			String tab = "";
			
			for (int i = 0; i < 2; i++) {
				
				tab += "	";
				
				simulationEvents = (Event[]) eventGenerator.generateNextStation(leave);
				arrive = (SimulationEvent) simulationEvents[0];
				leave = (SimulationEvent) simulationEvents[1];
				
				System.out.println(tab + "O: " + arrive.getDate().toGMTString()+" Buses " + arrive.getBusId());
				System.out.println(tab + "X: " + leave.getDate().toGMTString()+" Buses " + leave.getBusId());
				System.out.println(" ");
			}
		}
	}
}
