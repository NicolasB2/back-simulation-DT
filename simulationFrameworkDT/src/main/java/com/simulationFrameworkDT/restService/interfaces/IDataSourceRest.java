package com.simulationFrameworkDT.restService.interfaces;

import java.util.ArrayList;

import com.simulationFrameworkDT.model.SITM.SITMCalendar;
import com.simulationFrameworkDT.model.SITM.SITMLine;
import com.simulationFrameworkDT.model.SITM.SITMPlanVersion;
import com.simulationFrameworkDT.model.SITM.SITMStop;
import com.simulationFrameworkDT.restService.dataTransfer.ProjectDTO;

public interface IDataSourceRest {

	public String[] getFileNames();
	public String[] getHeaders(String projectName);
	public long findPlanVersion(ProjectDTO project);
	public ArrayList<SITMPlanVersion> findAllPlanVersions(String type);
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMLine> findAllLinesByPlanVersion(String type, long planVersionId);
	public ArrayList<SITMStop> findAllStopsByLine(String type, long planVersionID,long lineId);
}
