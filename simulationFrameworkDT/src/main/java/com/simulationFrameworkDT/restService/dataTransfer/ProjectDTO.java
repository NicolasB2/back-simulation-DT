package com.simulationFrameworkDT.restService.dataTransfer;

import java.sql.Date;

import lombok.Data;

@Data
public class ProjectDTO {
	
	private String name;
	private String initialDate;
	private String finalDate;
	private long planVersionId;
	private long lineId;
	private String fileName;
	private String fileSplit;
	private String fileType;
	
	
}
