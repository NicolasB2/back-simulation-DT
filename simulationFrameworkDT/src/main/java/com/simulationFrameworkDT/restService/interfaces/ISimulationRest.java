package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.model.Operation;

public interface ISimulationRest {

	public String simulation(String projectName, int headwayDesigned);
	public Operation getOperation(String projectName);
	public void start(String projectName);
	public void pause(String projectName);
	public void resume(String projectName);
	public void stop(String projectName);
}
