package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class EventGenerator {
	
		
	public static void main(String[] args) throws Exception {
		
		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate    = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(nextDate);
		
		EventGenerator eg = new EventGenerator();
		eg.generate(project);
		
	}
	
//	public static void main(String[] args) {
//		
//		EventGenerator eg = new EventGenerator();
//		
//		Double num1 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num1);
//		Double num2 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num2);
//		Double num3 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num3);
//		Double num4 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num4);
//		Double num5 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num5);
//		Double num6 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num6);
//		Double num7 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num7);
//		Double num8 = eg.LogNormalDistribution(111.67682, 1.09809);
//		System.out.println(num8);		
//	}
	
	@SuppressWarnings("deprecation")
	public Event generate(Project project){
		
		Date initialTime = project.getInitialDate();
		Date lastTime = project.getNextDate();
		
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0; // seconds of intern arrival time
		int service = 0; // seconds of service time
		
		while(initialTime.getTime() < lastTime.getTime()) { // while between initial and final time
			
			// ==========================================
			// simulation of first station
			// ==========================================
			
			interArrivalTime = (int) WeibullDistribution(2.14908, 63.81296); //calculate the intern arrival time with the distribution
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
			
			service = (int) WeibullDistribution(2.14908, 63.81296); //calculate the service time with the distribution
			long leaveTime = currentTime + (service*1000); //calculate the moment which the bus leave the station
			
			queueServiceTime.offer(leaveTime);//Add leave time to service queue
			
			Date date = new Date(leaveTime); 
			System.out.println("X: "+date.toGMTString()+" Buses "+queueBus.size());
		
			// ==========================================
			// simulation of next station
			// ==========================================
			
			interArrivalTime = (int) WeibullDistribution(2.14908, 63.81296); //calculate the intern arrival time with the distribution
			long currentTime2 = queueServiceTime.element() + (interArrivalTime*1000); // current time
			initialTime = new Date(currentTime2); // update the initial time with the value of current time
			
			System.out.println(" ");
			System.out.println("	O: "+initialTime.toGMTString()+" Buses "+queueBus.size());
			
			//if the queue of services time isn't empty
			if(!queueServiceTime.isEmpty()){ 
				
				long lastServiceTime = queueServiceTime.poll();
				
				//if current time less than last service time the current time change to last service
				if(currentTime < lastServiceTime) {
					currentTime = lastServiceTime;
				}
			}
			
			service = (int) WeibullDistribution(2.14908, 63.81296); //calculate the service time with the distribution
			leaveTime = currentTime + (service*1000); //calculate the moment which the bus leave the station
			
			queueServiceTime.offer(leaveTime);//Add leave time to service queue
			
			date = new Date(leaveTime); 
			System.out.println("	X: "+date.toGMTString()+" Buses "+queueBus.size());
			
			
		}
		
		return null;
	}
	
	public double WeibullDistribution(double alpha, double beta ) {
		
		String type = "WeibullDistribution";
 		
        Hashtable<String, Object> params = new Hashtable<>();
        
        params.put("alpha", alpha); //Shape
        params.put("beta", beta); //Scale
        
		ProbabilisticDistribution pd = new ProbabilisticDistribution(type, params);
		
		
		return pd.getNextDistributionValue();
	}
	
	public double LogNormalDistribution (double scale, double shape) {
		
		String type = "LogNormalDistribution";
 		
        Hashtable<String, Object> params = new Hashtable<>();
        
        params.put("shape", shape);
        params.put("scale", scale);
        
		ProbabilisticDistribution pd = new ProbabilisticDistribution(type, params);
		
		
		return pd.getNextDistributionValue();
	}

}
