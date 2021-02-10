package com.simulationFrameworkDT.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.SimulationResults;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.SITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
// Defines the SITM-MIO model to be simulated
public class SimController {

	@Autowired
	private DataSourceSystem dataSource;

	@Autowired
	private StateController projectController;

	@Autowired
	private EventProcessorController eventProcessorController;

	
	private VisualizationThread executionThread;
	private SimulationThread simulationThread;

	ArrayList<SITMStop> stations = new ArrayList<SITMStop>();

	public HashMap<String, String> getLastRow(Project project) {
		return dataSource.getLastRow(project);
	}

	public void startVisualization(String projectName) {

		Project pro = projectController.getProject();

		if (pro == null) {
			projectController.loadProject(projectName);
			pro = projectController.getProject();
		}
		executionThread = new VisualizationThread(this, pro);

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

		HashMap<String, Object> averages = new HashMap<String, Object>();

		for (int i = 0; i < stations.size(); i++) {

			averages.put(stations.get(i).getStopId() + "-MaxBuses", 0);
			averages.put(stations.get(i).getStopId() + "-MaxUsers", 0);
			averages.put(stations.get(i).getStopId() + "-MaxUsersDate", 0);
			averages.put(stations.get(i).getStopId() + "-MeanBus", 0);
			averages.put(stations.get(i).getStopId() + "-MeanPassengers", 0);

		}

		for (int i = 0; i < x; i++) {
			try {
				startSimulation(projectName, lineId, headwayDesigned);
				getSimulationThread().join();
			} catch (Exception e) {
				e.printStackTrace();
			}

			SimulationResults op = getSimulationThread().getSimulationResults();
			
			busesImpact += op.getBusesImpact();
			ewt += op.getExcessWaitingTime();
			hcv += op.getHeadwayCoefficientOfVariation();
			passengerSatisfaction += op.getPassengerSatisfaction();
			
			for (Map.Entry<Long, Double> meanBus : op.getMeansHOBus().entrySet()) {
				averages.put( meanBus.getKey()+"-MeanBus", meanBus.getValue()+(int)averages.get(meanBus.getKey()+"-MeanBus"));
			}
			
			for (Map.Entry<Long, Double> meanPassengers : op.getMeansHOPassengers().entrySet()) {
				averages.put( meanPassengers.getKey()+"-MeanPassengers", meanPassengers.getValue()+(int)averages.get(meanPassengers.getKey()+"-MeanPassengers"));
			}
			
			for (SITMStop sitmStop : stations) {
				averages.put(sitmStop.getStopId() + "-MaxBuses", sitmStop.getMaxBuses()+(int)averages.get(sitmStop.getStopId()+"-MaxBuses"));
				averages.put(sitmStop.getStopId() + "-MaxUsers", sitmStop.getMaxUsers()+(int)averages.get(sitmStop.getStopId()+"-MaxUsers"));
				averages.put(sitmStop.getStopId() + "-MaxUsersDate", sitmStop.getMaxUsersDate().getTime()+(int)averages.get(sitmStop.getStopId()+"-MaxUsersDate"));
			}

		}

		for (SITMStop sitmStop : stations) {

			averages.put(sitmStop.getStopId() + "-MaxBuses", ((int)averages.get(sitmStop.getStopId()+"-MaxBuses"))/x);
			averages.put(sitmStop.getStopId() + "-MaxUsers", ((int)averages.get(sitmStop.getStopId()+"-MaxUsers"))/x);
			averages.put(sitmStop.getStopId() + "-MaxUsersDate", ((long)averages.get(sitmStop.getStopId()+"-MaxUsersDate"))/x);
			averages.put(sitmStop.getStopId() + "-MeanBus", ((double)averages.get(sitmStop.getStopId()+"-MeanBus"))/x);
			averages.put(sitmStop.getStopId() + "-MeanPassengers", ((double)averages.get(sitmStop.getStopId()+"-MeanPassengers"))/x);
			
		}
		
		averages.put("promBusesImpact", (busesImpact / x));
		averages.put("promPassengerSatisfaction", (passengerSatisfaction / x));
		averages.put("promEwt", (ewt / x));
		averages.put("promHcv", (hcv / x));
		
		return averages;
	}
}
