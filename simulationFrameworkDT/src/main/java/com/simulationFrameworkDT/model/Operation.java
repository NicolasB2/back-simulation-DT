package com.simulationFrameworkDT.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.simulationFrameworkDT.model.factorySITM.SITMStop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Operation {
	
	private ArrayList<SITMStop> stops;
	
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
	
	//Hash of headway
	private HashMap<Long, Double> meansHOBus;
	private HashMap<Long, Double> meansHOPassengers;
		
	public Operation(int headwayDesigned, ArrayList<SITMStop> stops) {
		long timeOfTravel = 2 * 60 * 60;
		this.headwayDesigned = headwayDesigned;
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
		this.stops = stops;
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
