package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;

import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ExecutionThread extends Thread {

	private Project project;
	private SimController simController;
	private StateController projectController;
	
	private volatile boolean pause = false;
	private volatile boolean killed = false;
	
	public void kill() {
		pause = true;
		killed = true;
	}
	
	public ExecutionThread(SimController simController,Project project) {
		this.simController = simController;
		this.project = project;
		projectController = new StateController();
	}

	@SuppressWarnings("deprecation")
	public ArrayList<Event> getNextEvents(){	
		
		Date nextDate = new Date(project.getInitialDate().getTime()+project.getClock().getReadSpeed());
		ArrayList<Event> events = new ArrayList<>();
		
		if(nextDate.getTime()>project.getFinalDate().getTime()) {
			kill();
		}else {
			System.out.println(project.getInitialDate().toGMTString());
			project.setNextDate(nextDate);
			events = simController.getNextEvent(project);
			project.setInitialDate(nextDate);
			project.getClock().getNextTick(nextDate);
		}
		return events;
	}
	
	@Override
	public void run() {
		while (!killed) {
			while (!pause) {
				try {

					ArrayList<Event> events = getNextEvents();
									
					if(events==null) {
						
						kill();
						
					}else if (!events.isEmpty()) {			
						
						for (int i = 0; i < events.size(); i++) {
							simController.getEventProcessorController().processEvent(events.get(i),project.getTargetSystem());
						}
						
						project.updateVariables(simController.getLastRow(project));

						System.out.println();
						projectController.saveProject(project);
						sleep(project.getClock().getAnimationSpeed());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					pause = true;
				}
			}
		}
	}
}
