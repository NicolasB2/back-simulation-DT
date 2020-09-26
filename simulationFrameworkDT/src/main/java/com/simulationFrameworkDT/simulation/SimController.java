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
import com.simulationFrameworkDT.simulation.state.TargetSystem;

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

	public SimController() {

		// upload system state
		initialize_TargetSystem();

		// clock configuration
		clock = new Clock();

		// initialize relationships
		variables = new VariableController();
		eventProvirderController = new EventProviderController();
		eventProcessorController = new EventProcessorController();
		eventProvirderController.setDataSource(dataSource);
	}
	
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

	public HashMap<String,String> getLastRow(Project project){
		return dataSource.getLastRow(project);
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
		this.clock.setFastSpeed();
	}

	public void setNormalSpeed() {
		this.clock.setNormalSpeed();
	}

	public void setSlowSpeed() {
		this.clock.setSlowSpeed();
	}

	public void setOneToOneSpeed() {
		this.clock.setOneToOneSpeed();
	}

	public void setOneToFiveSpeed() {
		this.clock.setOneToFiveSpeed();
	}

	public void setOneToTenSpeed() {
		this.clock.setOneToTenSpeed();
	}

	public void setOneToThirtySpeed() {
		this.clock.setOneToThirtySpeed();
	}

	public void setOneToSixtySpeed() {
		this.clock.setOneToSixtySpeed();
	}
	
}
