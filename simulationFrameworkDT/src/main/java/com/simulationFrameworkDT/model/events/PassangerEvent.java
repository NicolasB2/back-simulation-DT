package com.simulationFrameworkDT.model.events;

import java.sql.Date;
import java.sql.Timestamp;

import com.simulationFrameworkDT.simulation.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassangerEvent extends Event {

	private long stopId;
	public PassangerEvent(Date date,long stopId) {
		super(null,date);
		this.stopId = stopId;
	}
	
	@Override
	public String toString() {
		Timestamp dateTime= new Timestamp(getDate().getTime());
		return dateTime.toString()+ ", stopId=" + stopId;
	}
	
}
