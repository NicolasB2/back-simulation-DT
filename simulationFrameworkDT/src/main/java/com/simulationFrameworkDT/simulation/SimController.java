package com.simulationFrameworkDT.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.Analytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class SimController{

	@Autowired 
	private DataSourceSystem dataSource;
	
	@Autowired
	private Analytics analytics;
	
	@Autowired 
	private StateController projectController;

	@Autowired 
	private EventProviderController eventProvirderController;
	
	@Autowired 
	private EventProcessorController eventProcessorController;

	private ExecutionThread executionThread;

	public HashMap<String,String> getLastRow(Project project){
		return dataSource.getLastRow(project);
	}
	
	public ArrayList<Event> getNextEvent(Project project){
		return eventProvirderController.getNextEvent(project);
	}
	
	public void start(String projectName) {
		
		Project pro = projectController.getProject();
		executionThread = new ExecutionThread(this,pro,analytics);
		
		
		if(executionThread.isPause()) {
			executionThread.setPause(false);
			System.out.println("=======> simulation resumed");
		}else {
			executionThread.start();
			System.out.println("=======> simulation started");
		}	
	}

	public void pause() {
		executionThread.setPause(true);
		System.out.println("=======> simulation paused");
	}

	public void resume() {
		executionThread.setPause(false);
		System.out.println("=======> simulation resumed");
	}

	public void finish() {
		executionThread.kill();
		System.out.println("=======> simulation finished");
	}
	
}
