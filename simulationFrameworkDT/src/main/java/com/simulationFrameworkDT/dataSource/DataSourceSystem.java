package com.simulationFrameworkDT.dataSource;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMOperationalTravels;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter @Setter
public class DataSourceSystem implements IDateSource, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public final static String FILE_CSV = "FileCSV";
	public final static String DATA_BASE = "DataBase";

	private String type;
	
	@Autowired
	public Source_db source_db;
	public Source_csv source_csv;

	public DataSourceSystem() {
		initializeCsv(new File("../datagrams.csv"), ",");	
	}
	
	public void initializeCsv(File sourceFile, String split) {
		this.type = FILE_CSV;
		source_csv = new Source_csv(sourceFile, split);
		setColumnNumberForSimulationVariables(0, 4, 5, 1, 7);
	}
	
	public void setColumnNumberForSimulationVariables(int clock, int longitude, int latitude, int busId, int lineId) {
		if(source_csv!=null) {
			source_csv.setColumnNumberForSimulationVariables(clock, longitude, latitude, busId, lineId);
		}
	}
	
	public void setHeaders(HashMap<String, Integer> headers) {
		if(source_csv!=null) {
			source_csv.setHeaders(headers);
		}
	}
	
	@Override 
	public String[] getHeaders() {

		switch (type) {
		case FILE_CSV:
			return source_csv.getHeaders();

		case DATA_BASE:
			return source_db.getHeaders();

		}
		return null;
	}
	
	public HashMap<String,String> getLastRow(){
		switch (type) {
		case FILE_CSV:
			return source_csv.getLastRow();

		case DATA_BASE:
			return source_db.getLastRow();
		}
		
		return null;
	}

	@Override
	public ArrayList<SITMPlanVersion> findAllPlanVersions() {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllPlanVersions();

		case DATA_BASE:
			return source_db.findAllPlanVersions();
		}
		
		return null;
	}

	@Override
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllCalendarsByPlanVersion(planVersionId);

		case DATA_BASE:
			return source_db.findAllCalendarsByPlanVersion(planVersionId);
		}
		
		return null;
	}

	@Override
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllLinesByPlanVersion(planVersionId);

		case DATA_BASE:
			return source_db.findAllLinesByPlanVersion(planVersionId);
		}
		
		return null;
	}

	@Override
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId, long lineId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllStopsByLine(planVersionId,lineId);

		case DATA_BASE:
			return source_db.findAllStopsByLine(planVersionId,lineId);
		}
		
		return null;
	}

	@Override
	public ArrayList<SITMOperationalTravels> findAllOperationalTravelsByRange(Date initialDate, Date lastDate, long lineId){
		
		switch (type) {
		case FILE_CSV:
			return source_csv.findAllOperationalTravelsByRange(initialDate, lastDate, lineId);

		case DATA_BASE:
			return source_db.findAllOperationalTravelsByRange(initialDate, lastDate, lineId);
		}
		
		return null;
	}
}
