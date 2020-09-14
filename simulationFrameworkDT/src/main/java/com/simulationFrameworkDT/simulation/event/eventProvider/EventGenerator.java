package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.Hashtable;

import com.simulationFrameworkDT.simulation.dataGenerationTools.ProbabilisticDistribution;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.EventType;

public class EventGenerator {

	private ProbabilisticDistribution probabilisticDistribution;

	public EventGenerator() {

		String type = "ChiSquaredDistribution";
		Hashtable<String, Object> params = new Hashtable<>();
		params.put("degreesOfFreedom", 3.0);

		probabilisticDistribution = new ProbabilisticDistribution(type, params);
	}

	public Event generate() {

		double number = probabilisticDistribution.getNextDistributionValue();
		
		if (number > 0.8) {
			Event event = new Event();
			event.setType(EventType.REINICIO_IVU_BOX_FUENTE_PODER);
			return event;
		}

		return null;
	}

}
