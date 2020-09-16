package com.simulationFrameworkDT.project;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
	private String name;
	/**
	 * DataSource selected, FILE_CSV or DATA_BASE. Public string in DataSourceSystem.class
	 */
	private String dSelected;
	private String bdName;
	private long planVersionId;
	private Date initialDate;
	private Date finalDate;
	
	public Project(String name, String dSelected, String bdName, long planVersion, Date initialDate, Date finalDate) {
		super();
		this.name = name;
		this.dSelected = dSelected;
		this.bdName = bdName;
		this.planVersionId = planVersion;
		this.initialDate = initialDate;
		this.finalDate = finalDate;
	}
	
}
