package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.factorySITM.SITMArc;

@Repository
public interface ArcRepository extends CrudRepository<SITMArc, Long> {}
