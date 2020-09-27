package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.IClockRest;
import com.simulationFrameworkDT.simulation.state.Clock;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

@RequestMapping("simulation/clock")
@RestController
@CrossOrigin(origins = "*")
public class ClockRest implements IClockRest{

	@Autowired
	private StateController stateController;
	
	@GetMapping("/load")
	@ResponseStatus(HttpStatus.OK)
	public Clock getClock(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		return project.getClock();
	}
	
	@PutMapping("/setfast")
	@ResponseStatus(HttpStatus.OK)
	public Clock setFastSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setFastSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/setnormal")
	@ResponseStatus(HttpStatus.OK)
	public Clock setNormalSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setNormalSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/setslow")
	@ResponseStatus(HttpStatus.OK)
	public Clock setSlowSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setSlowSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to1")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToOneSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setOneToOneSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to5")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToFiveSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setOneToFiveSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to10")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToTenSpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setOneToTenSpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to30")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToThirtySpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setOneToThirtySpeed();
		stateController.saveProject(project);
		return clock;
	}

	@PutMapping("/set1to60")
	@ResponseStatus(HttpStatus.OK)
	public Clock setOneToSixtySpeed(String nameProject) {
		Project project = stateController.loadProject(nameProject);
		Clock clock = project.getClock();
		clock.setOneToSixtySpeed();
		stateController.saveProject(project);
		return clock;
	}
}
