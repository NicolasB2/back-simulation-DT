package com.simulationFrameworkDT.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.state.Clock;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;
import com.simulationFrameworkDT.systemState.TargetSystem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class SimController{

	// external packages
	@Autowired private DataSourceSystem dataSource;
	@Autowired private StateController projectController;
	private TargetSystem targetSystem;

	// simulation package
	private Clock clock;
	private VariableController variables;
	private EventProviderController eventProvirderController;
	private EventProcessorController eventProcessorController;

	// execution thread
	private ExecutionThread executionThread;

	// variables
	private int speed;

	public SimController() {

		// set default variables
		speed = Clock.NORMAL;

		// upload system state
		initialize_TargetSystem();

		// clock configuration
		clock = new Clock();
		clock.setClockMode(Clock.DISCRET);
		clock.setClockRate(Clock.ONE_TO_FIVE);

		// initialize relationships
		variables = new VariableController();
		eventProvirderController = new EventProviderController();
		eventProcessorController = new EventProcessorController();
		eventProvirderController.setDataSource(dataSource);
	}

//	public void initializeDB() {
//		dataSource = new DataSourceSystem();
//		eventProvirderController.setDataSource(dataSource);
//	}
	
//	public void initializeSCV(File sourceFile, String split) {
//		dataSource = new DataSourceSystem();
//		dataSource.initializeCsv(sourceFile, split);
//		eventProvirderController.setDataSource(dataSource);
//	}
	
	public void setDataSource(DataSourceSystem dataSource) {
		this.dataSource = dataSource;
		this.eventProvirderController.setDataSource(dataSource);
	}
	
	public void initialize_TargetSystem() {
		this.targetSystem = new TargetSystem();
	}
		
	public ArrayList<SITMBus> getBusesByLine(long lineId){
		return targetSystem.filterBusesByLineId(lineId);
	}

	public HashMap<String,String> getLastRow(String type, Project project){
		return dataSource.getLastRow(type,project);
	}
	
	public ArrayList<Event> getNextEvent(Project project){
		return eventProvirderController.getNextEvent(project);
	}
	
	public void start(String projectName, long lineId) {
		
		Project project = projectController.loadProject(projectName);
		project.setLineId(lineId);
		executionThread = new ExecutionThread(this,project);
		
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

	public void stop() {
		executionThread.kill();
		System.out.println("=======> simulation finished");
	}

	public void setFastSpeed() {
		this.speed = Clock.FAST;
		System.out.println("=======> set Fast Speed");
	}

	public void setNormalSpeed() {
		this.speed = Clock.NORMAL;
		System.out.println("=======> set Normal Speed");
	}

	public void setSlowSpeed() {
		this.speed = Clock.SLOW;
		System.out.println("=======> set Slow Speed");
	}

	public void setOneToOneSpeed() {
		this.clock.setClockRate(Clock.ONE_TO_ONE);
		System.out.println("=======> set One To One Speed");
	}

	public void setOneToFiveSpeed() {
		this.clock.setClockRate(Clock.ONE_TO_FIVE);
		System.out.println("=======> set One To Five Speed");
	}

	public void setOneToTenSpeed() {
		this.clock.setClockRate(Clock.ONE_TO_TEN);
		System.out.println("=======> set One To Ten Speed");
	}

	public void setOneToThirtySpeed() {
		this.clock.setClockRate(Clock.ONE_TO_THIRTY);
		System.out.println("=======> set One To Thirty Speed");
	}

	public void setOneToSixtySpeed() {
		this.clock.setClockRate(Clock.ONE_TO_SIXTY);
		System.out.println("=======> set One To Sixty Speed");
	}
	
}
