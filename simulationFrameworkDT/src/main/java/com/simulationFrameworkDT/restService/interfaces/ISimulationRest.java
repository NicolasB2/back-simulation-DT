package com.simulationFrameworkDT.restService.interfaces;

public interface ISimulationRest {

	public void start(String projectName, long lineId);
	public void pause(String projectName);
	public void resume(String projectName);
	public void stop(String projectName);
}
