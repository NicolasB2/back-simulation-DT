package com.simulationFrameworkDT.restService.dataTransfer;

import lombok.Data;

@Data
public class ProjectDTO {
	
	private String name;
	private String initialDate;
	private String finalDate;
	private long lineId;
	private String fileName;
	private String fileSplit;
	private String fileType;
	private long planVersionId;
	
	
}
