package com.simulationFrameworkDT.restService.interfaces;

public interface ISimulationRest {

	public String simulation(String projectName, int headwayDesigned)throws Exception;
	public void start(String projectName);
	public void pause(String projectName);
	public void resume(String projectName);
	public void stop(String projectName);
}
