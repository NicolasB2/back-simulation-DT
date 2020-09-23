package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.ICalendar;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="CALENDAR")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMCalendar implements ICalendar,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name="CALENDARID")
	private long calendarId;
	
	@NotNull
	@Column(name="OPERATIONDAY")
	private Date operationDay;
	
	@NotNull
	@Column(name="SCHEDULETYPEID")
	private long scheduleTypeId;
	
	@NotNull
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	public SITMCalendar () {
		super();
	}
	
	public SITMCalendar(long calendarId, Date operationDay, long scheduleTypeId, long planVersionId) {
		this.calendarId = calendarId;
		this.operationDay = operationDay;
		this.scheduleTypeId = scheduleTypeId;
		this.planVersionId = planVersionId;
	}
}