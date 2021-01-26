package com.simulationFrameworkDT.model;

import com.simulationFrameworkDT.simulation.tools.IDistribution;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModelDataGenerator {

	private IDistribution passengersDistribution;
	private IDistribution interArrivalDistribution;
	private IDistribution serviceDistribution;
	
	public ModelDataGenerator(IDistribution passengersDistribution, IDistribution interArrivalDistribution, IDistribution serviceDistribution) {
		this.passengersDistribution = passengersDistribution;
		this.interArrivalDistribution = interArrivalDistribution;
		this.serviceDistribution = serviceDistribution;
	}

	public boolean isSimulated() {
		boolean havePassengers = passengersDistribution != null;
		boolean haveInterArrival = interArrivalDistribution != null;
		boolean haveService = serviceDistribution != null;
		return havePassengers && haveInterArrival && haveService;
	}
}