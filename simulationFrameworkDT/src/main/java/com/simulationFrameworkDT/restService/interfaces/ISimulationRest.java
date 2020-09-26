package com.simulationFrameworkDT.restService.interfaces;

public interface ISimulationRest {

	public void start(String projectName, long lineId);
	public void pause();
	public void resume();
	public void stop();

	public void setFastSpeed();
	public void setNormalSpeed();
	public void setSlowSpeed();

	public void setOneToOneSpeed();
	public void setOneToFiveSpeed();
	public void setOneToTenSpeed();
	public void setOneToThirtySpeed();
	public void setOneToSixtySpeed();
}
