package com.simulationFrameworkDT.dataSource;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.simulationFrameworkDT.model.SITM.SITMCalendar;
import com.simulationFrameworkDT.model.SITM.SITMLine;
import com.simulationFrameworkDT.model.SITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.SITM.SITMStop;
import com.simulationFrameworkDT.simulation.event.Event;
import com.simulationFrameworkDT.simulation.state.Project;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class DataSourceSystem {

	public final static String FILE_CSV = "FileCSV";
	public final static String DATA_BASE = "DataBase";

	public Source_db source_db;
	public Source_csv source_csv;
	
	public String[] getFileNames() {
		File f = new File("datagrams");
		String[] pathnames = f.list();
		return pathnames;
	}
	
	//================================================================================
    // Constructor and initialize
    //================================================================================
	
	public DataSourceSystem() {
		initializeCsv();
	}
	
	public void initializeCsv() {
		source_csv = new Source_csv();
	}
	
	//================================================================================
    // Simulation
    //================================================================================
	
	public String[] getHeaders(Project project) {

		switch (project.getFileType()) {
		case FILE_CSV:
			return source_csv.getHeaders(project.getFileName(),project.getFileSplit());

		case DATA_BASE:
			return source_db.getHeaders();

		}
		return null;
	}

	public HashMap<String, String> getLastRow(Project project) {
		switch (project.getFileType()) {
		case FILE_CSV:
			return source_csv.getLastRow(project.getFileName(),project.getFileSplit(),project.getNextDate());

		case DATA_BASE:
			return source_db.getLastRow(project.getNextDate());
		}

		return null;
	}
	
	public ArrayList<Event> findAllOperationalTravelsByRange(Project project) {

		switch (project.getFileType()) {
		case FILE_CSV:
			return source_csv.findAllOperationalTravelsByRange(project.getFileName(),project.getFileSplit(),project.getInitialDate(), project.getNextDate(), project.getLineId());
//			return source_csv.findAllOperationalTravelsByRange2(project.getFileName(),project.getFileSplit(),project.getInitialDate(), project.getNextDate(), project.getLineId());

		case DATA_BASE:
			return source_db.findAllOperationalTravelsByRange(project.getInitialDate(), project.getFinalDate(), project.getLineId());
		}

		return null;
	}
	
	//================================================================================
    // Queries
    //================================================================================
	
	public ArrayList<SITMPlanVersion> findAllPlanVersions(String type) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllPlanVersions();

		case DATA_BASE:
			return source_db.findAllPlanVersions();
		}

		return null;
	}

	public SITMPlanVersion findPlanVersionByDate(String type,Date initialDate, Date lastDate) {
		switch (type) {
		case FILE_CSV:
			return source_csv.findPlanVersionByDate(initialDate,lastDate);

		case DATA_BASE:
			return source_db.findPlanVersionByDate(initialDate,lastDate);
		}

		return null;
	}
	
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(String type, long planVersionId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllCalendarsByPlanVersion(planVersionId);

		case DATA_BASE:
			return source_db.findAllCalendarsByPlanVersion(planVersionId);
		}

		return null;
	}

	public ArrayList<SITMLine> findAllLinesByPlanVersion(String type, long planVersionId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllLinesByPlanVersion(planVersionId);

		case DATA_BASE:
			return source_db.findAllLinesByPlanVersion(planVersionId);
		}

		return null;
	}

	public ArrayList<SITMStop> findAllStopsByLine(String type, long planVersionId, long lineId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllStopsByLine(planVersionId, lineId);

		case DATA_BASE:
			return source_db.findAllStopsByLine(planVersionId, lineId);
		}

		return null;
	}
}
