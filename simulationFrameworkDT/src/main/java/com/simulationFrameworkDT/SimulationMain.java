package com.simulationFrameworkDT;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

public class SimulationMain {

	public static void main(String[] args) throws IOException, ParseException {
		
		SimController sm = new SimController(null);
		sm.initializeSCV(new File("../datagrams.csv"), ",");
		sm.setColumnNumberForSimulationVariables(0, 4, 5, 1, 7);
		
		dataTest(sm);
		startTest(sm);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
	}
	
	
	public static <T> void dataTest(SimController sm){
		
		System.out.println("plan Versions ========================================================================================================================================\n");
		ArrayList<SITMPlanVersion> planversions = sm.getPlanVersions();
		for (int i = 0; i < planversions.size(); i++) {System.out.println(planversions.get(i));}
		System.out.println();
		
		
		System.out.println("calendars ========================================================================================================================================\n");
		ArrayList<SITMCalendar> calendars = sm.getCalendarsByPlanVersion(261);
		for (int i = 0; i < calendars.size(); i++) {System.out.println(calendars.get(i));}
		System.out.println();
		
		
		System.out.println("lines =========================================================================================================================================\n");
		ArrayList<SITMLine> lines = sm.getLinesByPlanVersion(261);
		for (int i = 0; i < lines.size(); i++) {System.out.println(lines.get(i));}
		System.out.println();
		
		
		System.out.println("Stops ========================================================================================================================================\n");
		ArrayList<SITMStop> stops = sm.getDataSource().findAllStopsByLine(261, 131);
		for (int i = 0; i < stops.size(); i++) {System.out.println(stops.get(i));}
		System.out.println();
		
	}

	public static void startTest(SimController sm) throws ParseException {
				
		System.out.println("Events =================================================\n");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:08").getTime());
		Date last = new Date(dateFormat.parse("2019-06-20 18:30:00").getTime());
		
		sm.start(init,last,131);
	}
}
