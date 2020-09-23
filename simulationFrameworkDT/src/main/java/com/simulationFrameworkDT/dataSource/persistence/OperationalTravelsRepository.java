package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.factorySITM.SITMOperationalTravels;

@Repository
public interface OperationalTravelsRepository extends CrudRepository<SITMOperationalTravels, Long> {
	
	
	@Query(value="select o.OPERTRAVELID, o.BUSID, o.LASTSTOPID, o.GPS_X, o.GPS_Y, o.DEVIATIONTIME, o.ODOMETERVALUE, o.LINEID, o.TASKID, o.TRIPID, o.RIGHTCOURSE, o.ORIENTATION, o.EVENTDATE, o.EVENTTIME, o.REGISTERDATE, o.EVENTTYPEID, o.NEARESTSTOPID, o.LASTUPDATEDATE, o.NEARESTSTOPMTS, o.UPDNEARESTFLAG, o.LOGFILEID, o.NEARESTPLANSTOPID, o.NEARESTPLANSTOPMTS, o.PLANSTOPAUTH, o.RADIUSTOLERANCEMTS, o.TIMEDIFF "
			+ "from operationaltravels o "
			+ "where o.eventdate between ?1 and ?2 and rownum<1000"
			,nativeQuery=true)
	public Iterable<SITMOperationalTravels>findAllOP(String initialDate, String lastDate);
	
	
	@Query(value="select o.OPERTRAVELID, o.BUSID, o.LASTSTOPID, o.GPS_X, o.GPS_Y, o.DEVIATIONTIME, o.ODOMETERVALUE, o.LINEID, o.TASKID, o.TRIPID, o.RIGHTCOURSE, o.ORIENTATION, o.EVENTDATE, o.EVENTTIME, o.REGISTERDATE, o.EVENTTYPEID, o.NEARESTSTOPID, o.LASTUPDATEDATE, o.NEARESTSTOPMTS, o.UPDNEARESTFLAG, o.LOGFILEID, o.NEARESTPLANSTOPID, o.NEARESTPLANSTOPMTS, o.PLANSTOPAUTH, o.RADIUSTOLERANCEMTS, o.TIMEDIFF "
			+ "from operationaltravels o "
			+ "where o.eventdate = ?1 and o.eventtime >= ?2 and o.eventtime < ?3 and rownum<100"
			,nativeQuery=true)
	public Iterable<SITMOperationalTravels>findAllOPDay(String date, long startHour, long endHour);
	
	
	
}
