package com.simulationFrameworkDT.model.events;

import java.sql.Date;

import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatagramEvent extends Event{
	
	private long datagramDateTime;
	private String datagramDate;
	private long busId;
	private long stopId;
	private long odometer;
	private double longitude;
	private double latitude;
	private long taskId;
	private long lineId;
	private long tripId;
	
	public DatagramEvent(long datagramDateTime, String datagramDate, long busId, long stopId, long odometer, double longitude, double latitude, long taskId, long lineId, long tripId) {
		super(EventType.POSICIONAMIENTO_GPS, new Date(datagramDateTime));
		this.datagramDateTime = datagramDateTime;
		this.datagramDate = datagramDate;
		this.busId = busId;
		this.stopId = stopId;
		this.odometer = odometer;
		this.longitude = longitude;
		this.latitude = latitude;
		this.taskId = taskId;
		this.lineId = lineId;
		this.tripId = tripId;
	}

	@Override
	public String toString() {
		return "Datagram [datagramDateTime=" + datagramDateTime + ", datagramDate=" + datagramDate + ", busId=" + busId
				+ ", stopId=" + stopId + ", odometer=" + odometer + ", longitude=" + longitude + ", latitude="
				+ latitude + ", taskId=" + taskId + ", lineId=" + lineId + ", tripId=" + tripId + "]";
	}
}
