package com.simulationFrameworkDT.dataSource.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simulationFrameworkDT.systemState.factorySITM.SITMTask;

@Repository
public interface TaskRepository extends CrudRepository<SITMTask, Long> {}
