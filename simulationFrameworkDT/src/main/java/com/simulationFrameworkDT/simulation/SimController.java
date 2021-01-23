package com.simulationFrameworkDT.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.VisualizationAnalytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.StopDistribution;
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
	private VisualizationAnalytics analytics;
	
	@Autowired 
	private StateController projectController;

	@Autowired 
	private EventProviderController eventProvirderController;
	
	@Autowired 
	private EventProcessorController eventProcessorController;

	private ExecutionThread executionThread;
	
	private SimulationThread simulationThread;
	
	ArrayList<StopDistribution> stations = new ArrayList<StopDistribution>();

	public HashMap<String,String> getLastRow(Project project){
		return dataSource.getLastRow(project);
	}
	
	public ArrayList<Event> getNextEvent(Project project){
		return eventProvirderController.getNextEvent(project);
	}
	
	public void start(String projectName) {
		
		Project pro = projectController.getProject();
		
		if(pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
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
	
	
	public void addStationToSimulation(StopDistribution stopDistribution) {
		if(stopDistribution.getInterArrivalDistribution()!=null && stopDistribution.getServiceDistribution()!=null &&stopDistribution.getPassengersDistribution()!=null) {
			stations.add(stopDistribution);
		}else {
			
		}
	}
	
	public void startSimulation(String projectName, int headwayDesigned) {
		startSimulation(this.stations, projectName, headwayDesigned);
	}
	
	private void startSimulation(ArrayList<StopDistribution> stations, String projectName, int headwayDesigned) {
		
		Project pro = projectController.getProject();
		
		if(pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
		
		simulationThread = new SimulationThread(projectController.getProject(), stations ,headwayDesigned);
		simulationThread.setSleepTime(0);
		simulationThread.start();
	}
	
	public String simulationResults() {
		return "";
	}
}
