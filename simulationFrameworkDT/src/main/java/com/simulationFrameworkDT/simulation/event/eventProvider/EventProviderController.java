package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;

import lombok.Getter;

@Getter
@Service
public class EventProviderController {
	
	@Autowired 
	private EventFetcher eventFecher;
	private EventGenerator eventGenerator;
	
	public EventProviderController() {
		eventFecher = new EventFetcher();
		eventGenerator = new EventGenerator();
	}

	public ArrayList<Event> getNextEvent(Project project){
		ArrayList<Event> events = eventFecher.allFetch(project);
		return events;
	}
	
	public ArrayList<Event> getNextSimulation(Project project){
//		eventGenerator.generateFirstStation(project);
		return null;
	}

	public void setDataSource(DataSourceSystem dataSource) {
		eventFecher.setDataSource(dataSource);
	}

}
