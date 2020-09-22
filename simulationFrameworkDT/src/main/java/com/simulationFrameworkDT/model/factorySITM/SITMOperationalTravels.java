package com.simulationFrameworkDT.model.factorySITM;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
 

@Entity
@Table(name="OPERATIONALTRAVELS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMOperationalTravels {
	
	@Id
	@Column(name="OPERTRAVELID")
	private Long opertravelId;
	
	@Column(name="BUSID")
	private Long busId;
	
	@Column(name="LASTSTOPID")
	private Long laststopId;
	
	@Column(name="GPS_X")
	private String GPS_X;
	
	@Column(name="GPS_Y")
	private String GPS_Y;
	
	@Column(name="DEVIATIONTIME")
	private Long deviationTime;
	
	@Column(name="ODOMETERVALUE")
	private Long odometervalue;
	
	@Column(name="LINEID")
	private Long lineId;
	
	@Column(name="TASKID")
	private Long taskId;
	
	@Column(name="TRIPID")
	private Long tripId;
	
	@Column(name="RIGHTCOURSE")
	private Long rightcourse;
	
	@Column(name="ORIENTATION")
	private Long orientation;
	
	@Column(name="EVENTDATE")
	private Date eventDate;
	
	@Column(name="EVENTTIME")
	private Long eventTime;
	
	@Column(name="REGISTERDATE")
	private Date registerDate;
	
	@Column(name="EVENTTYPEID")
	private Long eventTypeId;
	
	@Column(name="NEARESTSTOPID")
	private Long nearestStopId;
	
	@Column(name="LASTUPDATEDATE")
	private Date lastUpDateDate;
	
	@Column(name="NEARESTSTOPMTS")
	private Long nearestStopMTS;
	
	@Column(name="UPDNEARESTFLAG")
	private String updNearestFlag;
	
	@Column(name="LOGFILEID")
	private Long logFileId;
	
	@Column(name="NEARESTPLANSTOPID")
	private Long nearestPlanStopId;
	
	@Column(name="NEARESTPLANSTOPMTS")
	private Long nearestPlanStopMTS;
	
	@Column(name="PLANSTOPAUTH")
	private String planStopAuth;
	
	@Column(name="RADIUSTOLERANCEMTS")
	private Long radiusToleranceMTS;
	
	@Column(name="TIMEDIFF")
	private Long timeDiff;
	
	public SITMOperationalTravels () {
		super();
	}

	public SITMOperationalTravels(Long opertravelId, Long busId,  long lineId, String gPS_X, String gPS_Y, Date eventDate) {
		this.opertravelId = opertravelId;
		this.busId = busId;
		this.lineId = lineId;
		this.GPS_X = gPS_X;
		this.GPS_Y = gPS_Y;
		this.eventDate = eventDate;
	}
}
