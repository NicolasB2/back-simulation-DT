package com.simulationFrameworkDT.simulation.event.eventProvider;

import rcaller.RCaller;

public class DistributionLogInv {
	
	
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
