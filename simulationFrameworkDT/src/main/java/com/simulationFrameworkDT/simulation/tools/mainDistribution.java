package com.simulationFrameworkDT.simulation.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;


public class mainDistribution {

	
	
 	public static void main(String[] args) throws ParseException {
		
 		String type = "WeibullDistribution";
 		
        Hashtable<String, Object> params = new Hashtable<>();
        
        params.put("alpha", 2.14908); //Shape
        params.put("beta", 63.81296); //Scale
        
        ProbabilisticDistribution pd = new ProbabilisticDistribution(type, params);
        
        int ai = 0;
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date initialDate = new Date(dateFormat.parse("2019-06-20 03:00:00").getTime());
        
        long numberDate = initialDate.getTime();
        
        for (int i = 0; i < 1000; i++) {
        	ai = (int) (pd.getNextDistributionValue())*1000;
        	numberDate+=ai;	
        	Date newDate = new Date(numberDate);
        	//System.out.print(ai+" ");
        	System.out.println(newDate.toString());
        }
        
	}
 	

}
