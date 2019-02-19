package com.hashedin.hu.leavetracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LeaveManager {

    public LeaveResponse applyForLeave(LeaveRequest request) {
        if (request.invalidDate()) {
            throw new IllegalArgumentException("Start Date must be before end date");
        }

        if (request.isAllowed())
        {
            LeavesLeft
            return new LeaveResponse(LeaveStatus.ACCEPTED, "Number of days requested for leave is permissible");
        }
        else
            return new LeaveResponse(LeaveStatus.REJECTED, "Number of days requested for leave is not permissible");
    }

    public long checkLeaveBalance(Employee employee){
        return employee.getGeneralLeaves();
    }

    public long logExtraHoursWorked(LocalDateTime startTime,LocalDateTime endTime)
    {
        return ChronoUnit.HOURS.between(fromDate, toDate);
    }

    //secondary functions

}
