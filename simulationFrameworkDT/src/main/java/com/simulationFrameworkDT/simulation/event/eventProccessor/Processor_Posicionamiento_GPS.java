package com.simulationFrameworkDT.simulation.event.eventProccessor;

import com.simulationFrameworkDT.model.Datagram;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.TargetSystem;

public class Processor_Posicionamiento_GPS implements IEventProcessor {

	@Override
	public void processEvent(Event event, TargetSystem TargetSystem) {

		if (event instanceof Datagram) {
			
			Datagram datagram = (Datagram) event;
			long busId = datagram.getBusId();
			long lineId = datagram.getLineId();
			double longitude = datagram.getLongitude();
			double latitude = datagram.getLatitude();
		
			if (longitude != -1 && latitude != -1) {
				TargetSystem.moveBus(busId, lineId, longitude, latitude);
			}
		}

	}

}