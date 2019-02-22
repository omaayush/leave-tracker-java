package com.hashedin.hu.huLeaveTracker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompOffRepository extends CrudRepository<CompOffModel, Integer> {
    List<CompOffModel> findByAssociatedLogId(int associatedLogId);
}
