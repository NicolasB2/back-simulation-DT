package com.simulationFrameworkDT.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.simulationFrameworkDT.analytics.fitness.CubicFitnessFunction;
import com.simulationFrameworkDT.analytics.fitness.NormalizedFitnessFunction;

import lombok.Getter;

@Getter
public class SimulationAnalytics {

	private int numberOfBuses;
	private int headwayDesigned;
	private HashMap<Long, ArrayList<Double>> hobspList;// passengers
	private HashMap<Long, ArrayList<Double>> hobssList;// buses-stop
	private ArrayList<Double> hobspLine;// passengers
	private ArrayList<Double> hobssLine;// buses-stop
	
	public static double MIN_WAITING_TIME_PASSENGER = 1.0;
	public static double MAX_WAITING_TIME_PASSENGER = 3600.0;
	
	public static double MIN_NUMBER_OF_BUSES = 1.0;
	public static double MAX_NUMBER_OF_BUSES = 34.0;
	public static double PLANED_NUMBER_OF_BUSES = 26.0;
	
	public SimulationAnalytics(int headwayDesigned, HashMap<Long, ArrayList<Double>> hobspList, HashMap<Long, ArrayList<Double>> hobssList) {
		long timeOfTravel = 2 * 60 * 60; //time a bus takes to make the journey
		this.headwayDesigned = headwayDesigned;
		this.hobspList = hobspList;
		this.hobssList = hobssList;
		this.hobspLine = margeStations(hobspList);
		this.hobssLine = margeStations(hobssList);
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
	}
	
	public void evaluationMetrics(SimulationResults operation) {
		operation.setHeadwayCoefficientOfVariation(headwayCoefficientOfVariation());
		operation.setExcessWaitingTime(excessWaitingTime());
		operation.setBusesImpact(fitnessOperation());
		operation.setPassengerSatisfaction(fitnessUsers());
		
		HashMap<Long, Double> meansHOBus = meansHOBus();
		HashMap<Long, Double> meansHOPassengers = meansHOPassengers();
		
		operation.setMeansHOBus(meansHOBus);
		operation.setMeansHOPassengers(meansHOPassengers);
		
	}

	public HashMap<Long, Double> meansHOPassengers() {

		Set<Long> stopIds = hobspList.keySet();
		HashMap<Long, Double> hs = new HashMap<Long, Double>();
		
		for (Long id: stopIds) {
			double meanHobsp = mean(hobspList.get(id));;	
			hs.put(id, meanHobsp);
		}
		return hs;
	}
	
	public HashMap<Long, Double> meansHOBus() {

		Set<Long> stopIds = hobspList.keySet();
		HashMap<Long, Double> hs = new HashMap<Long, Double>();
		
		for (Long id: stopIds) {
			double meanHobss = mean(hobssList.get(id));
			hs.put(id, meanHobss);
		}
		return hs;
	}
	
	public double headwayCoefficientOfVariation() {	
		double meanHobss = mean(hobssLine);
		double variance = variance(hobssLine);
		double standardDeviation = Math.sqrt(variance);
		double HCV = standardDeviation / meanHobss;
		return HCV;	
	}

	public double excessWaitingTime() {

		ArrayList<Double> hrs = new ArrayList<Double>();

		for (int j = 0; j < hobspLine.size(); j++) {
			double hr = ((double) hobspLine.get(j) / headwayDesigned) * 100;
			hrs.add(hr);
		}

		double meanHobsp = mean(hobspLine);
		double meanHr = mean(hrs);
		double varianceHr = variance(hrs);
		double EWT = (varianceHr / (2 * meanHr * 100)) * meanHobsp;
		return(EWT);
	}

	public double fitnessUsers() {
		double meanHobsp = mean(hobspLine);
		final NormalizedFitnessFunction normalizedFitness = new NormalizedFitnessFunction(MIN_WAITING_TIME_PASSENGER, MAX_WAITING_TIME_PASSENGER);
		double fitnessPassengers = normalizedFitness.evaluateNormalized(meanHobsp);
		return fitnessPassengers;
	}

	public double fitnessOperation() {
		final CubicFitnessFunction cubicFitness = new CubicFitnessFunction(MIN_NUMBER_OF_BUSES, PLANED_NUMBER_OF_BUSES, MAX_NUMBER_OF_BUSES);
		double fitnessBuses =  cubicFitness.evaluateNormalized(numberOfBuses);
		return fitnessBuses;
	}
	
	public ArrayList<Double> margeStations(HashMap<Long, ArrayList<Double>> v) {

		ArrayList<Double> line = new ArrayList<Double>();
		Set<Long> stopIds = v.keySet();

		for (long id : stopIds) {
			line.addAll(v.get(id));
		}
		return line;
	}

	public double mean(ArrayList<Double> v) {
		double res = 0;
		for (int i = 0; i < v.size(); i++) {
			res += v.get(i);
		}
		return res / v.size();
	}
	
	public double variance(ArrayList<Double> v) {
		double m = mean(v);
		double sum = 0;
		for (int i = 0; i < v.size(); i++) {
			sum += Math.pow(v.get(i), 2.0);
		}
		return sum / v.size() - Math.pow(m, 2.0);
	}
}
