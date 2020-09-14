package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.ArrayList;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;

public class EventProviderController {

	private EventFetcher eventFecher;
	private EventGenerator eventGenerator;

	public EventProviderController() {
		eventFecher = new EventFetcher();
		eventGenerator = new EventGenerator();
	}

	public ArrayList<Event> getNextEvent(Date initialDate, Date lastDate, long lineID){
		
		ArrayList<Event> events = eventFecher.allFetch(initialDate, lastDate, lineID);
		Event eventGenerated = eventGenerator.generate();

		if (eventGenerated != null) {
			events.add(eventGenerated);
		}

		return events;
	}

	public void setDataSource(DataSourceSystem dataSource) {
		eventFecher.setDataSource(dataSource);
	}

}
