package com.simulationFrameworkDT.simulation.state;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class Project implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Date initialDate;
	private Date nextDate;
	private Date finalDate;
	private long planVersionId;
	private long lineId;
	
	//DataSourceSystem
	private String fileType;
	private String fileName;
	private String fileSplit;
	
}
