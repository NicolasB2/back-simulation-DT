package com.simulationFrameworkDT.restService.implementation;

import java.io.File;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.project.Project;
import com.simulationFrameworkDT.project.ProjectController;
import com.simulationFrameworkDT.restService.interfaces.IProjectRest;

@RequestMapping("simulation/project")
@RestController
@CrossOrigin(origins = "*")
public class ProjectRest implements IProjectRest {

	@Autowired
	private ProjectController projectController;
	
	@Autowired
	private DataSourceSystem dataSource;
	
	@PostMapping("/save/oracle")
	public void saveProjectOracle(String name, Date initialDate, Date finalDate, long planVersionId) {
		Project project = new Project(name, initialDate, finalDate, planVersionId);
		projectController.saveProject(project);
	}

	@PostMapping("/save/csv")
	public void saveProjectScvOnServer(String name, Date initialDate, Date finalDate, long planVersionId, String fileName, String split) {
		Project project = new Project(name, initialDate, finalDate, planVersionId, fileName, split, 1);
		dataSource.initializeCsv(new File(fileName), ",");
		projectController.saveProject(project);
	}

	@PostMapping("/load/project")
	public Project loadProject(String name) {
		return projectController.loadProject(name);
	}

	@GetMapping("/names")
	public String[] getProjectsNames() {
		return projectController.getProjectsNames();
	}

}
