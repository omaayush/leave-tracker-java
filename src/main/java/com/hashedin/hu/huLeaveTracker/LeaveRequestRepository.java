package com.hashedin.hu.huLeaveTracker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends CrudRepository<LeaveRequest, Integer> {

}
