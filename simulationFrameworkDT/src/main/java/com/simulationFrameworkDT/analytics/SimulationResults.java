package com.simulationFrameworkDT.analytics;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simulationFrameworkDT.model.SITM.SITMStop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimulationResults {
	
	private ArrayList<SITMStop> stops;
	
	//Simulation
	private boolean execution = true;
	private Date currentDate;
	private Timestamp currentDateTwo;
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
		
	public SimulationResults(int headwayDesigned, ArrayList<SITMStop> stops) {
		long timeOfTravel = 2 * 60 * 60;
		this.headwayDesigned = headwayDesigned;
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
		this.stops = stops;
	}

	@Override
	public String toString() {
		String variables = "";
	
		variables = "headwayDesigned=" + headwayDesigned + "\nnumberOfBuses=" + numberOfBuses
				+ "\nheadwayCoefficientOfVariation=" + headwayCoefficientOfVariation + "\nexcessWaitingTime="
				+ excessWaitingTime + "\nbusesImpact=" + busesImpact + "\npassengerSatisfaction="
				+ passengerSatisfaction;
		
		for (Map.Entry<Long, Double> entry : meansHOBus.entrySet()) {
			variables+="meanHOBBus idStop: "+entry.getKey();
			variables+="\n"+entry.getValue();
		}
		
		for (Map.Entry<Long, Double> entry : meansHOPassengers.entrySet()) {
			variables+="meanHOBPassangers idStop:"+entry.getKey();
			variables+="\n"+entry.getValue();
		}
		
		return variables;
	}

}
