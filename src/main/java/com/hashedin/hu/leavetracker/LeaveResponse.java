package com.hashedin.hu.leavetracker;

public class LeaveResponse {
    LeaveStatus leaveStatus;
    LeaveResponses leaveResponses;
    private CompOffStatus compOffStatus;
    private CurrentLeaves currentLeaves;
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
    }
}

//    public LeaveResponse(CurrentLeaves currentLeaves ) {
//        this.currentLeaves=currentLeaves;
//    }
//    @Override
//    public String toString() {
//    return "LeaveResponse{" +
//            "statusOfLeaveRequest=" + statusOfLeaveRequest +
//            ", reason='" + reason + '\'' +
//            '}';
//}


