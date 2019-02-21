package com.hashedin.hu.leavetracker;

public class LeavesLeft {
    private LeaveRequest leaveRequest;
    public LeavesLeft(LeaveRequest leaveRequestRequest) {
        this.leaveRequest=leaveRequestRequest;
    }

    //    public LeaveResponse[] getAllLeaves(Employee employee)
//    {
//        LeaveResponse allLeaves[]=new LeaveResponse[5];
//        return allLeaves;
//    }
//        public LeaveResponse getAllLeaves()
//        {
//            return new LeaveResponse(this.leaveRequest.employee.toString());
//        }

    public void leavesDeductions() {

        if(this.leaveRequest.leaveType==LeaveType.GENERAL)
        {
            this.leaveRequest.employee.setGeneralLeaves(
                    this.leaveRequest.employee.getGeneralLeaves()-this.leaveRequest.noOfHolidaysApplied());
        }
        else if(this.leaveRequest.leaveType==LeaveType.SABBATICAL)
        {
            this.leaveRequest.employee.setSabaticalLeaves(
                    this.leaveRequest.employee.getSabaticalLeaves()-this.leaveRequest.noOfHolidaysApplied());
        }
        else if(this.leaveRequest.leaveType==LeaveType.MATERNITY)
        {
            this.leaveRequest.employee.setMaternalLeaves(
                    this.leaveRequest.employee.getMaternalLeaves()-this.leaveRequest.noOfHolidaysApplied());
        }
        else if(this.leaveRequest.leaveType==LeaveType.PATERNITY)
        {
            this.leaveRequest.employee.setPaternalLeaves(
                    this.leaveRequest.employee.getPaternalLeaves()-this.leaveRequest.noOfHolidaysApplied());
        }
        else if(this.leaveRequest.leaveType==LeaveType.COMPOFF)
        {
            //deduct comp off leaves
            CompOffManager compOffManager=new CompOffManager();
                compOffManager.removeAllowedCompOffForRequestedDate(this.leaveRequest.getStartDate());

        }
    }
}
