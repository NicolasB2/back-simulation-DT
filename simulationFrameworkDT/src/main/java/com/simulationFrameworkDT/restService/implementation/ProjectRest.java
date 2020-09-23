package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public void saveProjectOracle(Project project) {
		projectController.saveProject(project);
	}

	@PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
	public Project saveScv(@RequestBody Project project) {
		projectController.saveProject(project);
		return project;
	}

	@GetMapping("/load")
	public Project loadProject(String name) {
		return projectController.loadProject(name);
	}
 
	@GetMapping("/names")
	public String[] getProjectsNames() {
		return projectController.getProjectsNames();
	}

}
