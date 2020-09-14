package com.simulationFrameworkDT.simulation.event;

import java.util.HashMap;

import lombok.Data;

@Data
public class Event {
	
	private EventType type;
	private HashMap<String, String> context;
	
	public Event() {
		context = new HashMap<String, String>();
	}
        
}
