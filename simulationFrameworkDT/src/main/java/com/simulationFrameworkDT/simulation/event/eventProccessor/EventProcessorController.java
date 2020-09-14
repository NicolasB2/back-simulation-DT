package com.simulationFrameworkDT.simulation.event.eventProccessor;

import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.EventType;
import com.simulationFrameworkDT.systemState.TargetSystem;

public class EventProcessorController {
	
	private IEventProcessor strategy;

	public void processEvent(Event event, TargetSystem targetSystem) {
		
		if(event.getType().equals(EventType.POSICIONAMIENTO_GPS)) {
			strategy = new Processor_Posicionamiento_GPS();
			strategy.processEvent(event,targetSystem);
		}
		
	}
	
}