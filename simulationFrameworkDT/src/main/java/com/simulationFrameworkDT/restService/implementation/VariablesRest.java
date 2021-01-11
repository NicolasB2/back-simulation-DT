package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.restService.interfaces.IVariablesRest;
import com.simulationFrameworkDT.simulation.state.Project;
import com.simulationFrameworkDT.simulation.state.StateController;
import com.simulationFrameworkDT.simulation.state.Variable;

@RequestMapping("simulation/variables")
@RestController
@CrossOrigin(origins = "*")
public class VariablesRest implements IVariablesRest{

	@Autowired
	private StateController stateController;

	@PutMapping("/setheaders/{id}")
	public ArrayList<Variable> setHeaders(@PathVariable("id") String projectName, @RequestBody String[] headers) {
		Project project = stateController.loadProject(projectName+".dat");
		project.setHeaders(headers);
		stateController.saveProject(project);
		return project.getVariables();
	}

	@GetMapping("/load")
	public ArrayList<Variable> getVariables(String projectName) {
		Project project = stateController.loadProject(projectName+".dat");
//		System.out.println("Variables value 0 "+ project.getVariables().get(0).getValue());
//		System.out.println("Variables value 1"+ project.getVariables().get(1).getValue());
//		System.out.println("Variables value 2"+ project.getVariables().get(2).getValue());
//		System.out.println("Variables value 3"+ project.getVariables().get(3).getValue());
		return project.getVariables();
	}
	
	

}
