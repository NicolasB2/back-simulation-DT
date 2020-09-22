package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.IArc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="ARCS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMArc implements IArc,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ARCID")
	private long arcId;
	
	@Column(name="STARTPOINT")
	private long starPoint;
	
	@Column(name="ENDPOINT")
	private long endPoint;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="ARCLENGTH")
	private String arcLength;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	public SITMArc() {
		super();
	}	
	
	public SITMArc(long arcId, long starPoint, long endPoint, String description, String arcLength,long planVersionId) {
		this.arcId = arcId;
		this.starPoint = starPoint;
		this.endPoint = endPoint;
		this.description = description;
		this.arcLength = arcLength;
		this.planVersionId = planVersionId;
	}
}