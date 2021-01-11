package com.simulationFrameworkDT.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.Analytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.StopDistribution;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

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
	
	private SimulationThread simulationThread;

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
	
	public void startSimulation(String projectName, int headwayDesigned) {
		
		StopDistribution[] stations = new StopDistribution[2];
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(9.459459459);
		
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.WeibullDistribution(1.55075, 601.44131);
		
		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.LogLogisticDistribution(41.56875, 2.83267);
		
		StopDistribution stop1 = new StopDistribution(500250, passenger, ai, si);
		
		StopDistribution stop2 = new StopDistribution(500250, passenger, ai, si);
		
		stations[0]=stop1;
		stations[1]=stop2;
		
		startSimulation(stations, projectName, headwayDesigned);
	}
	
	private void startSimulation(StopDistribution[] stations, String projectName, int headwayDesigned) {
		
		Project pro = projectController.getProject();
		
		if(pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
		
		simulationThread = new SimulationThread(projectController.getProject(), stations ,headwayDesigned);
		simulationThread.start();
	}
	
	public String simulationResults() {
		return "";
	}
}
