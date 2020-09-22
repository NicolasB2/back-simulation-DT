package com.simulationFrameworkDT.dataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import com.simulationFrameworkDT.model.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.model.factorySITM.SITMLine;
import com.simulationFrameworkDT.model.factorySITM.SITMLineStop;
import com.simulationFrameworkDT.model.factorySITM.SITMOperationalTravels;
import com.simulationFrameworkDT.model.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source_csv implements IDateSource {
	
	public final static String DATAGRAMS_PATH = "datagrams";
	private HashMap<String, Integer> systemDirectory;
	
	public void setColumnNumberForSimulationVariables(int clock, int longitude, int latitude, int busId, int lineId) {
		systemDirectory = new HashMap<String, Integer>();
		systemDirectory.put("clock", clock);
		systemDirectory.put("busId", busId);
		systemDirectory.put("lineId", lineId);
		systemDirectory.put("longitude", longitude);
		systemDirectory.put("latitude", latitude);
	}
	
	public File getSourceFile(String file) {
		return new File(DATAGRAMS_PATH+File.separator+file);
	}
	
	public String[] getHeaders(String file, String split) {
		
		BufferedReader br;
		String[] headers = null;
		File sourceFile = getSourceFile(file);
		
		try {

			
			br = new BufferedReader(new FileReader(sourceFile));
			String text = br.readLine();
			headers = text.split(split);
			br.close();
			System.out.println(text);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return headers;
	}

	public HashMap<String,String> getLastRow(String file, String split, Date currentDate){
		
		File sourceFile = getSourceFile(file);
		BufferedReader br;
		String text = "";
		
		try {
			
			br = new BufferedReader(new FileReader(sourceFile));
			text = br.readLine();
			text = br.readLine();
			
			String[] data = text.split(split);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Long date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
			
			while(date<currentDate.getTime()){
				text = br.readLine();
				data = text.split(split);
				date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] data = text.split(split);
		String[] headers = getHeaders(file,split);
		HashMap<String, String> variables = new HashMap<>();
		
		for (int i = 0; i < data.length; i++) {
			variables.put(headers[i], data[i]);
		}
		return variables;
	}
	
	public ArrayList<SITMOperationalTravels> findAllOperationalTravelsByRange(String file, String split, Date initialDate, Date lastDate, long lineId){
		
		ArrayList<SITMOperationalTravels> operationaTravels = new ArrayList<SITMOperationalTravels>();

		File sourceFile = getSourceFile(file);
		
		try {

			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			String text = br.readLine(); 
			text = br.readLine();
			String[] data = text.split(split);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Long date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
			
			while(initialDate.getTime()<=date) {
				text = br.readLine();
				data = text.split(split);
				date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
			}
				
			if (text != null && !text.equals("")) {
				
				while(initialDate.getTime()>date) {
					text = br.readLine();
					
					if(text!=null && text!="") {
						data = text.split(split);
						date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
					}else {
						break;
					}
				}
				
				while (initialDate.getTime()<= date && date <=lastDate.getTime()) {
					
					Long opertravelId = System.currentTimeMillis();
					Long busId = Long.parseLong(data[systemDirectory.get("busId")]);
					double longitudeLng = Long.parseLong(data[systemDirectory.get("longitude")]);
					double latitudeLng = Long.parseLong(data[systemDirectory.get("latitude")]);
					String longitude = longitudeLng / 10000000 + "";
					String latitude = latitudeLng  / 10000000+ "";
					
					Date eventDate = new Date(date);

					if(data[7].equals(lineId+"") && longitudeLng!=-1 && latitudeLng!=-1) {
						SITMOperationalTravels op = new SITMOperationalTravels(opertravelId, busId, lineId, longitude, latitude, eventDate);
						operationaTravels.add(op);
					}
					
					text = br.readLine();
					if(text!=null && text!="") {
						data = text.split(split);
						date = dateFormat.parse(data[systemDirectory.get("clock")]).getTime();
					}else {
						break;
					}
					
				}		
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return operationaTravels;
	}

	@Override
	public ArrayList<SITMPlanVersion> findAllPlanVersions() {

		String path = new File("dataCSV/planversions.csv").getAbsolutePath();
		ArrayList<SITMPlanVersion> plans = new ArrayList<>();
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(path));

			String[] columns = null;
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				columns = line.split(";");

				if (!columns[0].isEmpty()) {

					long planVersionId = Long.parseLong(columns[0]);
					DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
					Date activationDate = new Date(dateFormat.parse(columns[1]).getTime());
					Date creationDate = new Date(dateFormat.parse(columns[2]).getTime());

					SITMPlanVersion plan = new SITMPlanVersion(planVersionId, activationDate, creationDate);
					plans.add(plan);
				}
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plans;
	}

	@Override
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId) {

		String path = new File("dataCSV/calendar.csv").getAbsolutePath();
		ArrayList<SITMCalendar> calendars = new ArrayList<>();

		BufferedReader br;

		try {

			br = new BufferedReader(new FileReader(path));
			String[] columns = null;
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				columns = line.split(";");

				if (!columns[0].isEmpty() && columns[3].equals(planVersionId + "")) {

					long calendarId = Long.parseLong(columns[0]);
					DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
					Date operationDate = new Date(dateFormat.parse(columns[1]).getTime());
					long scheduleTypeId = Long.parseLong(columns[2]);
					calendars.add(new SITMCalendar(calendarId, operationDate, scheduleTypeId, planVersionId));
				}

				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		Collections.sort(calendars,new Comparator<SITMCalendar>() {
			public int compare(SITMCalendar o1, SITMCalendar o2) {
		         return o1.getOperationDay().compareTo(o2.getOperationDay());
		     }
		});
		
		return calendars;
	}

	@Override
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId) {

		String path = new File("dataCSV/lines.csv").getAbsolutePath();
		ArrayList<SITMLine> lines = new ArrayList<>();

		BufferedReader br;

		try {

			br = new BufferedReader(new FileReader(path));
			String[] columns = null;
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				columns = line.split(";");

				if (!columns[0].isEmpty() && columns[1].equals(planVersionId + "")) {

					long lineid = Long.parseLong(columns[0]);
					String shortName = columns[2];
					String description = columns[3];

					lines.add(new SITMLine(lineid, shortName, description, planVersionId));

				}

				line = br.readLine();
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines;
	}

	public ArrayList<SITMStop> findAllStopsByPlanVersion(long planVersionId) {

		ArrayList<SITMStop> stops = new ArrayList<>();
		String path = new File("dataCSV/stops.csv").getAbsolutePath();

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(path));
			String[] columns = null;
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				columns = line.split(";");

				if (!columns[0].isEmpty() && columns[1].equals(planVersionId + "")
						&& !columns[6].contains("#") && !columns[6].equals("0")
						&& !columns[7].contains("#") && !columns[7].equals("0")) {

					String longName = columns[3];
					String shortName = columns[2];
					long stopId = Long.parseLong(columns[0]);

					double gPSX = 0;
					double gPSY = 0;
					double decimalLongitude = 0;
					double decimalLactitude = 0;

					if (!columns[4].isEmpty()) {
						gPSX = Double.parseDouble(columns[4]) / 10000000;
					}
					if (!columns[5].isEmpty()) {
						gPSY = Double.parseDouble(columns[5]) / 10000000;
					}
					if (!columns[6].isEmpty()) {
						String origi = columns[6].replace(".", "");
						StringBuffer str = new StringBuffer(origi);
						str.insert(3, ".");
						decimalLongitude = Double.parseDouble(str.toString());
					}
					if (!columns[7].isEmpty()) {
						String origi = columns[7].replace(".", "");
						StringBuffer str = new StringBuffer(origi);
						str.insert(1, ".");
						decimalLactitude = Double.parseDouble(str.toString());
					}

					stops.add(new SITMStop(stopId, shortName, longName, gPSX, gPSY, decimalLongitude,decimalLactitude, planVersionId));
				}

				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stops;
	}

	public ArrayList<SITMLineStop> findAllLineStopByPlanVersion(long planVersionId) {

		String path = new File("dataCSV/linestops.csv").getAbsolutePath();
		ArrayList<SITMLineStop> lineStops = new ArrayList<>();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String[] columns = null;
			String line = br.readLine();
			line = br.readLine();

			while (line != null) {
				columns = line.split(";");

				if (!columns[0].isEmpty() && columns[5].equals(planVersionId + "")) {
					
					long lineStopid = Long.parseLong(columns[0]);
					long stopsequence = Long.parseLong(columns[1]);
					long orientation = Long.parseLong(columns[2]);
					long lineid = Long.parseLong(columns[3]);
					long stopid = Long.parseLong(columns[4]);
					long planVersionid = Long.parseLong(columns[5]);
					long lineVariant = Long.parseLong(columns[6]);
					DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
					Date registerDate = new Date(dateFormat.parse(columns[7]).getTime());
					long lineVariantType = Long.parseLong(columns[8]);
					
					lineStops.add(new SITMLineStop(lineStopid, stopsequence, orientation, lineid, stopid,planVersionid, lineVariant, registerDate, lineVariantType));
				}
				
				line = br.readLine();
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineStops;
	}

	@Override
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId, long lineId) {

		ArrayList<SITMStop> stopsByLine = new ArrayList<>();
		ArrayList<SITMStop> stops = findAllStopsByPlanVersion(planVersionId);
		ArrayList<SITMLineStop> lineStops = findAllLineStopByPlanVersion(planVersionId);

		for (int i = 0; i < lineStops.size(); i++) {
			SITMLineStop lineStop = (SITMLineStop) lineStops.get(i);

			if (lineStop.getLineId() == lineId) {

				for (int j = 0; j < stops.size(); j++) {
					SITMStop stop = (SITMStop) stops.get(j);
					if (stop.getStopId() == lineStop.getStopId()) {
						stopsByLine.add(stop);
					}
				}
			}
		}

		return stopsByLine;
	}
	
}
