package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	public static void main(String[] args) throws ParseException {
		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate    = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(nextDate);
		
		EventGenerator eg = new EventGenerator();
		eg.generate(project);
	}
	
	public EventProviderController() {
		eventFecher = new EventFetcher();
		eventGenerator = new EventGenerator();
	}

	public ArrayList<Event> getNextEvent(Project project){
//		System.out.println("====>"+project.getInitialDate().toGMTString()+" "+project.getNextDate().toGMTString());
		ArrayList<Event> events = eventFecher.allFetch(project);
		eventGenerator.generate(project);
		return events;
	}

	public void setDataSource(DataSourceSystem dataSource) {
		eventFecher.setDataSource(dataSource);
	}

}
