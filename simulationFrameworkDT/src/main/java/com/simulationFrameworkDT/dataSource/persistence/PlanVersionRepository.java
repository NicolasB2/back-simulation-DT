package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.systemState.factorySITM.SITMPlanVersion;

@Repository
public interface PlanVersionRepository extends CrudRepository<SITMPlanVersion, Long>{}