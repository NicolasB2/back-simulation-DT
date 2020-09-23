package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.ITrip;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="TRIPS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMTrip implements ITrip,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="TRIPID")
	private long tripId;
	
	@Column(name="SCHEDULETYPEID")
	private long scheduleTypeId;
	
	
	@Column(name="TRIPSEQUENCE")
	private long tripSequence;
	
	@Column(name="STARTTIME")
	private String startTime;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="ORIENTATION")
	private boolean orientation;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;
	
	@Column(name="TASKID")
	private String taskId;
	
	@Column(name="LINEID")
	private long linedD;
	
	@Column(name="STARTSTOPID")
	private long startStopID;
	
	@Column(name="ENDSTOPID")
	private long endStopID;
	
	public SITMTrip () {
		super();
	}

	public SITMTrip(long tripId, long scheduleTypeId, long tripSequence, String startTime, String description, boolean orientation, long planVersionId, String taskId, long linedD, long startStopID, long endStopID) {
		this.tripId = tripId;
		this.scheduleTypeId = scheduleTypeId;
		this.tripSequence = tripSequence;
		this.startTime = startTime;
		this.description = description;
		this.orientation = orientation;
		this.planVersionId = planVersionId;
		this.taskId = taskId;
		this.linedD = linedD;
		this.startStopID = startStopID;
		this.endStopID = endStopID;
	}
}