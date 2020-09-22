package com.simulationFrameworkDT.simulation.event.eventProccessor;

import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.TargetSystem;

public class Processor_Posicionamiento_GPS implements IEventProcessor {

	@Override
	public void processEvent(Event event, TargetSystem TargetSystem) {

		long busID = Long.parseLong(event.getContext().get("busId"));
		long lineID = Long.parseLong(event.getContext().get("lineId"));
		double longitude = Double.parseDouble(event.getContext().get("longitude"));
		double latitude = Double.parseDouble(event.getContext().get("latitude"));
		
		if (longitude != -1 && latitude != -1) {
			
			
			System.out.println("Processor_Posicionamiento_GPS ===> "+
					"BusId="+event.getContext().get("busId")+" "+
					"GPS_X="+event.getContext().get("longitude")+" "+
					"GPS_Y="+event.getContext().get("latitude"));
			
			
			TargetSystem.moveBus(busID, lineID, longitude, latitude);
		}

	}

}