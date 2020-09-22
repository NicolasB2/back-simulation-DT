package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.IPlanVersion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="PLANVERSIONS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMPlanVersion implements IPlanVersion,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	@Column(name="ACTIVATIONDATE")
	private Date activationDate;

	@Column(name="CREATIONDATE")
	private Date creationDate;

	public SITMPlanVersion () {
		super();
	}
	
	public SITMPlanVersion(long planVersionId, Date activationDate, Date creationDate) {
		this.planVersionId = planVersionId;
		this.activationDate = activationDate;
		this.creationDate = creationDate;
	}
}