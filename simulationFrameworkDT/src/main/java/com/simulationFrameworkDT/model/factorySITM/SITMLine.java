package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.ILine;
import com.simulationFrameworkDT.simulation.tools.IDistribution;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="LINES")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMLine implements ILine,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="LINEID")
	private long lineId;
	
	@Column(name="SHORTNAME")
	private String shortName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;
	
	private IDistribution passengersDistribution;
	private IDistribution interArrivalDistribution;
	private IDistribution serviceDistribution;

	public SITMLine(long lineId, String shortName, String description, long planVersionId) {
		this.lineId = lineId;
		this.shortName = shortName;
		this.description = description;
		this.planVersionId = planVersionId;
	}

	public SITMLine(long lineId, String shortName, IDistribution passengersDistribution, IDistribution interArrivalDistribution, IDistribution serviceDistribution) {
		this.lineId = lineId;
		this.shortName = shortName;
		this.passengersDistribution = passengersDistribution;
		this.interArrivalDistribution = interArrivalDistribution;
		this.serviceDistribution = serviceDistribution;
	}
	
	public boolean isSimulated() {
		boolean haveId = lineId != 0;
		boolean havePassengers = passengersDistribution != null;
		boolean haveInterArrival = interArrivalDistribution != null;
		boolean haveService = serviceDistribution != null;
		return haveId && havePassengers && haveInterArrival && haveService;
	}
	
}