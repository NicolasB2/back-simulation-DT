package com.simulationFrameworkDT.model;

import com.simulationFrameworkDT.simulation.tools.IDistribution;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StopDistribution {

	private long stopId;
	private IDistribution passengersDistribution;
	private IDistribution interArrivalDistribution;
	private IDistribution serviceDistribution;
	
	public StopDistribution(long stopId, IDistribution passengersDistribution, IDistribution interArrivalDistribution, IDistribution serviceDistribution) {
		this.stopId = stopId;
		this.passengersDistribution = passengersDistribution;
		this.interArrivalDistribution = interArrivalDistribution;
		this.serviceDistribution = serviceDistribution;
	}
}
