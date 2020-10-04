package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	private StateController stateController;
	
	@GetMapping("/names")
	public String[] getProjectsNames() {
		return stateController.getProjectsNames();
	}
	
	@PostMapping("/save/oracle")
	public void saveProjectOracle(@RequestBody ProjectDTO project) {
		Project newProject = new Project();
		newProject.setProjectName(project.getName());
		newProject.setInitialDate(project.getInitialDate());
		newProject.setFinalDate(project.getFinalDate());
		newProject.setPlanVersionId(project.getPlanVersionId());
		stateController.saveProject(newProject);	
	}

	@PostMapping("/save")
	public void saveScv(@RequestBody ProjectDTO project) {
		Project newProject = new Project();
		newProject.setProjectName(project.getName());
		newProject.setInitialDate(project.getInitialDate());
		newProject.setFinalDate(project.getFinalDate());
		newProject.setPlanVersionId(project.getPlanVersionId());
		newProject.setLineId(project.getLineId());
		newProject.setFileType(project.getFileType());
		newProject.setFileSplit(project.getFileSplit());
		newProject.setFileName(project.getFileName());
		stateController.saveProject(newProject);	
	}

	@GetMapping("/load")
	public ProjectDTO loadProject(String projectName) {
		Project project = stateController.loadProject(projectName);
		ProjectDTO dto = new ProjectDTO();
		dto.setName(project.getProjectName());
		dto.setInitialDate(project.getInitialDate());
		dto.setFinalDate(project.getFinalDate());
		dto.setPlanVersionId(project.getPlanVersionId());
		dto.setLineId(project.getLineId());
		return dto;
	}

	@PutMapping("/setline")
	public ProjectDTO setLineId(String projectName, long lineId) {
		
		Project project = stateController.loadProject(projectName);
		project.setLineId(lineId);
		stateController.saveProject(project);
		
		ProjectDTO dto = new ProjectDTO();
		dto.setName(project.getProjectName());
		dto.setInitialDate(project.getInitialDate());
		dto.setFinalDate(project.getFinalDate());
		dto.setPlanVersionId(project.getPlanVersionId());
		dto.setLineId(project.getLineId());
		return dto;
	}
}
