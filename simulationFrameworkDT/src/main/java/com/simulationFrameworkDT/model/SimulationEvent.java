package com.simulationFrameworkDT.model;

import java.sql.Date;

import com.simulationFrameworkDT.simulation.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulationEvent extends Event {

	private boolean arrive;
	private long busId;
	private long stopId;
	private int passengers;
	private Date date;
	
	public SimulationEvent(boolean arrive, long busId, long stopId, int passengers, Date date) {
		this.arrive = arrive;
		this.busId = busId;
		this.stopId = stopId;
		this.passengers = passengers;
		this.date = date;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return "SimulationEvent [arrive=" + arrive + ", busId=" + busId + ", stopId=" + stopId + ", passengers=" + passengers + ", date=" + date.toGMTString() + "]";
	}

}
