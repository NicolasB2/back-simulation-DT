package com.simulationFrameworkDT.model;

import java.sql.Date;

import com.simulationFrameworkDT.simulation.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulationEvent extends Event {

	private long busId;
	private long stopId;
	private int passengers;
	private Date arriveDate;
	private Date leaveDate;
	
	public SimulationEvent(long busId, long stopId, int passengers, Date arriveDate, Date leaveDate) {
		this.busId = busId;
		this.stopId = stopId;
		this.passengers = passengers;
		this.arriveDate = arriveDate;
		this.leaveDate = leaveDate;
	}

	@Override
	public String toString() {
		return "SimulationEvent [busId=" + busId + ", stopId=" + stopId + ", passengers=" + passengers + ", arriveDate=" + arriveDate + ", leaveDate=" + leaveDate + "]";
	}
	
}
