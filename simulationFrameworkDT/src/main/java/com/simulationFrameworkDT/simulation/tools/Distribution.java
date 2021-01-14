package com.simulationFrameworkDT.simulation.tools;

import rcaller.RCaller;

public class Distribution implements IDistribution {
	
	RCaller caller;
	ProbabilisticDistribution pd;
	

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

		
	@SuppressWarnings("deprecation")
	public void LogNormal(double meanlog, double sdlog) {
		
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
	
	public static void main(String[] args) {
		
		
		ProbabilisticDistribution pd = new ProbabilisticDistribution();
		pd.LogLaplaceDistribution(0.0000 ,480.00000);
		
		for (int i = 0; i < 1000; i++) {
			System.out.println(Math.abs(pd.getSample()));
		}
	System.out.println("----------------------------------------------");
//		
//		ProbabilisticDistribution si = new ProbabilisticDistribution();
//		si.LogLogisticDistribution(41.56875, 2.83267); 
//		for (int i = 0; i < 100; i++) {
//			System.out.println(si.getSample());			
//		}
////		
//		ProbabilisticDistribution ai = new ProbabilisticDistribution();
//		ai.LogLogisticDistribution(38.32236	, 2.765178); 
//		for (int i = 0; i < 100; i++) {
//			System.out.println(ai.getSample());			
//		}
//		
		/*
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.GammaDistribution(254.37014,2.08500);
		for (int i = 0; i < 100; i++) {
			System.out.println(ai.getSample());			
		}
		
		*/
	System.out.println("--------------------------------------");
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.WeibullDistribution(1.43247,584.52700);
		for (int i = 0; i < 1000; i++) {
			System.out.println(ai.getSample());			
		}
	
	}
	

}
