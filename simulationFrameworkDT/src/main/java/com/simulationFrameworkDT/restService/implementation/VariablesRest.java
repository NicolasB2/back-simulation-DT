package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.IVariablesRest;
import com.simulationFrameworkDT.simulation.state.Variable;

@RequestMapping("simulation/variables")
@RestController
@CrossOrigin(origins = "*")
public class VariablesRest implements IVariablesRest{
	
	@GetMapping("")
	public ArrayList<Variable> getVariables() {
		return null;
	}

}
