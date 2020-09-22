package com.simulationFrameworkDT.restService.implementation;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.restService.interfaces.IProjectRest;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

@RequestMapping("simulation/project")
@RestController
@CrossOrigin(origins = "*")
public class ProjectRest implements IProjectRest {

	@Autowired
	private StateController projectController;
	
	@PostMapping("/save/oracle")
	public void saveProjectOracle(String name, Date initialDate, Date finalDate, long planVersionId) {
		Project project = new Project();
		project.setName(name);
		project.setInitialDate(initialDate);
		project.setFinalDate(finalDate);
		project.setPlanVersionId(planVersionId);
		project.setFileType(DataSourceSystem.DATA_BASE);
		projectController.saveProject(project);
	}

	@PostMapping("/save/csv")
	public void saveProjectScvOnServer(String name, Date initialDate, Date finalDate, long planVersionId, String fileName) {
		Project project = new Project();
		project.setName(name);
		project.setInitialDate(initialDate);
		project.setFinalDate(finalDate);
		project.setPlanVersionId(planVersionId);
		project.setFileName(fileName);
		project.setFileSplit(",");
		project.setFileType(DataSourceSystem.FILE_CSV);
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
