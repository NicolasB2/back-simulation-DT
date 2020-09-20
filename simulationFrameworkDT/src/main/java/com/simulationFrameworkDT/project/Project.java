package com.simulationFrameworkDT.project;

import java.io.Serializable;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Date initialDate;
	private Date finalDate;
	private long planVersionId;
	
	//DataSourceSystem
	private String fileName;
	private String split;
	private int filePosition;
	
	public Project(String name, Date initialDate, Date finalDate, long planVersionId) {
		this.name = name;
		this.initialDate = initialDate;
		this.finalDate = finalDate;
		this.planVersionId = planVersionId;
	}

	public Project(String name, Date initialDate, Date finalDate, long planVersionId, String fileName, String split, int filePosition) {
		this.name = name;
		this.initialDate = initialDate;
		this.finalDate = finalDate;
		this.planVersionId = planVersionId;
		this.fileName = fileName;
		this.split = split;
		this.filePosition = filePosition;
	}
	
	
}
