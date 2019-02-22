package com.hashedin.hu.huLeaveTracker;

public class LeaveResponse {
    StatusOfLeaveRequest statusOfLeaveRequest;
    ReasonsForLeaveResponse reason;

    LeaveResponse() {}

    public LeaveResponse(StatusOfLeaveRequest statusOfLeaveRequest, ReasonsForLeaveResponse reason) {
        this.statusOfLeaveRequest = statusOfLeaveRequest;
        this.reason = reason;
    }

    public StatusOfLeaveRequest getStatusOfLeaveRequest() {
        return statusOfLeaveRequest;
    }

    public void setStatusOfLeaveRequest(StatusOfLeaveRequest statusOfLeaveRequest) {
        this.statusOfLeaveRequest = statusOfLeaveRequest;
    }

    public ReasonsForLeaveResponse getReason() {
        return reason;
    }

    public void setReason(ReasonsForLeaveResponse reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LeaveResponse{" +
                "statusOfLeaveRequest=" + statusOfLeaveRequest +
                ", reason='" + reason + '\'' +
                '}';
    }
}
