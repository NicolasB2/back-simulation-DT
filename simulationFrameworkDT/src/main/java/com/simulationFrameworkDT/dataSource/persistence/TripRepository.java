package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.systemState.factorySITM.SITMTrip;

@Repository
public interface TripRepository extends CrudRepository<SITMTrip, Long> {}
