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
import com.simulationFrameworkDT.model.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.model.factorySITM.SITMLine;
import com.simulationFrameworkDT.model.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

public class SimulationMain {

	public static void main(String[] args) throws IOException, ParseException {
		
		//dataTest();
		startTest();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
	}
	
	
	public static void dataTest(){
		
		DataSourceSystem ds = new DataSourceSystem();
		ds.initializeCsv(new File("datagrams/datagrams.csv"), ",");
		ds.setColumnNumberForSimulationVariables(0, 4, 5, 1, 7);
		
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

	public static void saveProject() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:00").getTime());
		Date last = new Date(dateFormat.parse("2019-06-20 18:01:00").getTime());
		
		StateController pc = new StateController();
		Project project = new Project();
		project.setName("test");
		project.setInitialDate(init);
		project.setFinalDate(last);
		project.setPlanVersionId(261);
		project.setFileName("datagrams.csv");
		project.setFileSplit(",");
		project.setFileType(DataSourceSystem.FILE_CSV);
		pc.saveProject(project);
	}
	
	public static void startTest() throws ParseException{
		SimController sm =  new SimController();
		sm.setDataSource(new DataSourceSystem());
		sm.setProjectController(new StateController());
		saveProject();
		sm.start("test.dat",131);
	}
}
