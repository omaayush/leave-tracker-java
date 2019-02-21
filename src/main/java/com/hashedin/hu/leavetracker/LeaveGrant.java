package com.hashedin.hu.leavetracker;

import java.time.LocalDate;

public class LeaveGrant {
    private int employeeId;
    private LeaveResponse leaveResponse;
    private int duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;

    public LeaveGrant(int employeeId, LeaveResponse leaveResponse,
                      int duration, LocalDate startDate, LocalDate endDate,
                      LeaveType leaveType) {
        this.employeeId = employeeId;
        this.leaveResponse = leaveResponse;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LeaveResponse getLeaveResponse() {
        return leaveResponse;
    }

    public void setLeaveResponse(LeaveResponse leaveResponse) {
        this.leaveResponse = leaveResponse;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
}

