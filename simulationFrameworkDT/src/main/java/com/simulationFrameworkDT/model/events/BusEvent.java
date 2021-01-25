package com.simulationFrameworkDT.model.events;

import java.sql.Date;
import java.sql.Timestamp;

import com.simulationFrameworkDT.simulation.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusEvent extends Event {

	private boolean arrive;
	private long busId;
	private long stopId;
	private int passengers;
	
	public BusEvent(boolean arrive, long busId, long stopId, int passengers, Date date) {
		super(null,date);
		this.arrive = arrive;
		this.busId = busId;
		this.stopId = stopId;
		this.passengers = passengers;
	}

	@Override
	public String toString() {
		Timestamp dateTime= new Timestamp(getDate().getTime());
		String arriveText = arrive?"arrive":"leave";
		return dateTime.toString()+", " + arriveText + ", busId=" + busId + ", stopId=" + stopId + ", passengers=" + passengers;
	}
	
	public String getDateFormat() {
		Timestamp dateTime= new Timestamp(getDate().getTime());
		return dateTime.toString();
	}

}
