package com.simulationFrameworkDT.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ProjectController {

	public static final String FILE_EXTENSION = ".dat";
	public static final String PROJECTS_PATH = "projects";
	
	public void saveProject(Project project) {
		String pName = project.getName();
		
		File file = new File(PROJECTS_PATH+File.separator+pName);
		
		if (!file.exists()) {
			file.mkdirs();
        }
		
		file = new File(PROJECTS_PATH+File.separator+pName+File.separator+pName+FILE_EXTENSION);
		
    	ObjectOutputStream save;
    	
		try {
			
			save = new ObjectOutputStream(new FileOutputStream(file) );
			save.writeObject(project);
	    	save.close();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Project loadProject(String name) {
		return null;
	}
	
	public String[] getProjectsNames() {
		File f = new File(PROJECTS_PATH);
		String[] pathnames = f.list();
		return pathnames;
	}
}
