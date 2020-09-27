package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.dataTransfer.ProjectDTO;
import com.simulationFrameworkDT.restService.interfaces.IProjectRest;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

@RequestMapping("simulation/project")
@RestController
@CrossOrigin(origins = "*")
public class ProjectRest implements IProjectRest {

	@Autowired
	private StateController projectController;
	
	@GetMapping("/names")
	public String[] getProjectsNames() {
		return projectController.getProjectsNames();
	}
	
	@PostMapping("/save/oracle")
	public void saveProjectOracle(ProjectDTO project) {
		Project newProject = new Project();
		newProject.setName(project.getName());
		newProject.setInitialDate(project.getInitialDate());
		newProject.setFinalDate(project.getFinalDate());
		newProject.setPlanVersionId(project.getPlanVersionId());
		projectController.saveProject(newProject);	
	}

	@PostMapping("/save")
	public void saveScv(@RequestBody ProjectDTO project) {
		Project newProject = new Project();
		newProject.setName(project.getName());
		newProject.setInitialDate(project.getInitialDate());
		newProject.setFinalDate(project.getFinalDate());
		newProject.setPlanVersionId(project.getPlanVersionId());
		newProject.setLineId(project.getLineId());
		projectController.saveProject(newProject);	
	}

	@GetMapping("/load")
	public ProjectDTO loadProject(String name) {
		Project project = projectController.loadProject(name);
		ProjectDTO dto = new ProjectDTO();
		dto.setName(project.getName());
		dto.setInitialDate(project.getInitialDate());
		dto.setFinalDate(project.getFinalDate());
		dto.setPlanVersionId(project.getPlanVersionId());
		dto.setLineId(project.getLineId());
		return dto;
	}

}
