package com.simulationFrameworkDT.simulation.event.eventProvider;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class EventGenerator {
	/*
	public static void main(String[] args) throws ParseException {
		
		Project project = new Project();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date initialDate = new Date(dateFormat.parse("2019-06-20 00:00:00").getTime());
		Date nextDate    = new Date(dateFormat.parse("2019-06-20 01:00:00").getTime());
		project.setInitialDate(initialDate);
		project.setNextDate(nextDate);
		
		EventGenerator eg = new EventGenerator();
		eg.generate(project);
		
	}*/
	public static void main(String[] args) {
		
		EventGenerator eg = new EventGenerator();
		
		Double num1 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num1);
		Double num2 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num2);
		Double num3 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num3);
		Double num4 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num4);
		Double num5 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num5);
		Double num6 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num6);
		Double num7 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num7);
		Double num8 = eg.LogNormalDistribution(111.67682, 1.09809);
		System.out.println(num8);
			
	}
	
	@SuppressWarnings("deprecation")
	public Event generate(Project project){
		
		Date init = project.getInitialDate();
		Queue<SITMBus> queueBus = new LinkedList<SITMBus>();
		Queue<Long> queueServiceTime = new LinkedList<Long>();
		
		int interArrivalTime = 0;
		int service = 0;
		
		while(init.getTime() < project.getNextDate().getTime()) {
			
			interArrivalTime = (int) WeibullDistribution(2.14908, 63.81296);
			long actualTime = init.getTime() + (interArrivalTime*1000);
			
			init = new Date(actualTime);
			
			queueBus.offer(new SITMBus());
			System.out.println(" ");
			System.out.println("O: "+init.toGMTString()+" Buses "+queueBus.size());
			
			if(!queueServiceTime.isEmpty()){
				long lastServiceTime = queueServiceTime.poll();
				if(actualTime < lastServiceTime)
					actualTime = lastServiceTime;
			}
			
			service = (int) WeibullDistribution(2.14908, 63.81296);
			long leaveTime = actualTime + (service*1000);
			
			Date date = new Date(leaveTime); 
			queueServiceTime.offer(leaveTime);
			System.out.println("X: "+date.toGMTString()+" Buses "+queueBus.size());
			
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
