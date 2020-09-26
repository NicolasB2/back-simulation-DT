package com.simulationFrameworkDT.restService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.ISimulationRest;
import com.simulationFrameworkDT.simulation.SimController;

@RequestMapping("simulation")
@RestController
@CrossOrigin(origins = "*")
public class SimulationRest implements ISimulationRest {
	
	@Autowired
	private SimController SimController;

	@ResponseStatus(HttpStatus.OK)
	public void start(String projectName, long lineId) {
		SimController.start(projectName, lineId);
	}

	@ResponseStatus(HttpStatus.OK)
	public void pause() {
		SimController.pause();
	}

	@ResponseStatus(HttpStatus.OK)
	public void resume() {
		SimController.resume();
	}

	@ResponseStatus(HttpStatus.OK)
	public void stop() {
		SimController.stop();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setFastSpeed() {
		SimController.setFastSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setNormalSpeed() {
		SimController.setNormalSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setSlowSpeed() {
		SimController.setSlowSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setOneToOneSpeed() {
		SimController.setOneToOneSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setOneToFiveSpeed() {
		SimController.setOneToFiveSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setOneToTenSpeed() {
		SimController.setOneToTenSpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setOneToThirtySpeed() {
		SimController.setOneToThirtySpeed();
	}

	@ResponseStatus(HttpStatus.OK)
	public void setOneToSixtySpeed() {
		SimController.setOneToSixtySpeed();
	}

}
