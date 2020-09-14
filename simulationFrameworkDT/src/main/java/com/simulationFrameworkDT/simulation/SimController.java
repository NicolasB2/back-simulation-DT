package com.simulationFrameworkDT.simulation;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.variableState.VariableController;
import com.simulationFrameworkDT.systemState.TargetSystem;
import com.simulationFrameworkDT.systemState.factorySITM.SITMBus;
import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Service
public class SimController{

	// external packages
	private DataSourceSystem dataSource;
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
	private Date initialDate;
	private Date lastDate;

	@Autowired
	public SimController(DataSourceSystem dataSource) {

		this.dataSource=dataSource;

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
		executionThread = new ExecutionThread(this);
		eventProvirderController = new EventProviderController();
		eventProcessorController = new EventProcessorController();
		eventProvirderController.setDataSource(dataSource);
	}

	public void initializeDB() {
		dataSource = new DataSourceSystem();
		eventProvirderController.setDataSource(dataSource);
	}
	
	public void initializeSCV(File sourceFile, String split) {
		dataSource = new DataSourceSystem(sourceFile, split);
		eventProvirderController.setDataSource(dataSource);
	}
	
	public void initialize_TargetSystem() {
		this.targetSystem = new TargetSystem();
	}
	
	public void setDates(Date initialDate,Date lastDate) {
		this.initialDate = initialDate;
		this.lastDate = lastDate;
	}
	
	public void setHeaders(HashMap<String, Integer> headers) {
		dataSource.setHeaders(headers);
		//variables.addHeaders(headers);
	}
	
	public void setColumnNumberForSimulationVariables(int clock, int longitude, int latitude, int busID, int lineID) {
		dataSource.setColumnNumberForSimulationVariables(clock, longitude, latitude, busID, lineID);
	}
	
	public ArrayList<SITMPlanVersion> getPlanVersions() {
		return dataSource.findAllPlanVersions();
	}
	
	public ArrayList<SITMCalendar> getDateByPlanVersion(long planVersionID) {
		return dataSource.findAllCalendarsByPlanVersion(planVersionID);
	}
	
	public ArrayList<SITMLine> getLinesByPlanVersion(long planVersionId) {
		return dataSource.findAllLinesByPlanVersion(planVersionId);
	}
	
	public ArrayList<SITMStop> getStopsByLine(long planVersionId, long lineId){
		return dataSource.findAllStopsByLine(planVersionId, lineId);
	}
	
	public ArrayList<SITMBus> getBusesByLine(long lineId){
		return targetSystem.filterBusesByLineId(lineId);
	}

	public HashMap<String,String> getLastRow(){
		return dataSource.getLastRow();
	}
	
	public void start(long lineId) {
		
		if(executionThread.isPause()) {
			executionThread.setPause(false);
			System.out.println("=======> simulation resumed");
		}else {
			executionThread.setLineId(lineId);
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

	public ArrayList<Event> getNextEvents(long lineId){
		
		Date nextDate = new Date(initialDate.getTime()+clock.getClockRate());
		ArrayList<Event> events = new ArrayList<>();
		
		if(nextDate.getTime()>lastDate.getTime()) {
			events = null;
		}else {
			events = eventProvirderController.getNextEvent(initialDate,nextDate,lineId);
			getClock().getNextTick(nextDate);
			initialDate = nextDate;
		}
		return events;
	}
	
}

@Getter
@Setter
class ExecutionThread extends Thread {

	private long lineId;
	private SimController simController;
	private volatile boolean pause = false;
	private volatile boolean killed = false;

	public ExecutionThread(SimController simController) {
		this.simController = simController;
	}

	public void kill() {
		pause = true;
		killed = true;
	}

	@Override
	public void run() {
		while (!killed) {
			while (!pause) {
				try {

					ArrayList<Event> events = simController.getNextEvents(lineId);
									
					if(events==null) {
						
						pause = true;
						killed = true;
						System.out.println("=======> simulation finished");
						
					}else if (!events.isEmpty()) {			

						for (int i = 0; i < events.size(); i++) {
							simController.getEventProcessorController().processEvent(events.get(i),simController.getTargetSystem());
						}
						
						simController.getVariables().updateAllValues(simController.getLastRow());

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
