package com.simulationFrameworkDT.simulation.event;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Event {
	
	private EventType type;
	private Date date;

	public Event(EventType type, Date date) {
		super();
		this.type = type;
		this.date = date;
	}
        
}
