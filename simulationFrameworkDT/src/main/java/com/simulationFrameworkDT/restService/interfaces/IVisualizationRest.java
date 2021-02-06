package com.simulationFrameworkDT.restService.interfaces;

public interface IVisualizationRest {
	
	//Visualization
	public void start(String projectName);
	public void pause(String projectName);
	public void resume(String projectName);
	public void stop(String projectName);
}
