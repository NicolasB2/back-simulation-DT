package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

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
	
	public SITMStop () {
		super();
	}

	private HashMap<Long, SITMLine> lines;
	private int passengerQueue;
	private Queue<SITMBus> busQueue = new LinkedList<SITMBus>();
	
	public SITMStop(long stopId, String shortName, String longName, double gPSX, double gPSY, double decimalLongitude, double decimalLatitude, long planVersionId) {
		this.stopId = stopId;
		this.shortName = shortName;
		this.longName = longName;
		this.GPSX = gPSX;
		this.GPSY = gPSY;
		this.decimalLongitude = decimalLongitude;
		this.decimalLatitude = decimalLatitude;
		this.planVersionId = planVersionId;
		lines= new HashMap<Long, SITMLine>();
	}
	
	public SITMStop(long stopId) {
		this.stopId = stopId;
		lines= new HashMap<Long, SITMLine>();
	}
	
	public void addLine(SITMLine line) {
		if(line.isSimulated()) {
			lines.put(line.getLineId(), line);
		}
	}
	
	public void addPassenger() {
		passengerQueue ++;
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
	}
	
	public SITMBus removeBus() {
		return busQueue.poll();
	}
	
}