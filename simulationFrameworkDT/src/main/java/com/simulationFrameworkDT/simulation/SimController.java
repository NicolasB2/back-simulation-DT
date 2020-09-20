package com.simulationFrameworkDT.simulation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.project.Project;
import com.simulationFrameworkDT.project.ProjectController;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.variableState.VariableController;
import com.simulationFrameworkDT.systemState.TargetSystem;
import com.simulationFrameworkDT.systemState.factorySITM.SITMBus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class SimController{

	// external packages
	@Autowired private DataSourceSystem dataSource;
	@Autowired private ProjectController projectController;
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
	
	public ArrayList<Event> getNextEvent(long lineId, Project project){
		return eventProvirderController.getNextEvent(lineId,project);
	}
	
	public void start(String projectName, long lineId) {
		
		Project project = projectController.loadProject(projectName);
		executionThread = new ExecutionThread(this,project);
		
		if(executionThread.isPause()) {
			executionThread.setPause(false);
			System.out.println("=======> simulation resumed");
		}else {
			executionThread.setVariables(lineId);
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
