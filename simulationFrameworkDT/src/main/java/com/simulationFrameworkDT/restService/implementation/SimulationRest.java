package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.ISimulationRest;
import com.simulationFrameworkDT.simulation.SimController;

@RequestMapping("simulation/controller")
@RestController
@CrossOrigin(origins = "*")
public class SimulationRest implements ISimulationRest {
	
	@Autowired
	private SimController SimController;

	@PutMapping("/start/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void start(@PathVariable("id") String projectName) {
		SimController.start(projectName+".dat");
	}

	@PutMapping("/pause/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void pause(@PathVariable("id") String projectName) {
		SimController.pause();
	}

	@PutMapping("/resume/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void resume(@PathVariable("id") String projectName) {
		SimController.resume();
	}

	@PutMapping("/finish/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void stop(@PathVariable("id") String projectName) {
		SimController.finish();
	}

}
