package com.simulationFrameworkDT.simulation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.VisualizationAnalytics;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
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

	private ExecutionThread executionThread;

	private SimulationThread simulationThread;

	ArrayList<SITMStop> stations = new ArrayList<SITMStop>();

	public HashMap<String, String> getLastRow(Project project) {
		return dataSource.getLastRow(project);
	}

	public ArrayList<Event> getNextEvent(Project project) {
		return eventProvirderController.getNextEvent(project);
	}

	public void start(String projectName) {

		Project pro = projectController.getProject();

		if (pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
		executionThread = new ExecutionThread(this, pro, analytics);

		if (executionThread.isPause()) {
			executionThread.setPause(false);
			System.out.println("=======> simulation resumed");
		} else {
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

	public void addStationToSimulation(SITMStop stopDistribution) {
		stations.add(stopDistribution);
	}

	public void startSimulation(String projectName, long lineId, int headwayDesigned) {
		startSimulation(this.stations, projectName, lineId, headwayDesigned);
	}

	private void startSimulation(ArrayList<SITMStop> stations, String projectName, long lineId, int headwayDesigned) {

		Project pro = projectController.getProject();

		if (pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}

		simulationThread = new SimulationThread(projectController.getProject(), stations, lineId, headwayDesigned);
		simulationThread.setSleepTime(0);
		simulationThread.start();
	}

	public HashMap<String, Object> calculateAverages(int x, int hd) {

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
				startSimulation("test.dat", 131, hd);
				getSimulationThread().join();
			} catch (Exception e) {
				e.printStackTrace();
			}

			getSimulationThread().getOperation();
			busesImpact += getSimulationThread().getOperation().getBusesImpact();
			ewt += getSimulationThread().getOperation().getExcessWaitingTime();
			hcv += getSimulationThread().getOperation().getHeadwayCoefficientOfVariation();
			passengerSatisfaction += getSimulationThread().getOperation().getPassengerSatisfaction();
			maxBusFloraInd += getSimulationThread().getOperation().getMaxbusFloraInd();
			maxBusSalomia += getSimulationThread().getOperation().getMaxbusSalomia();
			maxUsersFloraInd += getSimulationThread().getOperation().getMaxUsersFloraInd();
			maxUsersSalomia += getSimulationThread().getOperation().getMaxUsersSalomia();
			meanHOBusSalomia += getSimulationThread().getOperation().getMeanHOBusSalomia();
			meanHOBusFloraInd += getSimulationThread().getOperation().getMeanHOBusFloraInd();
			meanHOUsersSalomia += getSimulationThread().getOperation().getMeanHOUsersSalomia();
			meanHOUsersFloraInd += getSimulationThread().getOperation().getMeanHOUsersFloraInd();
			maxUsersFloraIndDate += getSimulationThread().getOperation().getMaxUsersFloraIndDate().getTime();
			maxUsersSalomiaDate += getSimulationThread().getOperation().getMaxUsersSalomiaDate().getTime();
		}

		double promMaxBusFloraInd = (maxBusFloraInd / x);
		double promMaxBusSalomia = (maxBusSalomia / x);
		double promMeanHOBusSalomia = (meanHOBusSalomia / x);
		double promMeanHOBusFloraInd = (meanHOBusFloraInd / x);

		double promMaxUsersFloraInd = (maxUsersFloraInd / x);
		double promMaxUsersSalomia = (maxUsersSalomia / x);
		double promMeanHOUsersSalomia = (meanHOUsersSalomia / x);
		double promMeanHOUsersFloraInd = (meanHOUsersFloraInd / x);

		double promBusesImpact = (busesImpact / x);
		double promPassengerSatisfaction = (passengerSatisfaction / x);

		double promEwt = (ewt / x);
		double promHcv = (hcv / x);

		long promMaxUsersFloraIndDate = (maxUsersFloraIndDate / x);
		long promMaxUsersSalomiaDate = (maxUsersSalomiaDate / x);

		Timestamp dateTimeFlora = new Timestamp(promMaxUsersFloraIndDate);
		Timestamp dateTimeSalomia = new Timestamp(promMaxUsersSalomiaDate);

		/*
		 * System.out.println("Headway: "+hd);
		 * System.out.println("Cantidad buses: "+sm.getSimulationThread().getOperation()
		 * .getNumberOfBuses());
		 * 
		 * System.out.println("promMaxBusSalomia: "+promMaxBusSalomia);
		 * System.out.println("dateTimeSalomia: "+dateTimeSalomia);
		 * System.out.println("promMaxUsersSalomia: "+promMaxUsersSalomia);
		 * System.out.println("promMeanHOBusSalomia: "+promMeanHOBusSalomia);
		 * System.out.println("promMeanHOUsersSalomia: "+promMeanHOUsersSalomia);
		 * 
		 * System.out.println("promMaxBusFloraInd: "+promMaxBusFloraInd);
		 * System.out.println("dateTimeFlora: "+dateTimeFlora);
		 * System.out.println("promMaxUsersFloraInd: "+promMaxUsersFloraInd);
		 * System.out.println("promMeanHOBusFloraInd: "+promMeanHOBusFloraInd);
		 * System.out.println("promMeanHOUsersFloraInd: "+promMeanHOUsersFloraInd);
		 * 
		 * System.out.println("promBusesImpact: "+promBusesImpact);
		 * System.out.println("promPassengerSatisfaction: "+promPassengerSatisfaction);
		 * 
		 * System.out.println("promEwt: "+promEwt);
		 * System.out.println("promHcv: "+promHcv);
		 */

		System.out.println(hd);
		System.out.println(getSimulationThread().getOperation().getNumberOfBuses());

		System.out.println(promMaxBusSalomia);
		System.out.println(dateTimeSalomia);
		System.out.println(promMaxUsersSalomia);
		System.out.println(promMeanHOBusSalomia);
		System.out.println(promMeanHOUsersSalomia);

		System.out.println(promMaxBusFloraInd);
		System.out.println(dateTimeFlora);
		System.out.println(promMaxUsersFloraInd);
		System.out.println(promMeanHOBusFloraInd);
		System.out.println(promMeanHOUsersFloraInd);

		System.out.println(promBusesImpact);
		System.out.println(promPassengerSatisfaction);

		System.out.println(promEwt);
		System.out.println(promHcv);
		System.out.println("");

		HashMap<String, Object> averages = new HashMap<String, Object>();
		averages.put("promMaxBusSalomia", promMaxBusSalomia);
		averages.put("dateTimeSalomia", dateTimeSalomia);
		averages.put("promMaxUsersSalomia", promMaxUsersSalomia);
		averages.put("promMeanHOBusSalomia", promMeanHOBusSalomia);
		averages.put("promMeanHOUsersSalomia", promMeanHOUsersSalomia);
		averages.put("promMaxBusFloraInd", promMaxBusFloraInd);
		averages.put("dateTimeFlora", dateTimeFlora);
		averages.put("promMaxUsersFloraInd", promMaxUsersFloraInd);
		averages.put("promMeanHOBusFloraInd", promMeanHOBusFloraInd);
		averages.put("promMeanHOUsersFloraInd", promMeanHOUsersFloraInd);
		averages.put("promBusesImpact", promBusesImpact);
		averages.put("promPassengerSatisfaction", promPassengerSatisfaction);
		averages.put("promEwt", promEwt);
		averages.put("promHcv", promHcv);
		return averages;
	}
}
