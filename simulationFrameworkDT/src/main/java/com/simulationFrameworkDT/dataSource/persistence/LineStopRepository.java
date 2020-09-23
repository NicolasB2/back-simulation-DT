package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.factorySITM.SITMLineStop;

@Repository
public interface LineStopRepository extends CrudRepository<SITMLineStop, Long> {}