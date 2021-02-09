package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.SITM.SITMCalendar;

@Repository
public interface CalendarRepository extends CrudRepository<SITMCalendar, Long> {
	
	
	@Query(value="select * from CALENDAR  where planVersionID = ?1 order by operationday", nativeQuery=true)
	Iterable<SITMCalendar> findAllCalendarsbyPlanVersionId(long planVersionId);
	
}
