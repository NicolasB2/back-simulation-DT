package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;

public class EventGenerator {
	
	@SuppressWarnings("deprecation")
	public Event generate0(Project project){
		
		Date init = project.getInitialDate();
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0;
		int service = 0;
		
		while(init.getTime() < project.getNextDate().getTime()) {
			
			interArrivalTime = (int) DistributionLogInv.getValue(415.5, 362.29);
			long actualTime = init.getTime() + (interArrivalTime*1000);
			
			service = (int) DistributionLogInv.getValue(92.95, 352.19);
			long leaveTime = actualTime + (service*1000);
			
			
			if(!queueServiceTime.isEmpty() && queueServiceTime.peek()<actualTime) {
				Date date = new Date(queueServiceTime.peek()); 
				queueBus.poll();
				queueServiceTime.poll();
				System.err.println(date.toGMTString()+" Buses "+queueBus.size());
			}
			
			queueBus.offer(new SITMBus());
			queueServiceTime.offer(leaveTime);
			init = new Date(actualTime);
			System.out.println(init.toGMTString()+" Buses "+queueBus.size());
		}
		
		while(!queueBus.isEmpty()) {
			Date date = new Date(queueServiceTime.peek()); 
			queueBus.poll();
			queueServiceTime.poll();
			System.err.println(date.toGMTString()+" Buses "+queueBus.size());
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public Event generate(Project project){
		
		Date init = project.getInitialDate();
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0;
		int service = 0;
		
		while(init.getTime() < project.getNextDate().getTime()) {
			
			interArrivalTime = (int) DistributionLogInv.getValue(415.5, 362.29);
			long actualTime = init.getTime() + (interArrivalTime*1000);
			
			init = new Date(actualTime);
			queueBus.offer(new SITMBus());
			System.out.println(" ");
			System.out.println("O: "+init.toGMTString()+" Buses "+queueBus.size());
			
			if(queueServiceTime.isEmpty()) {
				service = (int) DistributionLogInv.getValue(92.95, 352.19);
				long leaveTime = actualTime + (service*1000);
				
				Date date = new Date(leaveTime); 
				queueServiceTime.offer(leaveTime);
				System.out.println("X: "+date.toGMTString()+" Buses "+queueBus.size());
			}else {
				
				long lastServiceTime = queueServiceTime.poll();
				if(actualTime < lastServiceTime) {
					actualTime = lastServiceTime;
				}
				
				service = (int) DistributionLogInv.getValue(92.95, 352.19);
				long leaveTime = actualTime + (service*1000);
				
				Date date = new Date(leaveTime); 
				queueServiceTime.offer(leaveTime);
				System.out.println("X: "+date.toGMTString()+" Buses "+queueBus.size());
			}
			
			
		}
		
		return null;
	}

}
