package com.simulationFrameworkDT.systemState.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.systemState.factoryInterfaces.IBus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="BUSES")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMBus implements IBus,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="BUSID")
	private long busId;
	
	@Column(name="BUSNUMBER")
	private long busNumber;
	
	@Column(name="IDENTIFICATION")
	private String identification;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;
	
	@Column(name="BUSTYPEID")
	private long busTypeId;
	
	private double latitude;
	
	private double longitude;
	
	private long lineId;

	public SITMBus () {
		super();
	}
	
	public SITMBus(long busId, long busNumber, String identification, long busTypeId, long planVersionId) {
		this.busId = busId;
		this.busNumber = busNumber;
		this.identification = identification;
		this.busTypeId = busTypeId;
		this.planVersionId = planVersionId;
	}
}