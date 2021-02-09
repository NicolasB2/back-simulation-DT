package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.analytics.SimulationResults;
import com.simulationFrameworkDT.model.SITM.SITMStop;

public interface ISimulationRest {

	//Simulation
	public String simulation(String projectName, int headwayDesigned);
	public SimulationResults getSimulationResults(String projectName);
	public ArrayList<SITMStop> getSimulatedStations(String projectName);
}
