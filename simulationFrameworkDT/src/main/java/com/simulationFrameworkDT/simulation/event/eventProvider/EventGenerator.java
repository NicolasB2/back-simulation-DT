package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

public class EventGenerator {

	public Event generateAi(Date date,int headwayDesigned, long id){
		long initialTime = date.getTime();
		int interArrivalTime = headwayDesigned;
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime); // generate the date object
		return new SimulationEvent(true,id,500250, 0, arrivetDate);//create the event for arrive
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

		while(initialDate.getTime() <= lastDate.getTime()+(1000*60*60)) {
			
			double aiPassenger = passenger.getSample()*1000;
			Date passengerArrivetime = new Date((long) (initialDate.getTime()+aiPassenger));
			passengersTime.offer(passengerArrivetime);
			initialDate = passengerArrivetime;
		}
		
		return passengersTime;
	}
	
}
