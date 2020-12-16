package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.event.Event;
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
		ArrayList<Event> datagrams = dataSource.findAllOperationalTravelsByRange(project);
		return datagrams;
	}

}
