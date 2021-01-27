package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.ModelDataGenerator;
import com.simulationFrameworkDT.model.factoryInterfaces.IStop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="STOPS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMStop implements IStop,Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="STOPID")
	private long stopId;

	@Column(name="SHORTNAME")
	private String shortName;

	@Column(name="LONGNAME")
	private String longName;
	
	@Column(name="GPS_X")
	private double GPSX;
	
	@Column(name="GPS_Y")
	private double GPSY;
	
	@Column(name="DECIMALLONGITUDE")
	private double decimalLongitude;
	
	@Column(name="DECIMALLATITUDE")
	private double decimalLatitude;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	private HashMap<Long, ModelDataGenerator> modelDataGenerators;
	private Queue<SITMBus> busQueue = new LinkedList<SITMBus>();
	private int passengerQueue;
	
	private Date maxUsersDate;
	private int maxUsers;
	private int maxBuses;
	
	public SITMStop () {
		super();
	}
	
	public SITMStop(long stopId) {
		this.stopId = stopId;
		modelDataGenerators= new HashMap<Long, ModelDataGenerator>();
	}
	
	public SITMStop(long stopId, String shortName, String longName, double gPSX, double gPSY, double decimalLongitude, double decimalLatitude, long planVersionId) {
		this.stopId = stopId;
		this.shortName = shortName;
		this.longName = longName;
		this.GPSX = gPSX;
		this.GPSY = gPSY;
		this.decimalLongitude = decimalLongitude;
		this.decimalLatitude = decimalLatitude;
		this.planVersionId = planVersionId;
		modelDataGenerators= new HashMap<Long, ModelDataGenerator>();
	}
	
	public void addModelDataGenerator(ModelDataGenerator modelDataGenerator, long lineId) {
		if(modelDataGenerator.isSimulated()) {
			modelDataGenerators.put(lineId, modelDataGenerator);
		}
	}
	
	public void addPassenger(Date eventDate) {
		passengerQueue ++;
		
		if(passengerQueue>=maxUsers) {
			maxUsers=passengerQueue;
			maxUsersDate = eventDate;
		}
	}
	
	public int removePassenger(int busSpace) {
		if(busSpace>=passengerQueue) {
			passengerQueue = 0;
		}else {
			passengerQueue -= busSpace;
		}
		return passengerQueue;
	}
	
	public void addBus(SITMBus bus) {
		busQueue.add(bus);
		
		if(busQueue.size()>=maxBuses) {
			maxBuses=busQueue.size();
		}
	}
	
	public SITMBus removeBus() {
		return busQueue.poll();
	}
	
}