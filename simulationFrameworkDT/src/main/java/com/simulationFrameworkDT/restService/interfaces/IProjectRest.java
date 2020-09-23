package com.simulationFrameworkDT.restService.interfaces;

import com.simulationFrameworkDT.simulation.state.Project;

public interface IProjectRest {

	public void saveProjectOracle(Project project);
	
	public Project saveScv(Project project);
	
	//public void saveProjectScvByFile(String name, Date initialDate, Date finalDate, long planVersionId);
	
	public Project loadProject(String name);
	
	public String[] getProjectsNames();
}
