package com.simulationFrameworkDT.model.factorySITM;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.model.factoryInterfaces.ITask;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="TASKS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMTask implements ITask,Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="TASKID")
	private long taskId;
	
	@Column(name="SCHEDULETYPEID")
	private long scheduleTypeId;
	
	@Column(name="LINES_LINEID")
	private long lineId;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;

	public SITMTask () {
		super();
	}
	
	public SITMTask(long taskId, long scheduleTypeId, long lineId, long planVersionId) {
		this.taskId = taskId;
		this.scheduleTypeId = scheduleTypeId;
		this.lineId = lineId;
		this.planVersionId = planVersionId;
	}
}