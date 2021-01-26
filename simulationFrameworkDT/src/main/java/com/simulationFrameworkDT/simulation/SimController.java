package com.simulationFrameworkDT.simulation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.VisualizationAnalytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.Operation;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
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
public class SimController {

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

	private VisualizationThread executionThread;

	private SimulationThread simulationThread;

	ArrayList<SITMStop> stations = new ArrayList<SITMStop>();

	public HashMap<String, String> getLastRow(Project project) {
		return dataSource.getLastRow(project);
	}

	public ArrayList<Event> getNextEvent(Project project) {
		return eventProvirderController.getNextEvent(project);
	}

	public void startVisualization(String projectName) {

		Project pro = projectController.getProject();

		if (pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
		executionThread = new VisualizationThread(this, pro, analytics);

		if (executionThread.isPause()) {
			executionThread.setPause(false);
			System.out.println("=======> Visualization resumed");
		} else {
			executionThread.start();
			System.out.println("=======> Visualization started");
		}
	}

	public void pauseVisualization() {
		executionThread.setPause(true);
		System.out.println("=======> Visualization paused");
	}

	public void resumeVisualization() {
		executionThread.setPause(false);
		System.out.println("=======> Visualization resumed");
	}

	public void finishVisualization() {
		executionThread.kill();
		System.out.println("=======> Visualization finished");
	}

	public void addStationToSimulation(SITMStop stopDistribution) {
		stations.add(stopDistribution);
	}

	public void startSimulation(String projectName, long lineId, int headwayDesigned) {
		
		Project pro = projectController.getProject();

		if (pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}

		simulationThread = new SimulationThread(projectController.getProject(), stations, lineId, headwayDesigned);
		simulationThread.setSleepTime(0);
		simulationThread.start();
	}

	public HashMap<String, Object> startSimulationManyExecutions(int x, String projectName, long lineId, int headwayDesigned) {

		double busesImpact = 0; 
		double passengerSatisfaction = 0;

		double ewt = 0;
		double hcv = 0;

		int maxBusFloraInd = 0;
		int maxBusSalomia = 0;
		double meanHOBusSalomia = 0;
		double meanHOBusFloraInd = 0;

		int maxUsersFloraInd = 0;
		int maxUsersSalomia = 0;
		double meanHOUsersSalomia = 0;
		double meanHOUsersFloraInd = 0;

		long maxUsersSalomiaDate = 0;
		long maxUsersFloraIndDate = 0;

		for (int i = 0; i < x; i++) {
			try {
				startSimulation(projectName, lineId, headwayDesigned);
				getSimulationThread().join();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Operation op = getSimulationThread().getOperation();
			busesImpact += op.getBusesImpact();
			ewt += op.getExcessWaitingTime();
			hcv += op.getHeadwayCoefficientOfVariation();
			passengerSatisfaction += op.getPassengerSatisfaction();
			maxBusFloraInd += op.getMaxbusFloraInd();
			maxBusSalomia += op.getMaxbusSalomia();
			maxUsersFloraInd += op.getMaxUsersFloraInd();
			maxUsersSalomia += op.getMaxUsersSalomia();
			meanHOBusSalomia += op.getMeanHOBusSalomia();
			meanHOBusFloraInd += op.getMeanHOBusFloraInd();
			meanHOUsersSalomia += op.getMeanHOUsersSalomia();
			meanHOUsersFloraInd += op.getMeanHOUsersFloraInd();
			maxUsersFloraIndDate += op.getMaxUsersFloraIndDate().getTime();
			maxUsersSalomiaDate += op.getMaxUsersSalomiaDate().getTime();
		}


		long promMaxUsersFloraIndDate = (maxUsersFloraIndDate / x);
		long promMaxUsersSalomiaDate = (maxUsersSalomiaDate / x);

		Timestamp dateTimeFlora = new Timestamp(promMaxUsersFloraIndDate);
		Timestamp dateTimeSalomia = new Timestamp(promMaxUsersSalomiaDate);

		HashMap<String, Object> averages = new HashMap<String, Object>();
		averages.put("promMaxBusSalomia", (maxBusSalomia / x));
		averages.put("dateTimeSalomia", dateTimeSalomia);
		averages.put("promMaxUsersSalomia", (maxUsersSalomia / x));
		averages.put("promMeanHOBusSalomia", (meanHOBusSalomia / x));
		averages.put("promMeanHOUsersSalomia", (meanHOUsersSalomia / x));
		averages.put("promMaxBusFloraInd", (maxBusFloraInd / x));
		averages.put("dateTimeFlora", dateTimeFlora);
		averages.put("promMaxUsersFloraInd", (maxUsersFloraInd / x));
		averages.put("promMeanHOBusFloraInd", (meanHOBusFloraInd / x));
		averages.put("promMeanHOUsersFloraInd", (meanHOUsersFloraInd / x));
		averages.put("promBusesImpact", (busesImpact / x));
		averages.put("promPassengerSatisfaction", (passengerSatisfaction / x));
		averages.put("promEwt", (ewt / x));
		averages.put("promHcv", (hcv / x));
		return averages;
	}
}
