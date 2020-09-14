package com.simulationFrameworkDT.restService.implementation;


import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulationFrameworkDT.dataSource.DataSourceSystem;
import com.simulationFrameworkDT.restService.interfaces.IDataSourceRest;
import com.simulationFrameworkDT.simulation.SimController;
import com.simulationFrameworkDT.systemState.factorySITM.SITMCalendar;
import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;
import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;
import com.simulationFrameworkDT.systemState.factorySITM.SITMStop;

@RequestMapping("simulation/datasource")
@RestController
@CrossOrigin(origins = "*")
public class DataSourceRest implements IDataSourceRest{

	@Autowired
	private DataSourceSystem dataSource;
	
	@Autowired
	private SimController sim;
	
	@GetMapping("/test")
	public ArrayList<SITMPlanVersion> findPlanVersions() {
		return sim.getDataSource().findAllPlanVersions();
	}
	
	@GetMapping("/datagrams")
	public String[] getFileNames() {
		return dataSource.getFileNames();
	}
	
	@GetMapping("/settype")
	public void setSourceType(String type) {
		dataSource.setType(type);
	}
	
	@GetMapping("/planversions")
	public ArrayList<SITMPlanVersion> findAllPlanVersions() {
		return dataSource.findAllPlanVersions();
	}

	@GetMapping("/calendars")
	public ArrayList<SITMCalendar> findAllCalendarsByPlanVersion(long planVersionId) {
		return dataSource.findAllCalendarsByPlanVersion(planVersionId);
	}
	
	@GetMapping("/dates")
	public ArrayList<Date> findDatesByPlanVersion(long planVersionId) {
		
		ArrayList<SITMCalendar> calendars = dataSource.findAllCalendarsByPlanVersion(planVersionId);
		ArrayList<Date> dates = new ArrayList<>();

		dates.add(calendars.get(0).getOperationDay());
		dates.add(calendars.get(calendars.size() - 1).getOperationDay());
		return dates;
	}

	@GetMapping("/lines")
	public ArrayList<SITMLine> findAllLinesByPlanVersion(long planVersionId) {
		return dataSource.findAllLinesByPlanVersion(planVersionId);
	}

	@GetMapping("/stops")
	public ArrayList<SITMStop> findAllStopsByLine(long planVersionId, long lineId) {
		return dataSource.findAllStopsByLine(planVersionId, lineId);
	}

}
