package com.simulationFrameworkDT.simulation.event.eventProvider;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;

import rcaller.RCaller;

public class DistributionLogInv {

	public static void main(String[] args) throws ParseException {
		

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:00").getTime());
		Date end = new Date(dateFormat.parse("2019-06-20 18:30:00").getTime());
		
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0;
		int service = 0;
		
		while(init.getTime() < end.getTime()) {
			
			interArrivalTime = (int) getValue(415.5, 362.29);
			long actualTime = init.getTime() + (interArrivalTime*1000);
			
			service = (int) getValue(92.95, 352.19);
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
		
	}
	
	
	@SuppressWarnings("deprecation")
	public static double getValue(double meanlog, double sdlog) {
		String[] values = null;
		try {
            RCaller caller = new RCaller();
            caller.cleanRCode();
            
            caller.setRscriptExecutable("C:\\Program Files\\R\\R-4.0.3\\bin\\Rscript");
            caller.cleanRCode();
           
            boolean continuos=true;
            
            do {
            	
            	caller.addRCode("qsimulate <- c()");
            	caller.addRCode("n<-c(0:100)");
            	caller.addRCode("value=qlnorm(runif(1), meanlog = "+meanlog+", sdlog = "+sdlog+", lower.tail = TRUE, log.p = FALSE)");
            	caller.addRCode("cat(abs(log(value)))");
            	caller.addRCode("cat(\"\\n\")");
            	caller.addRCode("qsimulate <- c(qsimulate, abs(log(value)))");
            	
            	caller.runAndReturnResult("qsimulate");
            	
            	values = caller.getParser().getAsStringArray("qsimulate");
            	
            	if(!values[0].equals("Inf")) {
            		continuos=false;
            	}
				
			} while (continuos);
            
            
            
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
		
		
		return Double.parseDouble(values[0]);

	}
	
	
}
