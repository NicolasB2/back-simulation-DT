package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.restService.interfaces.ITargetSystemRest;

@RequestMapping("system")
@RestController
@CrossOrigin(origins = "*")
public class TargetSystemRest implements ITargetSystemRest{
	
	@GetMapping("/buses")
	public ArrayList<SITMBus> findAllBuses() {
		return null;
	}
	
	@GetMapping("/stops")
	public ArrayList<SITMStop> findAllStops() {
		return null;
	}

}
