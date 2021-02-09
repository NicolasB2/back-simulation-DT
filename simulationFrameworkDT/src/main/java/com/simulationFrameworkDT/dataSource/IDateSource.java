package com.simulationFrameworkDT.dataSource;

import java.sql.Date;
import java.util.ArrayList;

import com.simulationFrameworkDT.model.SITM.SITMCalendar;
import com.simulationFrameworkDT.model.SITM.SITMLine;
import com.simulationFrameworkDT.model.SITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.SITM.SITMStop;

public interface IDateSource {
	
	public ArrayList<SITMPlanVersion> findAllPlanVersions();
	public SITMPlanVersion findPlanVersionByDate(Date initialDate, Date lastDate);
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId,long lineId);
	
}
