package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.events.BusEvent;
import com.simulationFrameworkDT.model.events.PassangerEvent;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

public class EventGenerator {

	public Event generateAi(Date date,int headwayDesigned, long busId, long stopId){
		long initialTime = date.getTime();
		int interArrivalTime = headwayDesigned;
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime); // generate the date object
		return new BusEvent(true,busId,stopId, 0, arrivetDate);//create the event for arrive
	}
	
	public Event generateAi(Date date, IDistribution ai, long busId, long stopId){
		long initialTime = date.getTime();
		int interArrivalTime = (int) ai.getSample(); //calculate the intern arrival time with the distribution
		long arriveTime = initialTime + (interArrivalTime*1000); // calculate the arrive time
		Date arrivetDate = new Date(arriveTime); // generate the date object
		return new BusEvent(true,busId,stopId, 0, arrivetDate);//create the event for arrive
	}
	
	public Event generateSi(Date date, IDistribution si, long busId, long stopId){
		long initialTime = date.getTime();
		int serviceTime = (int) si.getSample(); //calculate the service time with the distribution
		long leaveTime = initialTime + (serviceTime*1000); //calculate the moment which the bus leave the station
		Date leaveDate = new Date(leaveTime); // generate the date object
		return new BusEvent(false,busId,stopId, 0, leaveDate);//create the event for leave
	}
	
	public Queue<PassangerEvent> generateUsers(Date initialDate, Date lastDate, IDistribution passenger,long stopId){

		Queue<PassangerEvent> passengersTime = new LinkedList<PassangerEvent>();

		while(initialDate.getTime() <= lastDate.getTime()) {		
			double aiPassenger = passenger.getSample()*1000;
			Date passengerArrivetime = new Date((long) (initialDate.getTime()+aiPassenger));
			passengersTime.offer(new PassangerEvent(passengerArrivetime,stopId));
			initialDate = passengerArrivetime;
		}
		
		return passengersTime;
	}
	
}
