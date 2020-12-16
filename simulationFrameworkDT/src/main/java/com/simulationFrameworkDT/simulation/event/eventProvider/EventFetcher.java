package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.analytics.Datagram;
import com.simulationFrameworkDT.dataSource.DataSourceSystem;
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
		ArrayList<Datagram> datagrams = dataSource.findAllOperationalTravelsByRange(project);
		ArrayList<Event> eventlist = new ArrayList<>();

		for (int i = 0; i < datagrams.size(); i++) {

			Event event = new Event();
			event.setType(EventType.POSICIONAMIENTO_GPS);

			HashMap<String, String> context = new HashMap<String, String>();
			context.put("opertravelId", 261 + "");
			context.put("busId", datagrams.get(i).getBusId() + "");
			context.put("stopId", datagrams.get(i).getStopId() + "");
			context.put("longitude", datagrams.get(i).getLongitude() + "");
			context.put("latitude", datagrams.get(i).getLatitude() + "");
			context.put("lineId", datagrams.get(i).getLineId() + "");
			context.put("eventDate", datagrams.get(i).getDatagramDateTime() + "");
			event.setContext(context);
			eventlist.add(event);

		}

		return eventlist;
	}

}
