package com.simulationFrameworkDT.model;

import com.simulationFrameworkDT.simulation.event.Event;

public class Datagram extends Event{
	
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
	
	public Datagram(long datagramDateTime, String datagramDate, long busId, long stopId, long odometer, double longitude, double latitude, long taskId, long lineId, long tripId) {
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
		return "Datagram [datagramDate=" + datagramDate + ", datagramDateTime=" + datagramDateTime + ", busId=" + busId
				+ ", stopId=" + stopId + ", odometer=" + odometer + ", longitude=" + longitude + ", latitude="
				+ latitude + ", taskId=" + taskId + ", lineId=" + lineId + ", tripId=" + tripId + "]";
	}
	
	public long getDatagramDateTime() {
		return datagramDateTime;
	}

	public void setDatagramDateTime(long datagramDateTime) {
		this.datagramDateTime = datagramDateTime;
	}

	public String getDatagramDate() {
		return datagramDate;
	}

	public void setDatagramDate(String datagramDate) {
		this.datagramDate = datagramDate;
	}

	public long getBusId() {
		return busId;
	}

	public void setBusId(long busId) {
		this.busId = busId;
	}

	public long getStopId() {
		return stopId;
	}

	public void setStopId(long stopId) {
		this.stopId = stopId;
	}

	public long getOdometer() {
		return odometer;
	}

	public void setOdometer(long odometer) {
		this.odometer = odometer;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public long getTripId() {
		return tripId;
	}

	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
}
