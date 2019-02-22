package com.hashedin.hu.huLeaveTracker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LogWorkRepository extends CrudRepository<LogWorkModel, Integer> {
    public ArrayList<LogWorkModel> findAllByEmployeeId(int employeeId);
}
