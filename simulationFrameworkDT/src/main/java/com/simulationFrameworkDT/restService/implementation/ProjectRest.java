package com.simulationFrameworkDT.restService.implementation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
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
	
	@Autowired
	private DataSourceSystem dataSource;
	
	@GetMapping("/names")
	public String[] getProjectsNames() {
		return stateController.getProjectsNames();
	}
	
	@PostMapping("/save/oracle")
	public long saveProjectOracle(@RequestBody ProjectDTO project) {
		Project newProject = new Project();
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date initdate = new Date(dateFormat.parse(project.getInitialDate()).getTime());
			Date finaldate = new Date(dateFormat.parse(project.getFinalDate()).getTime());
			newProject.setInitialDate(initdate);
			newProject.setFinalDate(finaldate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newProject.setProjectName(project.getName());
		newProject.setPlanVersionId(project.getPlanVersionId());	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date initdate;
		Date finaldate;
		long planVersionId = 0;
		
		try {
			initdate = new Date(dateFormat.parse(project.getInitialDate()).getTime());
			finaldate = new Date(dateFormat.parse(project.getFinalDate()).getTime());
			planVersionId = dataSource.findPlanVersionByDate(project.getFileType(), initdate, finaldate).getPlanVersionId();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newProject.setPlanVersionId(planVersionId);
		stateController.saveProject(newProject);
		return planVersionId;
	}

	@PostMapping("/save")
	public long saveScv(@RequestBody ProjectDTO project) {
		Project newProject = new Project();
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date initdate = new Date(dateFormat.parse(project.getInitialDate()).getTime());
			Date finaldate = new Date(dateFormat.parse(project.getFinalDate()).getTime());
			newProject.setInitialDate(initdate);
			newProject.setFinalDate(finaldate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newProject.setProjectName(project.getName());
		newProject.setPlanVersionId(project.getPlanVersionId());
		newProject.setLineId(project.getLineId());
		newProject.setFileType(project.getFileType());
		newProject.setFileSplit(project.getFileSplit());
		newProject.setFileName(project.getFileName());
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date initdate;
		Date finaldate;
		long planVersionId = 0;
		
		try {
			initdate = new Date(dateFormat.parse(project.getInitialDate()).getTime());
			finaldate = new Date(dateFormat.parse(project.getFinalDate()).getTime());
			planVersionId = dataSource.findPlanVersionByDate(project.getFileType(), initdate, finaldate).getPlanVersionId();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newProject.setPlanVersionId(planVersionId);
		stateController.saveProject(newProject);
		return planVersionId;
	}

	@SuppressWarnings("deprecation")
	@GetMapping("/load")
	public ProjectDTO loadProject(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
		ProjectDTO dto = new ProjectDTO();
		
		String initdate = project.getInitialDate().toGMTString();
		String finaldate = project.getFinalDate().toGMTString();
		
		dto.setInitialDate(initdate);
		dto.setFinalDate(finaldate);
		dto.setName(project.getProjectName());
		dto.setLineId(project.getLineId());
		dto.setFileType(project.getFileType());
		dto.setFileSplit(project.getFileSplit());
		dto.setFileName(project.getFileName());
		return dto;
	}

	@SuppressWarnings("deprecation")
	@PutMapping("/setline/{id}/{lineId}")
	public ProjectDTO setLineId(@PathVariable("id") String projectName,@PathVariable("lineId") long lineId) {
		
		Project project = stateController.getProject();
		project.setLineId(lineId);
		ProjectDTO dto = new ProjectDTO();
		
		String initdate = project.getInitialDate().toGMTString();
		String finaldate = project.getFinalDate().toGMTString();
		
		dto.setName(project.getProjectName());
		dto.setInitialDate(initdate);
		dto.setFinalDate(finaldate);
		dto.setLineId(project.getLineId());
		dto.setFileType(project.getFileType());
		dto.setFileSplit(project.getFileSplit());
		dto.setFileName(project.getFileName());
		dto.setPlanVersionId(project.getPlanVersionId());
		return dto;
	}
}
