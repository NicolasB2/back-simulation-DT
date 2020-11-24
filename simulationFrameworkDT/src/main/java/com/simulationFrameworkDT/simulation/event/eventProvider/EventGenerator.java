package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;

public class EventGenerator {
	
	public Event generate() throws ParseException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:00").getTime());
		Date end = new Date(dateFormat.parse("2019-06-20 18:30:00").getTime());
		
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0;
		int service = 0;
		
		while(init.getTime() < end.getTime()) {
			
			interArrivalTime = (int) DistributionLogInv.getValue(415.5, 362.29);
			long actualTime = init.getTime() + (interArrivalTime*1000);
			
			service = (int) DistributionLogInv.getValue(92.95, 352.19);
			long leaveTime = actualTime + (service*1000);
			
			
			if(!queueServiceTime.isEmpty() && queueServiceTime.peek()<actualTime) {
				Date date = new Date(queueServiceTime.peek()); 
				queueBus.poll();
				queueServiceTime.poll();
				System.err.println(date.toString()+" Buses"+queueBus.size());
			}
			
			queueBus.offer(new SITMBus());
			queueServiceTime.offer(leaveTime);
			init = new Date(actualTime);
			System.out.println(init.toString()+" Buses"+queueBus.size());
		}
		
		while(!queueBus.isEmpty()) {
			Date date = new Date(queueServiceTime.peek()); 
			queueBus.poll();
			queueServiceTime.poll();
			System.err.println(date.toString()+" Buses"+queueBus.size());
		}
		
		return null;
	}

}
