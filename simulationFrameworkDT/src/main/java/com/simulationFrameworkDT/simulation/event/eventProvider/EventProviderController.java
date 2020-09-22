package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.ArrayList;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;

public class EventProviderController {

	private EventFetcher eventFecher;
	private EventGenerator eventGenerator;

	public EventProviderController() {
		eventFecher = new EventFetcher();
		eventGenerator = new EventGenerator();
	}

	public ArrayList<Event> getNextEvent(Project project){
		
		ArrayList<Event> events = eventFecher.allFetch(project);
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
