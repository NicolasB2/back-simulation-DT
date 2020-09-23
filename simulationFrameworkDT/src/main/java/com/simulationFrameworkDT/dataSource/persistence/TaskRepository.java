package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.model.factorySITM.SITMTask;

@Repository
public interface TaskRepository extends CrudRepository<SITMTask, Long> {}
