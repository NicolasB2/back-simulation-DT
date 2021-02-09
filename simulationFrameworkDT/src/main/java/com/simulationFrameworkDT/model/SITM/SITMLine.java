package com.simulationFrameworkDT.model.SITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.ILine;

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

	public SITMLine(long lineId, String shortName, String description, long planVersionId) {
		this.lineId = lineId;
		this.shortName = shortName;
		this.description = description;
		this.planVersionId = planVersionId;
	}
	
}