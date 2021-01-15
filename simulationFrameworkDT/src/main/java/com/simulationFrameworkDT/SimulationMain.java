package com.simulationFrameworkDT;

import java.io.BufferedReader;
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
import com.simulationFrameworkDT.simulation.event.eventProccessor.EventProcessorController;
import com.simulationFrameworkDT.simulation.event.eventProvider.EventProviderController;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;
import com.simulationFrameworkDT.analytics.*;

public class SimulationMain {

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		
//		dataTest();
//		visualizationTest();
		simulationTest(1,120);
		
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
		
		Date init = new Date(dateFormat.parse("2019-06-20 05:00:00").getTime());
		Date last = new Date(dateFormat.parse("2019-06-20 11:00:00").getTime());
		
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
		sm.setAnalytics(new VisualizationAnalytics());
		sm.getAnalytics().setDataSource(sm.getDataSource());
		sm.setProjectController(new StateController());
		sm.setEventProcessorController(new EventProcessorController());
		sm.setEventProvirderController(new EventProviderController());
		sm.getEventProvirderController().getEventFecher().setDataSource(sm.getDataSource());
		saveProject(sm);
		sm.start("test.dat");
	}
	
	public static SimController sm() {
		SimController sm =  new SimController();
		sm.setDataSource(new DataSourceSystem());
		sm.setAnalytics(new VisualizationAnalytics());
		sm.getAnalytics().setDataSource(sm.getDataSource());
		sm.setProjectController(new StateController());
		sm.setEventProcessorController(new EventProcessorController());
		sm.setEventProvirderController(new EventProviderController());
		sm.getEventProvirderController().getEventFecher().setDataSource(sm.getDataSource());
		return sm;
	}
	public static void simulationTest(int x,int hd) throws ParseException, InterruptedException {
		
		SimController sm = sm();
		saveProject(sm);
		
		int busesFloraInd = 0;
		int busesSalomia = 0;
		double busesImpact = 0;
		int busesRoad = 0;
		double ewt= 0;
		double hcv= 0;
		double passengerSatisfaction=0;
		int usersFloraInd = 0;
		int userSalomia = 0;
		
		for (int i = 0; i < x; i++) {
			sm.startSimulation("test.dat",hd);
			sm.getSimulationThread().join();
			sm.getSimulationThread().getOperation();
			busesFloraInd+=sm.getSimulationThread().getOperation().getBusesFloraInd();
			busesSalomia+=sm.getSimulationThread().getOperation().getBusesSalomia();
			busesImpact+=sm.getSimulationThread().getOperation().getBusesImpact();
			busesRoad+=sm.getSimulationThread().getOperation().getBusesRoad();
			ewt+=sm.getSimulationThread().getOperation().getExcessWaitingTime();
			hcv+=sm.getSimulationThread().getOperation().getHeadwayCoefficientOfVariation();
			passengerSatisfaction+=sm.getSimulationThread().getOperation().getPassengerSatisfaction();
			usersFloraInd+=sm.getSimulationThread().getOperation().getUsersFloraInd();
			userSalomia+=sm.getSimulationThread().getOperation().getUsersSalomia();
		}
		
		double promBusesFloraInd= (busesFloraInd/x);
		double promBusesSalomia= (busesSalomia/x);
		
		double promBusesRoad= (busesRoad/x);
		
		double promUsersFloraInd= (usersFloraInd/x);
		double promUsersSalomia= (userSalomia/x);
		
		double promBusesImpact= (busesImpact/x);
		double promPassengerSatisfaction = (passengerSatisfaction/x);
		
		double promEwt= (ewt/x);
		double promHcv= (hcv/x);
		
		
	}
	

	
	
}
