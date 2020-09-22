package com.simulationFrameworkDT.simulation.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Service;

@Service
public class StateController {

	public static final String FILE_EXTENSION = ".dat";
	public static final String PROJECTS_PATH = "projects";
	
	public void saveProject(Project project) {
		String pName = project.getName();
		
		File file = new File(PROJECTS_PATH);
		
		if (!file.exists()) {
			file.mkdirs();
        }
		
		file = new File(PROJECTS_PATH+File.separator+pName+FILE_EXTENSION);
		
    	ObjectOutputStream objectOutputStream;
    	
		try {
			
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(project);
	    	objectOutputStream.close();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Project loadProject(String name) {
		
		ObjectInputStream objectinputstream = null;
		Project project = null;
		try {

			objectinputstream = new ObjectInputStream(new FileInputStream(PROJECTS_PATH+File.separator+name));
		    project = (Project) objectinputstream.readObject();
		    objectinputstream.close();
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return project;
	}
	
	public String[] getProjectsNames() {
		File f = new File(PROJECTS_PATH);
		String[] pathnames = f.list();
		return pathnames;
	}
}
