package com.simulationFrameworkDT.simulation.event.eventProccessor;

import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.TargetSystem;

public interface IEventProcessor {

	public void processEvent(Event event,TargetSystem targetSystem);
	
}
