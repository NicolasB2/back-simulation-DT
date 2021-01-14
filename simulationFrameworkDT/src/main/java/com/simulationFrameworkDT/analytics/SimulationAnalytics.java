package com.simulationFrameworkDT.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import lombok.Getter;

@Getter
public class SimulationAnalytics {

	private int headwayDesigned;
	private HashMap<Long, ArrayList<Double>> hobspList;// passengers
	private HashMap<Long, ArrayList<Double>> hobssList;// buses-stop
	
	public SimulationAnalytics(int headwayDesigned, HashMap<Long, ArrayList<Double>> hobspList, HashMap<Long, ArrayList<Double>> hobssList) {
		this.headwayDesigned = headwayDesigned;
		this.hobspList = hobspList;
		this.hobssList = hobssList;
	}

	public void calculatingExcessWaitingTimeatBusStops() {
		
		Set<Long> stopIds = hobspList.keySet();
		for (long id :stopIds) {

			System.out.println("Stop Id" + id);
			ArrayList<Double> Hobsp = hobspList.get(id);
			ArrayList<Double> hrs = new ArrayList<Double>();

//			System.out.println("==================> Hobss");
//			for (int j = 0; j < Hobss.size(); j++) {
//				System.out.println(Hobss.get(j));
//			}
			
//			System.out.println("==================> Hobsp");
//			for (int j = 0; j < Hobsp.size(); j++) {
//				System.out.println(Hobsp.get(j));
//			}
			
//			System.out.println("==================> Hr");
			for (int j = 0; j < Hobsp.size(); j++) {
				double hr = ((double) Hobsp.get(j) / headwayDesigned) * 100;
				hrs.add(hr);
//				System.out.println(hr);
			}

			double meanHobss = mean(hobssList.get(id));
			double meanHobsp = mean(hobspList.get(id));
			double meanHr = mean(hrs);
			double varianceHr = variance(hrs);
			double EWTaBS = (varianceHr / (meanHobss*meanHr*100))*meanHobsp;
			
			System.out.println("MeanHobss : "+meanHobss);
			System.out.println("MeanHobsp : "+meanHobsp);
			System.out.println("Mean Hr : " + meanHr);
			System.out.println("variance Hr : " + varianceHr);
			System.out.println("EWTaBS : "+EWTaBS);
			System.out.println("");
		}
	}
	
	
	public double variance(ArrayList<Double> v) {
		double m = mean(v);
		double sum = 0;
		for (int i = 0; i < v.size(); i++) {
			sum += Math.pow(v.get(i), 2.0);
		}

		return sum / v.size() - Math.pow(m, 2.0);
	}

	public double mean(ArrayList<Double> v) {
		double res = 0;
		for (int i = 0; i < v.size(); i++) {
			res += v.get(i);
		}

		return res / v.size();
	}
}
