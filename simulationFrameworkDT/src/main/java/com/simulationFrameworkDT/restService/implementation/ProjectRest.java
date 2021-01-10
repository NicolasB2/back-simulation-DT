package com.simulationFrameworkDT.restService.implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
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
			planVersionId = dataSource.findPlanVersionByDate(project.getFileType(), initdate, finaldate)
					.getPlanVersionId();

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
			planVersionId = dataSource.findPlanVersionByDate(project.getFileType(), initdate, finaldate)
					.getPlanVersionId();

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
		Project project = stateController.loadProject(projectName + ".dat");
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
	public ProjectDTO setLineId(@PathVariable("id") String projectName, @PathVariable("lineId") long lineId) {

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

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile[] file) {
		if (null == file[0].getOriginalFilename()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {
			byte[] bytes = file[0].getBytes();
			Path path = Paths.get(file[0].getOriginalFilename());
			Files.write(path, bytes);
			System.out.println(path.getFileName());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<>("Good Job", HttpStatus.OK);
	}
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("id") String projectName, HttpServletRequest request) {
		String fileName = projectName;
		Resource resource = null;
		if (fileName != null && !fileName.isEmpty()) {
			try {
				Path filePath = Paths.get("projects"+File.separator+fileName+".dat").normalize();
		        resource = new UrlResource(filePath.toUri());
		        if(!resource.exists()) {
		        	throw new FileNotFoundException("File not found " + fileName);
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if (contentType == null) {
				contentType = "application/octet-stream";
			}
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
