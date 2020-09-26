package com.simulationFrameworkDT.restService.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.simulationFrameworkDT.restService.interfaces.IClockRest;

public class ClockRest implements IClockRest{

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setFastSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setNormalSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setSlowSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setOneToOneSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setOneToFiveSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setOneToTenSpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setOneToThirtySpeed() {
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void setOneToSixtySpeed() {
	}
}
