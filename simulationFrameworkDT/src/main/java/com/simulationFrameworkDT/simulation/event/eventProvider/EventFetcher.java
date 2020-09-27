package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.factorySITM.SITMOperationalTravels;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.EventType;
import com.simulationFrameworkDT.simulation.state.Project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class EventFetcher {

	@Autowired
	private DataSourceSystem dataSource;

	public ArrayList<Event> allFetch(Project project) {
		ArrayList<SITMOperationalTravels> operationaTravels = dataSource.findAllOperationalTravelsByRange(project);
		ArrayList<Event> eventlist = new ArrayList<>();

		for (int i = 0; i < operationaTravels.size(); i++) {

			Event event = new Event();
			event.setType(EventType.POSICIONAMIENTO_GPS);

			HashMap<String, String> context = new HashMap<String, String>();
			context.put("opertravelId", operationaTravels.get(i).getOpertravelId() + "");
			context.put("busId", operationaTravels.get(i).getBusId() + "");
			context.put("longitude", operationaTravels.get(i).getGPS_X() + "");
			context.put("latitude", operationaTravels.get(i).getGPS_Y() + "");
			context.put("lineId", operationaTravels.get(i).getLineId() + "");
			context.put("eventDate", operationaTravels.get(i).getEventDate().getTime() + "");

			event.setContext(context);
			eventlist.add(event);

		}

		return eventlist;
	}

}
