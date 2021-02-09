package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.model.SITM.SITMBus;
import com.simulationFrameworkDT.model.SITM.SITMStop;

public interface ITargetSystemRest {

	public ArrayList<SITMBus> findAllBuses(String projectName);
	public ArrayList<SITMStop> findAllStops(String projectName);
}
