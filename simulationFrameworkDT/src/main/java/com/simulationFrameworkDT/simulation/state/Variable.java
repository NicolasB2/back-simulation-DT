package com.simulationFrameworkDT.simulation.state;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Variable implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String header;
	private String value;
	
	public Variable(String header, String value) {
		super();
		this.header = header;
		this.value = value;
	}
	
}
