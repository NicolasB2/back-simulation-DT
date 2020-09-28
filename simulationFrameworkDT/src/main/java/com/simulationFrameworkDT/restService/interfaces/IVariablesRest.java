package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.simulation.state.Variable;

public interface IVariablesRest {

	public ArrayList<Variable> setHeaders(String projectName, String[] headers);
	public ArrayList<Variable> getVariables(String projectName);
}
