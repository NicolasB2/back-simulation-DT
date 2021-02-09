package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.SITM.SITMBus;

@Repository
public interface BusRepository extends CrudRepository<SITMBus, Long> {}
