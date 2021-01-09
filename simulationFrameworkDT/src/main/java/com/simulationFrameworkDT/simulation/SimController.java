package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.Analytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
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
	
	public void startSimulation(SITMStop[] stations, int headwayDesigned) {
		SimulationThread st = new SimulationThread(projectController.getProject(), stations ,headwayDesigned);
		st.start();
	}
	public static void main(String[] args) throws Exception {

		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(initialDate);
		project.setFinalDate(nextDate);
		
		SimController simController = new SimController();
		StateController stateController = new StateController();
		stateController.setProject(project);
		simController.setProjectController(stateController);

		SITMStop[] stops = new SITMStop[2];
		
		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(9.459459459);
		
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.WeibullDistribution(1.55075, 601.44131);
		
		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.LogLogisticDistribution(41.56875, 2.83267);
		
		SITMStop stop1 = new SITMStop(500250, passenger, ai, si);
		
		SITMStop stop2 = new SITMStop(500250, passenger, ai, si);
		
		stops[0]=stop1;
		stops[1]=stop2;
		
		simController.startSimulation(stops ,360);
	}
}
