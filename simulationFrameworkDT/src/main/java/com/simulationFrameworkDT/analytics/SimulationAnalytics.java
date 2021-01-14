package com.simulationFrameworkDT.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.simulationFrameworkDT.analytics.fitness.CompositeFitnessFunction;
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
	public static double MAX_WAITING_TIME_PASSENGER = 600.0;
	
	public static double MIN_NUMBER_OF_BUSES = 1.0;
	public static double MAX_NUMBER_OF_BUSES = 30.0;
	public static double PLANED_NUMBER_OF_BUSES = 25.0;
	
	public SimulationAnalytics(int numberOfBuses, int headwayDesigned, HashMap<Long, ArrayList<Double>> hobspList, HashMap<Long, ArrayList<Double>> hobssList) {
		this.numberOfBuses=numberOfBuses;
		this.headwayDesigned = headwayDesigned;
		this.hobspList = hobspList;
		this.hobssList = hobssList;
		this.hobspLine = margeStations(hobspList);
		this.hobssLine = margeStations(hobssList);
	}

	public void headwayCoefficientOfVariation() {
		
		double meanHobss = mean(hobssLine);
		double variance = variance(hobssLine);
		double standardDeviation = Math.sqrt(variance);
		double headwayCoefficientOfVariation = standardDeviation / meanHobss;

		System.out.println("Headway Coefficient Of Variation " + headwayCoefficientOfVariation);
		System.out.println("");
		
	}

	public void excessWaitingTimeatBusStops() {

		ArrayList<Double> hrs = new ArrayList<Double>();

		for (int j = 0; j < hobspLine.size(); j++) {
			double hr = ((double) hobspLine.get(j) / headwayDesigned) * 100;
			hrs.add(hr);
		}

		double meanHobsp = mean(hobspLine);
		double meanHobss = mean(hobssLine);
		double meanHr = mean(hrs);
		double varianceHr = variance(hrs);
		double EWTaBS = (varianceHr / (2 * meanHr * 100)) * meanHobsp;

		System.out.println("MeanHobsp : " + meanHobsp);
		System.out.println("MeanHobss : " + meanHobss);
		System.out.println("Mean Hr : " + meanHr);
		System.out.println("variance Hr : " + varianceHr);
		System.out.println("EWTaBS : " + EWTaBS);
		System.out.println("");
	}

	public void fitness() {
		double meanHobsp = mean(hobspLine);
		
		final CompositeFitnessFunction function = new CompositeFitnessFunction()
				.withFunction(new CubicFitnessFunction(MIN_NUMBER_OF_BUSES, PLANED_NUMBER_OF_BUSES, MAX_NUMBER_OF_BUSES), 0.4)
				.withFunction(new NormalizedFitnessFunction(MIN_WAITING_TIME_PASSENGER, MAX_WAITING_TIME_PASSENGER), 0.6).validate();

		double fitness = function.evaluateNormalized(new CubicFitnessFunction.CubicFunctionArgument(numberOfBuses),
				new NormalizedFitnessFunction.NormalizedFunctionArgument(meanHobsp));
		System.out.println(fitness);
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
