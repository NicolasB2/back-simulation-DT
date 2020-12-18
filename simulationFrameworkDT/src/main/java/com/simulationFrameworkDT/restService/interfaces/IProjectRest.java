package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.restService.dataTransfer.ProjectDTO;

public interface IProjectRest {

	public String[] getProjectsNames();
	public long saveProjectOracle(ProjectDTO project);	
	public long saveScv(ProjectDTO project);
	public ProjectDTO loadProject(String projectName);
	public ProjectDTO setLineId(String projectName, long lineId);
	
}
