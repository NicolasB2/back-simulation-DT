package com.simulationFrameworkDT.simulation.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import rcaller.RCaller;

public class Distribution implements IDistribution {
	
	RCaller caller;

	@Override
	public double getSample() {
		
		String[] values = null;
		Boolean continuos = false;
		     	
     	do {

     		caller.runAndReturnResult("qsimulate");
     		values = caller.getParser().getAsStringArray("qsimulate");
     		if(!values[0].equals("Inf")) {
     			continuos=false;
     		}
			
		} while (continuos);
     			
		return Double.parseDouble(values[0]);
	}

		
	public void LogNormal(double meanlog, double sdlog) {
		
		String[] values = null;
		try {
            RCaller caller = new RCaller();
            caller.cleanRCode();
            
            caller.setRscriptExecutable("C:\\Program Files\\R\\R-4.0.3\\bin\\Rscript");
            caller.cleanRCode();
          
            caller.addRCode("qsimulate <- c()");
        	caller.addRCode("n<-c(0:100)");
        	caller.addRCode("value=qlnorm(runif(1), meanlog = "+meanlog+", sdlog = "+sdlog+", lower.tail = TRUE, log.p = FALSE)");
        	caller.addRCode("cat(abs(log(value)))");
            caller.addRCode("cat(\"\\n\")");
            caller.addRCode("qsimulate <- c(qsimulate, abs(log(value)))"); 	
            this.caller = caller;           
        } catch (Exception e) {
            System.out.println(e.toString());
        }
		
	}
	
	
	@Override
	public void typeHandler(String type, HashMap<String, Object> args) {
		 
		
		
		
		
	}
	

	public static void main(String[] args) {
		
		Distribution dt = new Distribution();
		
		dt.LogNormal(100, 10);
		
		for (int i = 0; i < 10; i++) {
			System.out.println(dt.getSample());			
		}
	}

}
