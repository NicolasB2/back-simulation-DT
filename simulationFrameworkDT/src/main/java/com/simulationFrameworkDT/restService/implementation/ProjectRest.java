package com.simulationFrameworkDT.restService.implementation;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

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
	
	@PostMapping("/saveOracle")
	public void saveProjectOracle(String name, Date initialDate, Date finalDate, long planVersionId) {
		Project project = new Project(name, initialDate, finalDate, planVersionId);
		dataSource.setType(DataSourceSystem.FILE_CSV);
		project.setDataSource(dataSource);
		projectController.saveProject(project);
	}

	@PostMapping("/saveCsv")
	public void saveProjectScvOnServer(String fileName, String name, Date initialDate, Date finalDate, long planVersionId) {
		Project project = new Project(name, initialDate, finalDate, planVersionId);
		dataSource.setType(DataSourceSystem.FILE_CSV);
		dataSource.initializeCsv(new File(fileName), ",");
		project.setDataSource(dataSource);
		projectController.saveProject(project);
	}

	@PostMapping("/loadProject")
	public Project loadProject(String name) {
		return projectController.loadProject(name);
	}

	@GetMapping("/names")
	public ArrayList<String> getProjectsNames() {
		return projectController.getProjectsNames();
	}

}
