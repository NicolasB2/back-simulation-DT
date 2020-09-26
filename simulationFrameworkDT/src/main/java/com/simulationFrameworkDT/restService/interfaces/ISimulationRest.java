package com.simulationFrameworkDT.restService.interfaces;

public interface ISimulationRest {

	public void start(String projectName, long lineId);
	public void pause();
	public void resume();
	public void stop();
}
