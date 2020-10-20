package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.IClockRest;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.simulation.state.Clock;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

@RequestMapping("simulation/clock")
@RestController
@CrossOrigin(origins = "*")
public class ClockRest implements IClockRest{

	@Autowired
	private StateController stateController;
	
	@Autowired
	private SimController simController;
	
	@GetMapping("/load")
	@ResponseStatus(HttpStatus.OK)
	public Clock getClock(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
		return project.getClock();
	}
	
	@PutMapping("/setfast")
	@ResponseStatus(HttpStatus.OK)
	public Clock setFastSpeed(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
		Clock clock = project.getClock();
		clock.setFastSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/setnormal")
	@ResponseStatus(HttpStatus.OK)
	public Clock setNormalSpeed(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
		Clock clock = project.getClock();
		clock.setNormalSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/setslow")
	@ResponseStatus(HttpStatus.OK)
	public Clock setSlowSpeed(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
		Clock clock = project.getClock();
		clock.setSlowSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to1/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToOneSpeed(@PathVariable("id") String projectName) {
		Clock clock = null;
		try {
			simController.pause();
			Thread.sleep(500);
			Project project = stateController.loadProject(projectName+".dat");
			clock = project.getClock();
			clock.setOneToOneSpeed();
			stateController.saveProject(project);
			Thread.sleep(500);
			simController.resume();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clock;
	}

	@PutMapping("/set1to5/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToFiveSpeed(@PathVariable("id") String projectName) {
		Clock clock = null;
		try {
			simController.pause();
			Thread.sleep(500);
			Project project = stateController.loadProject(projectName+".dat");
			clock = project.getClock();
			clock.setOneToFiveSpeed();
			stateController.saveProject(project);
			Thread.sleep(500);
			simController.resume();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clock;
		
	}

	@PutMapping("/set1to10/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToTenSpeed(@PathVariable("id") String projectName) {
		Clock clock = null;
		try {
			simController.pause();
			Thread.sleep(500);
			Project project = stateController.loadProject(projectName+".dat");
			clock = project.getClock();
			clock.setOneToTenSpeed();
			stateController.saveProject(project);
			Thread.sleep(500);
			simController.resume();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clock;
		
		
	}

	@PutMapping("/set1to30/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToThirtySpeed(@PathVariable("id") String projectName) {
		Clock clock = null;
		try {
			simController.pause();
			Thread.sleep(500);
			Project project = stateController.loadProject(projectName+".dat");
			clock = project.getClock();
			clock.setOneToThirtySpeed();
			stateController.saveProject(project);
			Thread.sleep(500);
			simController.resume();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clock;
	}

	@PutMapping("/set1to60/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToSixtySpeed(@PathVariable("id") String projectName) {
		Clock clock = null;
		try {
			simController.pause();
			Thread.sleep(500);
			Project project = stateController.loadProject(projectName+".dat");
			clock = project.getClock();
			clock.setOneToSixtySpeed();
			stateController.saveProject(project);
			Thread.sleep(500);
			simController.resume();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clock;
	}
}
