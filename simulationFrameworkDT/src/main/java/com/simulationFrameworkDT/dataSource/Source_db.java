package com.simulationFrameworkDT.dataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.simulationFrameworkDT.dataSource.persistence.CalendarRepository;
import com.simulationFrameworkDT.dataSource.persistence.LineRepository;
import com.simulationFrameworkDT.dataSource.persistence.OperationalTravelsRepository;
import com.simulationFrameworkDT.dataSource.persistence.PlanVersionRepository;
import com.simulationFrameworkDT.dataSource.persistence.StopRepository;
import com.simulationFrameworkDT.model.events.DatagramEvent;
import com.simulationFrameworkDT.model.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.model.factorySITM.SITMLine;
import com.simulationFrameworkDT.model.factorySITM.SITMOperationalTravels;
import com.simulationFrameworkDT.model.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.event.EventType;

public class Source_db implements IDateSource {

	private static final String dbURL = "jdbc:oracle:thin:@192.168.161.43:1521:liason";
	private static final String USER_NAME = "metrocali";
	private static final String USER_PASSWORD = "metrocali";

	@Autowired
	private PlanVersionRepository planVersionRepository;

	@Autowired
	private StopRepository stopRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private OperationalTravelsRepository operationalTravelsRepository;

	//================================================================================
    // Constructor and initialize
    //================================================================================
	
	public Source_db() {
		super();
	}

	public boolean jdbTestConnection() {
		try {
			Connection connection = DriverManager.getConnection(dbURL, USER_NAME, USER_PASSWORD);
			System.out.println("Connection Succesfull !!");
			connection.close();
			return true;
		} catch (Exception e) {
			System.out.println("Error connection DB!");
			e.printStackTrace();
			return false;
		}
	}

	//================================================================================
    // Simulation
    //================================================================================
	
	public String[] getHeaders() {

		String[] headers = new String[10];
		headers[0] = "opertravelId";
		headers[1] = "busId";
		headers[2] = "laststopId";
		headers[3] = "GPS_X";
		headers[4] = "GPS_Y";
		headers[5] = "odometervalue";
		headers[6] = "lineId";
		headers[7] = "taskId";
		headers[8] = "tripId";
		headers[9] = "eventDate";

		return headers;
	}
	
	public HashMap<String,String> getLastRow(Date currentDate){
		return null;
	}
	
	public ArrayList<Event> findAllOperationalTravelsByRange(Date initialDate, Date lastDate, long lineId){
		
		ArrayList<Event> returnAnswer = new ArrayList<Event>();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		
		if(initialDate.toString().equals(lastDate.toString())) {
			
			String date = dateFormat.format(initialDate);

			try {
				
				long startHour = (hourFormat.parse(hourFormat.format(initialDate)).getTime()-18000000)/1000;
				System.out.println(startHour);
				
				long endHour = (hourFormat.parse(hourFormat.format(lastDate)).getTime()-18000000)/1000;
				System.out.println(endHour);
				
				System.out.println(date);
			
				for (SITMOperationalTravels element : operationalTravelsRepository.findAllOPDay(date, startHour, endHour)) {
					
					String datagramDate = element.getEventDate().toString();
					long datagramDateTime = element.getEventTime();
					long busId = element.getBusId();
					long stopId = element.getNearestStopId();
					double longitude =  Double.parseDouble(element.getGPS_X());
					double latitude =  Double.parseDouble(element.getGPS_Y());

					DatagramEvent datagram = new DatagramEvent(datagramDateTime, datagramDate, busId, stopId, 0, longitude, latitude, 0,lineId, 0);
					datagram.setType(EventType.POSICIONAMIENTO_GPS);
					returnAnswer.add(datagram);
				}	

			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
		}else {
			for (SITMOperationalTravels element : operationalTravelsRepository.findAllOP("01/08/19","01/08/19")) {
				
				String datagramDate = element.getEventDate().toString();
				long datagramDateTime = element.getEventTime();
				long busId = element.getBusId();
				long stopId = element.getNearestStopId();
				double longitude =  Double.parseDouble(element.getGPS_X());
				double latitude =  Double.parseDouble(element.getGPS_Y());

				DatagramEvent datagram = new DatagramEvent(datagramDateTime, datagramDate, busId, stopId, 0, longitude, latitude, 0,lineId, 0);
				returnAnswer.add(datagram);
			}
		}
		
		return returnAnswer;
	}
	
	//================================================================================
    // Queries
    //================================================================================
	
	@Override
	public ArrayList<SITMPlanVersion> findAllPlanVersions() {

		ArrayList<SITMPlanVersion> returnAnswer = new ArrayList<SITMPlanVersion>();

		for (SITMPlanVersion element : planVersionRepository.findAll()) {
			returnAnswer.add(element);	
		}

		return returnAnswer;
	}

	public SITMPlanVersion findPlanVersionByDate(Date initialDate, Date lastDate){
		return null;
	}
	
	@Override
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId) {

		ArrayList<SITMCalendar> returnAnswer = new ArrayList<SITMCalendar>();

		for (SITMCalendar element : calendarRepository.findAllCalendarsbyPlanVersionId(planVersionId)) {
			returnAnswer.add(element);
		}

		return returnAnswer;
	}

	@Override
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId) {

		ArrayList<SITMLine> returnAnswer = new ArrayList<SITMLine>();

		for (SITMLine element : lineRepository.findAllLinesbyPlanVersionId(planVersionId)) {
			returnAnswer.add(element);
		}

		return returnAnswer;
	}

	@Override
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId, long lineId) {

		ArrayList<SITMStop> returnAnswer = new ArrayList<SITMStop>();

		for (SITMStop element : stopRepository.findAllStopsbyLineId(planVersionId, lineId)) {
			returnAnswer.add(element);
		}

		return returnAnswer;
	}

}
