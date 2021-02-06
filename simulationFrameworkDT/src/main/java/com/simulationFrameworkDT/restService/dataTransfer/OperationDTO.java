package com.simulationFrameworkDT.restService.dataTransfer;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OperationDTO {
	
	//Simulation
	private Date date;
	private Timestamp date2;
	private int usersSalomia;
	private int usersFloraInd;
	private int busesSalomia;
	private int busesRoad;	
	private int busesFloraInd;
	private boolean finished = true;
	
	//Important Variables
	private int headwayDesigned;
	private int numberOfBuses;
	
	private int maxbusSalomia;
	private int maxbusFloraInd;
	
	private int maxUsersSalomia;
	private int maxUsersFloraInd;
	
	private Date maxUsersSalomiaDate;
	private Date maxUsersFloraIndDate;
	
	//Analytics
	private double headwayCoefficientOfVariation;
	private double excessWaitingTime;
	private double busesImpact;
	private double passengerSatisfaction;
	
	private double meanHOUsersSalomia;
	private double meanHOUsersFloraInd;
	
	private double meanHOBusSalomia;
	private double meanHOBusFloraInd;
	
	public OperationDTO(int headwayDesigned) {
		long timeOfTravel = 2 * 60 * 60;
		this.headwayDesigned = headwayDesigned;
		this.numberOfBuses = (int)timeOfTravel/headwayDesigned;
	}
	
	public void update(Date date, int usersSalomia, int busesSalomia, int busesRoad, int usersFloraInd,int busesFloraInd) {
		this.date = date;
		this.usersSalomia = usersSalomia;
		this.busesSalomia = busesSalomia;
		this.busesRoad = busesRoad;
		this.usersFloraInd = usersFloraInd;
		this.busesFloraInd = busesFloraInd;
		
		this.date2 = new Timestamp(date.getTime());
		
		if(busesSalomia>maxbusSalomia) {
			maxbusSalomia = busesSalomia;
		}
		if(busesFloraInd>maxbusFloraInd) {
			maxbusFloraInd = busesFloraInd;
		}
		
		if(usersSalomia>maxUsersSalomia) {
			maxUsersSalomia = usersSalomia;
			maxUsersSalomiaDate = date;
		}
		if(usersFloraInd>maxUsersFloraInd) {	
			maxUsersFloraInd = usersFloraInd;
			maxUsersFloraIndDate = date;
		}
		

	}

//	@Override
//	public String toString() {
//		Timestamp dateTime= new Timestamp(date.getTime());
//		return dateTime + ", usersSalomia=" + usersSalomia + ", busesSalomia=" + busesSalomia
//				+ ", busesRoad=" + busesRoad + 
//				", usersFloraInd=" + usersFloraInd + ", busesFloraInd=" + busesFloraInd;
//	}

	@Override
	public String toString() {
		return "Operation [date=" + date2 + ", headwayDesigned=" + headwayDesigned + ", numberOfBuses=" + numberOfBuses
				+ ", usersSalomia=" + usersSalomia + ", busesSalomia=" + busesSalomia + ", busesRoad=" + busesRoad
				+ ", usersFloraInd=" + usersFloraInd + ", busesFloraInd=" + busesFloraInd
				+ ", headwayCoefficientOfVariation=" + headwayCoefficientOfVariation + ", excessWaitingTime="
				+ excessWaitingTime + ", busesImpact=" + busesImpact + ", passengerSatisfaction="
				+ passengerSatisfaction + ", maxUsersSalomia=" + maxUsersSalomia + ", maxUsersFloraInd="
				+ maxUsersFloraInd + ", maxbusSalomia=" + maxbusSalomia + ", maxbusFloraInd=" + maxbusFloraInd + "]";
	}

}
