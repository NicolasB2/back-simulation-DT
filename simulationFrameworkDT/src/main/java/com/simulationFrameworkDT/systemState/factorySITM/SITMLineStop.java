package com.simulationFrameworkDT.systemState.factorySITM;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.simulationFrameworkDT.systemState.factoryInterfaces.ILineStop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="LINESTOPS")
@BatchSize(size=25)
@Getter @Setter @ToString
public class SITMLineStop implements ILineStop,Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="LINESTOPID")
	private long lineStopId;
	
	@Column(name="STOPSEQUENCE")
	private long stopsequence;
	
	@Column(name="ORIENTATION")
	private long orientation;
	
	@Column(name="LINEID")
	private long lineId;
	
	@Column(name="STOPID")
	private long stopId;
	
	@Column(name="PLANVERSIONID")
	private long planVersionId;
	
	@Column(name="LINEVARIANT")
	private long lineVariant;
	
	@Column(name="REGISTERDATE")
	private Date registerDate;
	
	@Column(name="LINEVARIANTTYPE")
	private long lineVariantType;

	public SITMLineStop () {
		super();
	}
	
	public SITMLineStop(long lineStopId, long stopsequence, long orientation, long lineId, long stopId, long planVersionId, long lineVariant, Date registerDate, long lineVariantType) {
		this.lineStopId = lineStopId;
		this.stopsequence = stopsequence;
		this.orientation = orientation;
		this.lineId = lineId;
		this.stopId = stopId;
		this.planVersionId = planVersionId;
		this.lineVariant = lineVariant;
		this.registerDate = registerDate;
		this.lineVariantType = lineVariantType;
	}
}
