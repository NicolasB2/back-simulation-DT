package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.systemState.factorySITM.SITMScheduleTypes;

@Repository
public interface ScheludeTypesRepository extends CrudRepository<SITMScheduleTypes, Long> {}