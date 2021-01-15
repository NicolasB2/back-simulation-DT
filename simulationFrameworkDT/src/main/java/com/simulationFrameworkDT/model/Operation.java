package com.simulationFrameworkDT.model;

import lombok.Getter;

@Getter
public class Operation {

	private double headwayCoefficientOfVariation;
	private double excessWaitingTime;
	private double busesImpact;
	private double passengerSatisfaction;
	
	private String date;
	
	private int usersSalomia;
	private int busesSalomia;
	
	private int busesRoad;	
	private int usersFloraInd;
	private int busesFloraInd;
	
	public void update(String date, int usersSalomia, int busesSalomia, int busesRoad, int usersFloraInd,int busesFloraInd) {
		this.date = date;
		this.usersSalomia = usersSalomia;
		this.busesSalomia = busesSalomia;
		this.busesRoad = busesRoad;
		this.usersFloraInd = usersFloraInd;
		this.busesFloraInd = busesFloraInd;
	}

	@Override
	public String toString() {
		return date + ", usersSalomia=" + usersSalomia + ", busesSalomia=" + busesSalomia
				+ ", busesRoad=" + busesRoad + 
				", usersFloraInd=" + usersFloraInd + ", busesFloraInd=" + busesFloraInd;
	}
	
	

}
