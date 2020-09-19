package com.simulationFrameworkDT.dataSource;

import java.io.File;
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
@Getter
@Setter
public class DataSourceSystem {

	public final static String FILE_CSV = "FileCSV";
	public final static String DATA_BASE = "DataBase";

	@Autowired
	public Source_db source_db;
	public Source_csv source_csv;

	public DataSourceSystem() {
		initializeCsv(new File("../datagrams.csv"), ",");
	}

	public String[] getFileNames() {
		File f = new File("datagrams");
		String[] pathnames = f.list();
		return pathnames;
	}

	public void initializeCsv(File sourceFile, String split) {
		source_csv = new Source_csv(sourceFile, split);
		setColumnNumberForSimulationVariables(0, 4, 5, 1, 7);
	}

	public void setColumnNumberForSimulationVariables(int clock, int longitude, int latitude, int busId, int lineId) {
		if (source_csv != null) {
			source_csv.setColumnNumberForSimulationVariables(clock, longitude, latitude, busId, lineId);
		}
	}

	public void setHeaders(HashMap<String, Integer> headers) {
		if (source_csv != null) {
			source_csv.setHeaders(headers);
		}
	}

	public String[] getHeaders(String type) {

		switch (type) {
		case FILE_CSV:
			return source_csv.getHeaders();

		case DATA_BASE:
			return source_db.getHeaders();

		}
		return null;
	}

	public HashMap<String, String> getLastRow(String type) {
		switch (type) {
		case FILE_CSV:
			return source_csv.getLastRow();

		case DATA_BASE:
			return source_db.getLastRow();
		}

		return null;
	}

	public ArrayList<SITMPlanVersion> findAllPlanVersions(String type) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllPlanVersions();

		case DATA_BASE:
			return source_db.findAllPlanVersions();
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

	public ArrayList<SITMOperationalTravels> findAllOperationalTravelsByRange(String type, Date initialDate, Date lastDate, long lineId) {

		switch (type) {
		case FILE_CSV:
			return source_csv.findAllOperationalTravelsByRange(initialDate, lastDate, lineId);

		case DATA_BASE:
			return source_db.findAllOperationalTravelsByRange(initialDate, lastDate, lineId);
		}

		return null;
	}
}
