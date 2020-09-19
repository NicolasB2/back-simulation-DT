package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

public interface IDataSourceRest {

	public String[] getFileNames();
	public ArrayList<SITMPlanVersion> findAllPlanVersions(String type) ;
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(String type, long planVersionID,long lineId);
}
