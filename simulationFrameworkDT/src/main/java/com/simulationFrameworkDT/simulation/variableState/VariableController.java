package com.simulationFrameworkDT.simulation.variableState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.simulationFrameworkDT.simulation.dataGenerationTools.ProbabilisticDistribution;

public class VariableController implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> headersValue;

	public VariableController() {
		headersValue = new HashMap<>();
	}

	public void addValue(String variable, ProbabilisticDistribution distribution) {
		headersValue.put(variable, distribution);
	}
	
	public void addValue(String variable, String value) {
		headersValue.put(variable, value);
	}

	public void deletedValue(String variable) {
		headersValue.remove(variable);
	}

	public void updateValue(String variable, Object value) {
		headersValue.replace(variable, value);
	}

	public void updateAllValues(HashMap<String, String> variables) {

		for (HashMap.Entry<String, String> entry : variables.entrySet()) {

			if (headersValue.containsKey(entry.getKey())) {
				headersValue.replace(entry.getKey(), entry.getValue());
			}
		}
	}

	public String getValue(String variable) {

		if (headersValue.containsKey(variable)) {
			if (headersValue.get(variable) instanceof String) {
				String value = (String) headersValue.get(variable);
				return value;
			}

			if (headersValue.get(variable) instanceof ProbabilisticDistribution) {
				ProbabilisticDistribution value = (ProbabilisticDistribution) headersValue.get(variable);
				return value.getNextDistributionValue() + "";
			}
		}

		return null;
	}

	public ArrayList<Variable> getAllVariables() {

		ArrayList<Variable> variables = new ArrayList<>();

		for (HashMap.Entry<String, Object> entry : headersValue.entrySet()) {
			variables.add(new Variable(entry.getKey(), getValue(entry.getKey())));
		}
		
		return variables;
	}

}
