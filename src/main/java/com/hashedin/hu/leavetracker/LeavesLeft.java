package com.hashedin.hu.leavetracker;

public class LeavesLeft {
    public void getAllLeaves(Employee employee)
    {

    }

    public void afterApplyingLeave(LeaveRequest request) {

        if(request.leaveType==LeaveType.GENERAL)
        {
            request.employee.setGeneralLeaves(
                    request.employee.getGeneralLeaves()-request.noOfHolidaysApplied());
        }
        else if(request.leaveType==LeaveType.SABBATICAL)
        {
            request.employee.setSabaticalLeaves(
                    request.employee.getSabaticalLeaves()-request.noOfHolidaysApplied());
        }
        else if(request.leaveType==LeaveType.MATERNITY)
        {
            request.employee.setMaternalLeaves(
                    request.employee.getMaternalLeaves()-request.noOfHolidaysApplied());
        }
        else if(request.leaveType==LeaveType.PATERNITY)
        {
            request.employee.setPaternalLeaves(
                    request.employee.getPaternalLeaves()-request.noOfHolidaysApplied());
        }
        else if(request.leaveType==LeaveType.COMPOFF)
        {
            request.employee.setCompOffLeaves(
                    request.employee.getCompOffLeaves()-request.noOfHolidaysApplied());
        }
    }
}
