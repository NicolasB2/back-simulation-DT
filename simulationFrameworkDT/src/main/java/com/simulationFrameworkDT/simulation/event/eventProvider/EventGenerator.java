package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.ArrayList;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

public class EventGenerator {
	
	private ArrayList<Long> ids = new ArrayList<>();
	
	public Event[] generateFirstStation(Project project, IDistribution ai, IDistribution si){
		
		Date initialDate = project.getInitialDate();
		Date nextDate = project.getNextDate();

		int interArrivalTime = (int) ai.getSample(); //calculate the intern arrival time with the distribution
		int serviceTime = (int) si.getSample(); //calculate the service time with the distribution
		
		long initialTime = initialDate.getTime();
		long lastService = nextDate.getTime();
		
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime);
		
		//if current time less than last service time the current time change to last service
		if(arriveTime < lastService) {
			arriveTime = lastService;
		}
		
		long leaveTime = arriveTime + (serviceTime*1000); //calculate the moment which the bus leave the station
		Date leaveDate = new Date(leaveTime); 
		
		
		long id = generateId();//generate the bus id
		SimulationEvent arrive = new SimulationEvent(true,id,500250, 0, arrivetDate);//create the event for arrive
		SimulationEvent leave  = new SimulationEvent(false,id,500250, 0, leaveDate);// create the event for leave
		Event events [] = {arrive,leave};
		
		return events;
	}
	
	public Event[] generateNextStation(SimulationEvent simulationEvent, IDistribution ai, IDistribution si){
		
		Date initialDate = simulationEvent.getDate();

		int interArrivalTime = (int) ai.getSample(); //calculate the intern arrival time with the distribution
		int serviceTime = (int) si.getSample(); //calculate the service time with the distribution
		
		long initialTime = initialDate.getTime();
		
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime);
		
		long leaveTime = arriveTime + (serviceTime*1000); //calculate the moment which the bus leave the station
		Date leaveDate = new Date(leaveTime); 
		
		long id = simulationEvent.getBusId();//generate the bus id
		SimulationEvent arrive = new SimulationEvent(true,id,500250, 0, arrivetDate);//create the event for arrive
		SimulationEvent leave  = new SimulationEvent(false,id,500250, 0, leaveDate);// create the event for leave
		Event events [] = {arrive,leave};
		
		return events;
	}

	private long generateId() {
		
		long id = (long) (Math.random()*(9999-1000+1)+1000);
		
		while(ids.contains(id)) {
			id = (long) (Math.random()*(9999-1000+1)+1000);
		}
		
		return id;
	}
	
}
