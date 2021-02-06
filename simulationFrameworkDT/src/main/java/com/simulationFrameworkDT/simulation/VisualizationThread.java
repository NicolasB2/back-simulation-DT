package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.simulationFrameworkDT.analytics.VisualizationAnalytics;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.state.Project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class VisualizationThread extends Thread {

	private Project project;
	private SimController simController;
	private EventProviderController eventProvirderController;

	@Autowired
	private VisualizationAnalytics analytics;
	
	private volatile boolean pause = false;
	private volatile boolean killed = false;
	
	public void kill() {
		pause = true;
		killed = true;
	}
	
	public VisualizationThread(SimController simController, Project project) {
		this.simController = simController;
		this.project = project;
		this.eventProvirderController = new EventProviderController();
		this.eventProvirderController.setDataSource(simController.getDataSource());
		this.analytics = new  VisualizationAnalytics();
		this.analytics.setDataSource(simController.getDataSource());
		analytics.init(project);
	}

	public ArrayList<Event> getNextEvents(){
		
		Date nextDate = new Date(project.getInitialDate().getTime()+project.getClock().getReadSpeed());
		ArrayList<Event> events = new ArrayList<>();
		
		if(nextDate.getTime()>project.getFinalDate().getTime()) {
			
			kill();
			System.out.println("=======> Visualization finished");
			System.out.println();
			analytics.postAnalysis();
			
		}else {		
			project.setNextDate(nextDate);
			events = eventProvirderController.getNextEvent(project);
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
						System.out.println("=======> Visualization finished");
						
					}else if (!events.isEmpty()) {			
						
						for (int i = 0; i < events.size(); i++) {
							simController.getEventProcessorController().processEvent(events.get(i),project.getTargetSystem());
							analytics.analysisPerBus(events.get(i));
						}
						
						project.updateVariables(simController.getLastRow(project));
						simController.getProjectController().setProject(project);
						simController.getProjectController().saveProject(project);
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
