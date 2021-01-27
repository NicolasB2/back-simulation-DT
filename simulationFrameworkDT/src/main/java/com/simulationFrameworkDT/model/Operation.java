package com.simulationFrameworkDT.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Operation {
	
	//Simulation
	private Date date;
	private boolean finished = true;
	
	//Important Variables
	private int headwayDesigned;
	private int numberOfBuses;
	
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
		long timeOfTravel = 2 * 60 * 60;
		this.headwayDesigned = headwayDesigned;
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
	}

	@Override
	public String toString() {
		Timestamp datePrint = new Timestamp(date.getTime());
		return  datePrint + ", headwayDesigned=" + headwayDesigned + ", numberOfBuses=" + numberOfBuses
				+ ", headwayCoefficientOfVariation=" + headwayCoefficientOfVariation + ", excessWaitingTime="
				+ excessWaitingTime + ", busesImpact=" + busesImpact + ", passengerSatisfaction="
				+ passengerSatisfaction;
	}

}
