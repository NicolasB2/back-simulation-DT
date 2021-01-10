package com.simulationFrameworkDT.restService.interfaces;

public interface ISimulationRest {

	public void simulation(String projectName, int headwayDesigned);
	public void start(String projectName);
	public void pause(String projectName);
	public void resume(String projectName);
	public void stop(String projectName);
}
