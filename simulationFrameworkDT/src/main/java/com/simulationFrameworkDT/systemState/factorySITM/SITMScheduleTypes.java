package com.simulationFrameworkDT.systemState.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.systemState.factoryInterfaces.IScheduleTypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="SCHEDULETYPES")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMScheduleTypes implements IScheduleTypes,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="SCHEDULETYPEID")
	private long scheduleTypeId;
	
	@Column(name="SHORTNAME")
	private String shortName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	public SITMScheduleTypes () {
		super();
	}
	
	public SITMScheduleTypes(long scheduleTypeId, String shortName, String description, long planVersionId) {
		this.scheduleTypeId = scheduleTypeId;
		this.shortName = shortName;
		this.description = description;
		this.planVersionId = planVersionId;
	}
}