package com.simulationFrameworkDT.simulation.state;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Variable {
	
	private String header;
	private String value;
	
	public Variable(String header, String value) {
		super();
		this.header = header;
		this.value = value;
	}
	
}
