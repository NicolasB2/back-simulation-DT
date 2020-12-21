package com.simulationFrameworkDT.simulation.tools;

import java.util.HashMap;

public interface IDistribution {

	double getSample();
	void typeHandler(String type, HashMap<String, Object> args);
}