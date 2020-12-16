package com.simulationFrameworkDT.simulation.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;

public class TargetSystem implements Serializable {

	private static final long serialVersionUID = 1L;

	private HashMap<Long, SITMBus> buses ;


	public TargetSystem() {
		buses = new HashMap<Long, SITMBus>();
	}

	public void moveBus(long busId, long lineId, double longitude, double latitude) {
		
//		System.out.println("Move Bus ===> "+
//				"BusId="+busId+"  "+
//				"Longitude="+longitude+"  "+
//				"Latitude="+latitude);
		
		if(buses.containsKey(busId)) {
			
			buses.get(busId).setLatitude(latitude);
			buses.get(busId).setLongitude(longitude);
			buses.get(busId).setLineId(lineId);
			
		}else {
			
			SITMBus bus = new SITMBus();
			bus.setBusId(busId);
			bus.setLatitude(latitude);
			bus.setLongitude(longitude);
			bus.setLineId(lineId);
			buses.put(busId, bus);
		}		
	}

	public ArrayList<SITMBus> filterBusesByLineId(long lineID) {
		ArrayList<SITMBus> busesByLine = new ArrayList<>();

		for (Entry<Long, SITMBus> entry : buses.entrySet()) {
			SITMBus bus = entry.getValue();
			
			if(bus.getLineId()==lineID) {
				busesByLine.add(bus);
			}
		}
		return busesByLine;
	}

}
