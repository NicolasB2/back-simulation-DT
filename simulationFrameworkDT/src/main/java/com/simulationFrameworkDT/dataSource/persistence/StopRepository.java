package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.factorySITM.SITMStop;

@Repository
public interface StopRepository extends CrudRepository<SITMStop, Long> {
	
	@Query(value="select o.STOPID, o.PLANVERSIONID, o.SHORTNAME, o.LONGNAME, o.GPS_X, o.GPS_Y, o.DECIMALLONGITUDE, o.DECIMALLATITUDE "
			+ "from STOPS o, LINESTOPS s "
			+ "where o.PLANVERSIONID = ?1 and s.STOPID= o.STOPID and s.LINEID= ?2 "
			+ "group by o.STOPID, o.PLANVERSIONID, o.SHORTNAME, o.LONGNAME, o.GPS_X, o.GPS_Y, o.DECIMALLONGITUDE, o.DECIMALLATITUDE", nativeQuery=true)
	public Iterable<SITMStop>findAllStopsbyLineId(long planVersionID,long lineID);
	
}