package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.simulation.state.Clock;

public interface IClockRest {

	public Clock getClock(String projectName);
	
	public Clock setFastSpeed(String projectName);
	public Clock setNormalSpeed(String projectName);
	public Clock setSlowSpeed(String projectName);

	public Clock setOneToOneSpeed(String projectName);
	public Clock setOneToFiveSpeed(String projectName);
	public Clock setOneToTenSpeed(String projectName);
	public Clock setOneToThirtySpeed(String projectName);
	public Clock setOneToSixtySpeed(String projectName);
}
