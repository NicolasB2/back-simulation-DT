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
	private VisualizationAnalytics analytics;
	
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
		
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.LogLaplaceDistribution(0.0 ,467.00000);
		
		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.LogLaplaceDistribution(0.0, 31.6666);
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(6.54763);
		
		StopDistribution stop1 = new StopDistribution(500250, passenger, ai, si);
		
		//***************************************************************************
		
		ProbabilisticDistribution ai2 = new ProbabilisticDistribution();
		ai.WeibullDistribution(1.52116, 599.809135);
		
		ProbabilisticDistribution si2 = new ProbabilisticDistribution();
		si.LogLogisticDistribution(37.832223, 5.204677);
		
		ProbabilisticDistribution passenger2 = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(7.41318);
		
		StopDistribution stop2 = new StopDistribution(500300, passenger2, ai2, si2);
		
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
