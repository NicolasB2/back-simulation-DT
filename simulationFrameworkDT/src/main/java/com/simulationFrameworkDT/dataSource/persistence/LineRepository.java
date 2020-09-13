package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.systemState.factorySITM.SITMLine;

@Repository
public interface LineRepository extends CrudRepository<SITMLine, Long> {
	
	@Query(value="select * from lines where planversionID=?1",nativeQuery=true)
	Iterable<SITMLine> findAllLinesbyPlanVersionId(long planVersionId);
}
