package com.simulationFrameworkDT.restService.interfaces;

import java.sql.Date;

import com.simulationFrameworkDT.project.Project;

public interface IProjectRest {

	public void saveProjectOracle(String name, Date initialDate, Date finalDate, long planVersionId);
	
	public void saveProjectScvOnServer(String name, Date initialDate, Date finalDate, long planVersionId, String fileName);
	
	//public void saveProjectScvByFile(String name, Date initialDate, Date finalDate, long planVersionId);
	
	public Project loadProject(String name);
	
	public String[] getProjectsNames();
}
