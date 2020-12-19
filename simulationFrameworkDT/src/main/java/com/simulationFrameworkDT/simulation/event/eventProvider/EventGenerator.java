package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.SimulationEvent;
import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class EventGenerator {
	
	@SuppressWarnings("deprecation")
	public Event generate(Project project){
		
		Date initialDate = project.getInitialDate();
		Date nextDate = project.getNextDate();

		int interArrivalTime = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131); //calculate the intern arrival time with the distribution
		int serviceTime = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131); //calculate the service time with the distribution
		
		long currentTime = initialDate.getTime() + (interArrivalTime*1000); // current time
		long lastService = nextDate.getTime();
		
		Date currentDate = new Date(currentTime); // update the initial time with the value of current time
		System.out.println("O: "+currentDate.toGMTString()+" Buses ");
		
		//if current time less than last service time the current time change to last service
		if(currentTime < lastService) {
			currentTime = lastService;
		}
		
		long leaveTime = currentTime + (serviceTime*1000); //calculate the moment which the bus leave the station
		
		Date date = new Date(leaveTime); 
		System.out.println("X: "+date.toGMTString()+" Buses ");
		System.out.println(" ");
		
		return new SimulationEvent(System.currentTimeMillis(), 0, currentDate, date);
	}
	
	@SuppressWarnings("deprecation")
	public void generateTest(Project project) {
		
		Date initialTime = project.getInitialDate();
		Date lastTime = project.getFinalDate();
		
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0; // seconds of intern arrival time
		int service = 0; // seconds of service time
		
		while(initialTime.getTime() < lastTime.getTime()) { // while between initial and final time
			
			// ==========================================
			// simulation of first station
			// ==========================================
			
			interArrivalTime = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131);; //calculate the intern arrival time with the distribution
			long currentTime = initialTime.getTime() + (interArrivalTime*1000); // current time
			initialTime = new Date(currentTime); // update the initial time with the value of current time
			
			queueBus.offer(new SITMBus()); // Added a bus into the station queue
			
			System.out.println(" ");
			System.out.println("O: "+initialTime.toGMTString()+" Buses "+queueBus.size());
			
			//if the queue of services time isn't empty
			if(!queueServiceTime.isEmpty()){ 
				
				long lastServiceTime = queueServiceTime.poll();
				
				//if current time less than last service time the current time change to last service
				if(currentTime < lastServiceTime) {
					currentTime = lastServiceTime;
				}
			}
			
			service = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131);; //calculate the service time with the distribution
			long leaveTime = currentTime + (service*1000); //calculate the moment which the bus leave the station
			
			queueServiceTime.offer(leaveTime);//Add leave time to service queue
			
			Date date = new Date(leaveTime); 
			System.out.println("X: "+date.toGMTString()+" Buses "+queueBus.size());
		
			// ==========================================
			// simulation of next station
			// ==========================================
			
			interArrivalTime = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131);; //calculate the intern arrival time with the distribution
			long currentTime2 = queueServiceTime.element() + (interArrivalTime*1000); // current time
			Date initialTime2 = new Date(currentTime2); // update the initial time with the value of current time
			
			System.out.println(" ");
			System.out.println("	O: "+initialTime2.toGMTString()+" Buses "+queueBus.size());
			
			//if the queue of services time isn't empty
			if(!queueServiceTime.isEmpty()){ 
				
				long lastServiceTime = queueServiceTime.poll();
				
				//if current time less than last service time the current time change to last service
				if(currentTime2 < lastServiceTime) {
					currentTime2 = lastServiceTime;
				}
			}
			
			service = (int) ProbabilisticDistribution.WeibullDistribution(1.55075, 601.44131);; //calculate the service time with the distribution
			leaveTime = currentTime2 + (service*1000); //calculate the moment which the bus leave the station
			
			queueServiceTime.offer(leaveTime);//Add leave time to service queue
			
			date = new Date(leaveTime); 
			System.out.println("	X: "+date.toGMTString()+" Buses "+queueBus.size());
			
		}
	}

}
