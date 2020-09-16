package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

public interface IDataSourceRest {

	public String[] getFileNames();
	public void setSourceType(String type);
	public ArrayList<SITMPlanVersion> findAllPlanVersions() ;
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionID,long lineId);
}
