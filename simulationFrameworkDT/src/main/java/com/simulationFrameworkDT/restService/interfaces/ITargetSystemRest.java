package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.model.SITM.SITMBus;

public interface ITargetSystemRest {

	public ArrayList<SITMBus> findAllBuses(String projectName);

}
