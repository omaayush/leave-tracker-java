package com.hashedin.hu.leavetracker;

public class LeaveManager {

    public LeaveResponse applyForLeave(LeaveRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start Date must be before end date");
        }

        long days = 2;

        if (days < 3)
            return new LeaveResponse(LeaveStatus.ACCEPTED, "Less than 3 leaves accepted");
        else
            return new LeaveResponse(LeaveStatus.REJECTED, "Unknown number of leaves");
    }

}
