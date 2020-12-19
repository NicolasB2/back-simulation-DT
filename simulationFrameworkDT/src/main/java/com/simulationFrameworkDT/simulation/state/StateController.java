package com.simulationFrameworkDT.simulation.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class StateController {

	public static final String FILE_EXTENSION = ".dat";
	public static final String PROJECTS_PATH = "projects";
	public Project project;
	
	//================================================================================
    // Project Controller
    //================================================================================
	
	public String[] getProjectsNames() {
		File f = new File(PROJECTS_PATH);
		String[] pathnames = f.list();
		return pathnames;
	}
	
	public void saveProject(Project project) {
		
		String pName = project.getProjectName();
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
	    	 this.project = project;
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
		    this.project = project;
		}catch (Exception e) {
		    e.printStackTrace();
		}
		
		return project;
	}
	
	//================================================================================
    // Animation Speed
    //================================================================================
	
	public void setFastSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setFastSpeed();
	}

	public void setNormalSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setNormalSpeed();
	}

	public void setSlowSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setSlowSpeed();
	}

	//================================================================================
    // Read Speed
    //================================================================================
	
	public void setOneToOneSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setOneToOneSpeed();
	}

	public void setOneToFiveSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setOneToFiveSpeed();
	}

	public void setOneToTenSpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setOneToTenSpeed();
	}

	public void setOneToThirtySpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setOneToThirtySpeed();
	}

	public void setOneToSixtySpeed(String Projectname) {
		Project project = loadProject(Projectname);
		project.getClock().setOneToSixtySpeed();
	}
	
}
