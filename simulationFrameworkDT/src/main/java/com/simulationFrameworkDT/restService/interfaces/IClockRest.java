package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.simulation.state.Clock;

public interface IClockRest {

	public Clock getClock(String nameProject);
	
	public Clock setFastSpeed(String nameProject);
	public Clock setNormalSpeed(String nameProject);
	public Clock setSlowSpeed(String nameProject);

	public Clock setOneToOneSpeed(String nameProject);
	public Clock setOneToFiveSpeed(String nameProject);
	public Clock setOneToTenSpeed(String nameProject);
	public Clock setOneToThirtySpeed(String nameProject);
	public Clock setOneToSixtySpeed(String nameProject);
}
