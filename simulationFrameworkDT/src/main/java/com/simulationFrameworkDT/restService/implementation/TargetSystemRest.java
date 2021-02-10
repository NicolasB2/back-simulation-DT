	package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.model.SITM.SITMBus;
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
		
		Project project = stateController.getProject();
		
		if(project!=null) {
			return project.getTargetSystem().filterBusesByLineId(project.getLineId());
		}
		return new ArrayList<>();
	}

}
