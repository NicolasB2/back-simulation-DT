package com.simulationFrameworkDT.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Operation {
	
	//Simulation
	private String date;
	private int usersSalomia;
	private int usersFloraInd;
	private int busesSalomia;
	private int busesRoad;	
	private int busesFloraInd;
	
	//Important Variables
	private int headwayDesigned;
	private int numberOfBuses;
	
	private int maxUsersSalomia;
	private int maxUsersFloraInd;
	
	private int maxbusSalomia;
	private int maxbusFloraInd;
	
	//Analytics
	private double headwayCoefficientOfVariation;
	private double excessWaitingTime;
	private double busesImpact;
	private double passengerSatisfaction;
	
	private double meanHOUsersSalomia;
	private double meanHOUsersFloraInd;
	
	private double meanHOBusSalomia;
	private double meanHOBusFloraInd;
	
	public Operation(int headwayDesigned) {
		this.headwayDesigned = headwayDesigned;
	}
	
	public void update(String date, int usersSalomia, int busesSalomia, int busesRoad, int usersFloraInd,int busesFloraInd) {
		
		long timeOfTravel = 2 * 60 * 60;
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
		
		this.date = date;
		this.usersSalomia = usersSalomia;
		this.busesSalomia = busesSalomia;
		this.busesRoad = busesRoad;
		this.usersFloraInd = usersFloraInd;
		this.busesFloraInd = busesFloraInd;
		
		if(usersSalomia>maxUsersSalomia) {
			maxUsersSalomia = usersSalomia;
		}
		
		if(usersFloraInd>maxUsersFloraInd) {	
			maxUsersFloraInd = usersFloraInd;
		}
		
		if(busesSalomia>maxbusSalomia) {
			maxbusSalomia = busesSalomia;
		}
		
		if(busesFloraInd>maxbusFloraInd) {
			maxbusFloraInd = busesFloraInd;
		}
	}

//	@Override
//	public String toString() {
//		return date + ", usersSalomia=" + usersSalomia + ", busesSalomia=" + busesSalomia
//				+ ", busesRoad=" + busesRoad + 
//				", usersFloraInd=" + usersFloraInd + ", busesFloraInd=" + busesFloraInd;
//	}

	@Override
	public String toString() {
		return "Operation [date=" + date + ", headwayDesigned=" + headwayDesigned + ", numberOfBuses=" + numberOfBuses
				+ ", usersSalomia=" + usersSalomia + ", busesSalomia=" + busesSalomia + ", busesRoad=" + busesRoad
				+ ", usersFloraInd=" + usersFloraInd + ", busesFloraInd=" + busesFloraInd
				+ ", headwayCoefficientOfVariation=" + headwayCoefficientOfVariation + ", excessWaitingTime="
				+ excessWaitingTime + ", busesImpact=" + busesImpact + ", passengerSatisfaction="
				+ passengerSatisfaction + ", maxUsersSalomia=" + maxUsersSalomia + ", maxUsersFloraInd="
				+ maxUsersFloraInd + ", maxbusSalomia=" + maxbusSalomia + ", maxbusFloraInd=" + maxbusFloraInd + "]";
	}

}
