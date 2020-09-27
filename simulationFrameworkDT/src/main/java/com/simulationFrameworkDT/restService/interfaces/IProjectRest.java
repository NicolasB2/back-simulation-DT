package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.restService.dataTransfer.ProjectDTO;

public interface IProjectRest {

	public String[] getProjectsNames();
	public void saveProjectOracle(ProjectDTO project);	
	public void saveScv(ProjectDTO project);
	public ProjectDTO loadProject(String name);
}
