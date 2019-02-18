package com.hashedin.hu.leavetracker;

public class LeaveResponse {
    String statusMessage;
    LeaveStatus leaveStatus;

    public LeaveResponse(LeaveStatus leaveStatus, String statusMessage ) {
        this.statusMessage = statusMessage;
        this.leaveStatus = leaveStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}
