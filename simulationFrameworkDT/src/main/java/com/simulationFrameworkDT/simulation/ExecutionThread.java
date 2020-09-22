package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.project.Project;
import com.simulationFrameworkDT.project.ProjectController;
import com.simulationFrameworkDT.simulation.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ExecutionThread extends Thread {

	private long lineId;
	private Project project;
	private SimController simController;
	private ProjectController projectController;
	
	private volatile boolean pause = false;
	private volatile boolean killed = false;
	
	public void kill() {
		pause = true;
		killed = true;
	}
	
	public ExecutionThread(SimController simController,Project project) {
		this.simController = simController;
		this.project = project;
		projectController = new ProjectController();
	}

	public void setVariables(long lineId) {
		this.lineId = lineId;
	}

	@SuppressWarnings("deprecation")
	public ArrayList<Event> getNextEvents(){
		
		Date nextDate = new Date(project.getInitialDate().getTime()+simController.getClock().getClockRate());
		ArrayList<Event> events = new ArrayList<>();
		
		if(nextDate.getTime()>project.getFinalDate().getTime()) {
			kill();
		}else {
			System.out.println(project.getInitialDate().toGMTString());
			events = simController.getNextEvent(lineId, project);
			project.setInitialDate(nextDate);
			simController.getClock().getNextTick(nextDate);
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
							simController.getEventProcessorController().processEvent(events.get(i),simController.getTargetSystem());
						}
						
						simController.getVariables().updateAllValues(simController.getLastRow(DataSourceSystem.FILE_CSV,project));

						System.out.println();
						projectController.saveProject(project);
						sleep(simController.getSpeed());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					pause = true;
				}
			}
		}
	}
}
