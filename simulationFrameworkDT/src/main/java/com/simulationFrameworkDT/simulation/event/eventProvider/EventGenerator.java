package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

public class EventGenerator {
	
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
		
		
		long id = 00;//generate the bus id
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
	
	public Event generateAi(Date date, IDistribution ai, long id){
		long initialTime = date.getTime();
		int interArrivalTime = (int) ai.getSample(); //calculate the intern arrival time with the distribution
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime); // generate the date object
		return new SimulationEvent(true,id,500250, 0, arrivetDate);//create the event for arrive
	}
	
	public Event generateSi(Date date, IDistribution si, long id){
		long initialTime = date.getTime();
		int serviceTime = (int) si.getSample(); //calculate the service time with the distribution
		long leaveTime = initialTime + (serviceTime*1000); //calculate the moment which the bus leave the station
		Date leaveDate = new Date(leaveTime); // generate the date object
		return new SimulationEvent(false,id,500250, 0, leaveDate);//create the event for leave
	}
	
	public Queue<Date> generateUsers(Date initialDate, Date lastDate, IDistribution passenger){

		Queue<Date> passengersTime = new LinkedList<Date>();
		int simulationTime =  (int) ((lastDate.getTime()-initialDate.getTime())/1000)/60;
		
		for (int i = 1; i <= simulationTime+60; i++) {
			
			int numPassenger = (int) Math.round(passenger.getSample());
			//System.out.println("numPassenger "+numPassenger);
			
			for (int j = 0; j < numPassenger; j++) {
				Date passengerArrivetime = new Date(initialDate.getTime() + (i * 60 * 1000));
				passengersTime.offer(passengerArrivetime);
				//System.out.println(time.toGMTString());
			}
			
		}
		
		return passengersTime;
	}
	
}
