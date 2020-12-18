package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.model.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.model.factorySITM.SITMLine;
import com.simulationFrameworkDT.model.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;

public interface IDataSourceRest {

	public String[] getFileNames();
	public long findPlanVersion();
	public ArrayList<SITMPlanVersion> findAllPlanVersions(String type) ;
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(String type, long planVersionID,long lineId);
	public String[] getHeaders(String projectName);
}
