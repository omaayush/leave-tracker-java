package com.hashedin.hu.leavetracker;
public class LeaveResponse {
    LeaveStatus leaveStatus;
    LeaveResponses leaveResponses;
    private CompOffStatus compOffStatus;
    private String allLeaves;

    LeaveResponse()
    {
    }

    public LeaveResponse(LeaveStatus leaveStatus, LeaveResponses leaveResponses ) {
        this.leaveResponses=leaveResponses;
        this.leaveStatus = leaveStatus;
    }

    public LeaveResponse(LeaveStatus leaveStatus, CompOffStatus compOffStatus) {
        this.leaveStatus=leaveStatus;
        this.compOffStatus=compOffStatus;
    }

    public LeaveResponse(String hi)
    {
        this.allLeaves=hi;
    }}