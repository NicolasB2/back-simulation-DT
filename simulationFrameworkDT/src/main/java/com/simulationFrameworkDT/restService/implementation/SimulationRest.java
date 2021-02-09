package com.simulationFrameworkDT.restService.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.analytics.SimulationResults;
import com.simulationFrameworkDT.model.ModelDataGenerator;
import com.simulationFrameworkDT.model.SITM.SITMStop;
import com.simulationFrameworkDT.restService.interfaces.ISimulationRest;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.simulation.tools.ProbabilisticDistribution;

@RequestMapping("simulation/controller")
@RestController
@CrossOrigin(origins = "*")
public class SimulationRest implements ISimulationRest {

	@Autowired
	private SimController SimController;
	
	@PutMapping("/simulation/{id}/{headway}")
	@ResponseStatus(HttpStatus.OK)
	public String simulation(@PathVariable("id") String projectName, @PathVariable("headway") int headwayDesigned) {
		ProbabilisticDistribution ai = new ProbabilisticDistribution();
		ai.LogLaplaceDistribution(0.0, 467.00000);

		ProbabilisticDistribution si = new ProbabilisticDistribution();
		si.LogLaplaceDistribution(0.0, 31.6666);

		ProbabilisticDistribution passenger = new ProbabilisticDistribution();
		passenger.ExponentialDistribution(6.54763);

		ModelDataGenerator mdg = new ModelDataGenerator(passenger, ai, si);
		SITMStop stop1 = new SITMStop(500250);
		stop1.addModelDataGenerator(mdg,131);
		SimController.addStationToSimulation(stop1);

		ProbabilisticDistribution ai2 = new ProbabilisticDistribution();
		ai2.WeibullDistribution(1.52116, 599.809135);

		ProbabilisticDistribution si2 = new ProbabilisticDistribution();
		si2.LogLogisticDistribution(37.832223, 5.204677);

		ProbabilisticDistribution passenger2 = new ProbabilisticDistribution();
		passenger2.ExponentialDistribution(7.41318);

		ModelDataGenerator mdg2 = new ModelDataGenerator(passenger2, ai2, si2);
		SITMStop stop2 = new SITMStop(500300);
		stop2.addModelDataGenerator(mdg2, 131);
		SimController.addStationToSimulation(stop2);
		SimController.startSimulation(projectName,131,headwayDesigned*60);
		return projectName;
	}
	
	@PutMapping("/operation/{id}")
	@ResponseStatus(HttpStatus.OK)
	public SimulationResults getSimulationResults(@PathVariable("id") String projectName) {
		return SimController.getSimulationThread().getSimulationResults();
	}
	
	@PutMapping("/stops/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ArrayList<SITMStop> getSimulatedStations(@PathVariable("id") String projectName){
		return SimController.getSimulationThread().getStops();
	}
	
}
