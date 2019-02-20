package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class LeaveManager {
    ArrayList<LeaveRequest> listOfApprovedRequests;
    CompOffManager compOffManager;

    LeaveManager() {
        this.listOfApprovedRequests = new ArrayList<LeaveRequest>();
        this.compOffManager = new CompOffManager();
    }


    public LeaveResponse applyForLeave(LeaveRequest request)
    {
        LeavesLeft leavesLeft = new LeavesLeft(request);
        LeaveResponse response=new LeaveResponse();

        //LeaveRequest leaveRequest=new LeaveRequest()
        if(givenDateIsNull(request.startDate) || givenDateIsNull(request.endDate)) {
            response.leaveStatus=LeaveStatus.REJECTED;
            //add leave response also
            throw new IllegalArgumentException("The start date and end date cannot be null");
        }

        if (request.invalidDate()) {
            response.leaveStatus=LeaveStatus.REJECTED;
            throw new IllegalArgumentException("Start Date must be before end date");
        }


        if (request.isAllowed())
        {
            leavesLeft.leavesDeductions();
            return new LeaveResponse(LeaveStatus.ACCEPTED, LeaveResponses.LEAVE_APPROVED);
        }
        else
            return request.whyRejected();
            //return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.LEAVE_BALANCE_INSUFFICIENT);
    }

    private boolean givenDateIsNull(LocalDate startDate) {
        return startDate == null;
    }

    public LeaveResponse checkLeaveBalance(Employee employee){
        return employee.getAllLeaves();
    }

    public LeaveResponse logExtraHoursWorked
            (Employee employee,
             LocalDate dateOfAppliedCompOff,
             LocalTime startTime,
             LocalTime endTime)
    {
        return compOffManager.add(employee,dateOfAppliedCompOff,startTime,endTime);
    }

    //secondary functions

}
