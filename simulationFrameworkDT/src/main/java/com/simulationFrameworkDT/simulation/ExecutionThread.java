package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.simulationFrameworkDT.analytics.Analytics;
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
	
	@Autowired
	private Analytics analytics;
	
	private volatile boolean pause = false;
	private volatile boolean killed = false;
	
	public void kill() {
		pause = true;
		killed = true;
	}
	
	public ExecutionThread(SimController simController,Project project, Analytics analytics) {
		this.simController = simController;
		this.project = project;
		this.analytics = analytics;
		projectController = new StateController();
	}

	@SuppressWarnings("deprecation")
	public ArrayList<Event> getNextEvents(){	
		Date nextDate = new Date(project.getInitialDate().getTime()+project.getClock().getReadSpeed());
		ArrayList<Event> events = new ArrayList<>();
		
		if(nextDate.getTime()>project.getFinalDate().getTime()) {
			
			kill();
			System.out.println("=======> simulation finished");
			
		}else {
			
//			System.out.println(project.getInitialDate().toGMTString());
			
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
					
					this.project = projectController.loadProject(this.project.getProjectName()+".dat");
					ArrayList<Event> events = getNextEvents();
									
					if(events==null) {
						
						kill();
						System.out.println("=======> simulation finished");
						
					}else if (!events.isEmpty()) {			
						
						for (int i = 0; i < events.size(); i++) {
							simController.getEventProcessorController().processEvent(events.get(i),project.getTargetSystem());
							analytics.analysisPerBus(events.get(i));
						}
						
						project.updateVariables(simController.getLastRow(project));

//						System.out.println();
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
