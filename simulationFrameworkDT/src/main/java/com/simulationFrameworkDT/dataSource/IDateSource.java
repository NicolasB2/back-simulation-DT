package com.simulationFrameworkDT.dataSource;

import java.util.ArrayList;

import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

public interface IDateSource {

//	public String[] getHeaders();
//	public HashMap<String,String> getLastRow();
//	public ArrayList<SITMOperationalTravels> findAllOperationalTravelsByRange(Date initialDate, Date lastDate, long lineId);
	public ArrayList<SITMPlanVersion> findAllPlanVersions() ;
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId,long lineId);
	
}
