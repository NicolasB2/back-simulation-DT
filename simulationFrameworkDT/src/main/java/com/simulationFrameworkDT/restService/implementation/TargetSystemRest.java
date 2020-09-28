package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.model.factorySITM.SITMBus;
import com.simulationFrameworkDT.model.factorySITM.SITMStop;
import com.simulationFrameworkDT.restService.interfaces.ITargetSystemRest;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;

@RequestMapping("simulation/system")
@RestController
@CrossOrigin(origins = "*")
public class TargetSystemRest implements ITargetSystemRest{
	
	@Autowired
	private StateController stateController;
	
	@GetMapping("/buses")
	public ArrayList<SITMBus> findAllBuses(String projectName) {
		Project project = stateController.loadProject(projectName);
		return project.getTargetSystem().filterBusesByLineId(project.getLineId());
	}
	
	@GetMapping("/stops")
	public ArrayList<SITMStop> findAllStops(String projectName) {
		return null;
	}

}
