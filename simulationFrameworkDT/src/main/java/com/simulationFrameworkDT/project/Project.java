package com.simulationFrameworkDT.project;

import java.io.Serializable;
import java.sql.Date;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;

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
	
	private DataSourceSystem dataSource;
	
	public Project(String name, Date initialDate, Date finalDate, long planVersionId) {
		this.name = name;
		this.initialDate = initialDate;
		this.finalDate = finalDate;
		this.planVersionId = planVersionId;
	}
}
