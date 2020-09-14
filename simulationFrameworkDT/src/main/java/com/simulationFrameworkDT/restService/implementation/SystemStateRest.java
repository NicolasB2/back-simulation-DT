package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.ISystemStateRest;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.systemState.factorySITM.SITMBus;

@RequestMapping("simulation")
@RestController
@CrossOrigin(origins = "*")
public class SystemStateRest implements ISystemStateRest{

	@Autowired
	private SimController simController;
	
	@GetMapping("/buses")
	public ArrayList<SITMBus> findAllBuses(long lineId) {
		return simController.getBusesByLine(lineId);
	}
}
