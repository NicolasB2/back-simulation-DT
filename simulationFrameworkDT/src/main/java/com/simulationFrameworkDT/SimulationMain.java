package com.simulationFrameworkDT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.model.ModelDataGenerator;
import com.simulationFrameworkDT.model.SITM.SITMCalendar;
import com.simulationFrameworkDT.model.SITM.SITMLine;
import com.simulationFrameworkDT.model.SITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.SITM.SITMStop;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

public class SimulationMain {

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		
//		dataTest();
		visualizationTest();	
		
//		int[] x = {210,240,270,300,330,360,390,600,900,1200};
//		for (int i = 0; i < 1; i++) {
//			simulationTest(1,x[i]);
//		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
	}
	
	public static void dataTest(){
		
		DataSourceSystem ds = new DataSourceSystem();
		ds.initializeCsv();
		
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

	public static void saveProject(SimController sm) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date init = new Date(dateFormat.parse("2019-06-20 18:00:00").getTime());
		Date last = new Date(dateFormat.parse("2019-06-20 18:30:00").getTime());
		
		StateController pc = sm.getProjectController();
		Project project = new Project();
		project.setProjectName("test");
		project.setInitialDate(init);
		project.setFinalDate(last);
		project.setPlanVersionId(261);
		project.setLineId(131);
		project.setFileName("datagrams.csv");
		project.setFileSplit(",");
		project.setFileType(DataSourceSystem.FILE_CSV);
		pc.saveProject(project);
	}
	
	public static void visualizationTest() throws ParseException{
		SimController sm =  new SimController();
		sm.setDataSource(new DataSourceSystem());
		sm.setProjectController(new StateController());
		sm.setEventProcessorController(new EventProcessorController());
		saveProject(sm);
		sm.startVisualization("test.dat");
	}
	
	public static SimController sm() {
		SimController sm =  new SimController();
		sm.setDataSource(new DataSourceSystem());
		sm.setProjectController(new StateController());
		sm.setEventProcessorController(new EventProcessorController());
		return sm;
	}
	
	public static void simulationTest(int x,int hd) throws ParseException, InterruptedException { 	
		
		SimController sm = sm();
		saveProject(sm);
		
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.LogLaplaceDistribution(0.0, 467.00000);

		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.LogLaplaceDistribution(0.0, 31.6666);

		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(6.54763);

		ModelDataGenerator mdg = new ModelDataGenerator(passenger, ai, si);
		
		SITMStop stop1 = new SITMStop(500250);
		stop1.addModelDataGenerator(mdg,131);
		sm.addStationToSimulation(stop1);

		ProbabilisticDistribution ai2 = new ProbabilisticDistribution();
		ai2.WeibullDistribution(1.52116, 599.809135);

		ProbabilisticDistribution si2 = new ProbabilisticDistribution();
		si2.LogLogisticDistribution(37.832223, 5.204677);

		ProbabilisticDistribution passenger2 = new ProbabilisticDistribution();
		passenger2.ExponentialDistribution(7.41318);

		ModelDataGenerator mdg2 = new ModelDataGenerator(passenger2, ai2, si2);
		SITMStop stop2 = new SITMStop(500300);
		stop2.addModelDataGenerator(mdg2, 131);
		sm.addStationToSimulation(stop2);
		
		HashMap<String, Object> results= sm.startSimulationManyExecutions(x, "test.dat", 131, hd);
		
		for (Map.Entry<String, Object> entry : results.entrySet()) {
		    System.out.println(entry.getKey() + ": " + entry.getValue());
		}	
	}
}
