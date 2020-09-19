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

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

public class SimulationMain {

	public static void main(String[] args) throws IOException, ParseException {
		
		DataSourceSystem ds = new DataSourceSystem();
		ds.initializeCsv(new File("datagrams/datagrams.csv"), ",");
		ds.setColumnNumberForSimulationVariables(0, 4, 5, 1, 7);
		dataTest(ds);
		startTest(ds);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
	}
	
	
	public static <T> void dataTest(DataSourceSystem ds){
		
		System.out.println("plan Versions ========================================================================================================================================\n");
		ArrayList<SITMPlanVersion> planversions = ds.findAllPlanVersions(DataSourceSystem.FILE_CSV);
		for (int i = 0; i < planversions.size(); i++) {System.out.println(planversions.get(i));}
		System.out.println();
		
		
		System.out.println("calendars ========================================================================================================================================\n");
		ArrayList<SITMCalendar> calendars = ds.findAllCalendarsByPlanVersion(DataSourceSystem.FILE_CSV,261);
		for (int i = 0; i < calendars.size(); i++) {System.out.println(calendars.get(i));}
		System.out.println();
		
		
		System.out.println("lines =========================================================================================================================================\n");
		ArrayList<SITMLine> lines = ds.findAllLinesByPlanVersion(DataSourceSystem.FILE_CSV,261);
		for (int i = 0; i < lines.size(); i++) {System.out.println(lines.get(i));}
		System.out.println();
		
		
		System.out.println("Stops ========================================================================================================================================\n");
		ArrayList<SITMStop> stops = ds.findAllStopsByLine(DataSourceSystem.FILE_CSV,261, 131);
		for (int i = 0; i < stops.size(); i++) {System.out.println(stops.get(i));}
		System.out.println();
		
	}

	public static void startTest(DataSourceSystem ds) throws ParseException {
		
		System.out.println("Events =================================================\n");
		SimController sm =  new SimController();
		sm.setDataSource(ds);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:08").getTime());
		Date last = new Date(dateFormat.parse("2019-06-20 18:30:00").getTime());
		
		sm.start(init,last,131);
	}
}
