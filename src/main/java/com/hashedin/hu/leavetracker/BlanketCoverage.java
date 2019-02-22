package com.hashedin.hu.leavetracker;

import java.time.temporal.ChronoUnit;

public class BlanketCoverage {

    public LeaveResponse ifBlanketCoverage(LeaveRequest request) {
        //LeaveResponse response = new LeaveResponse();

        if(request.leaveType == LeaveType.MATERNITY && request.employee.getGender() == Gender.MALE) {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.MATERNITY_LEAVE_FOR_MALE_NOTALLOWED);
        }

        else if(request.leaveType == LeaveType.MATERNITY
                && !request.employee.hasWorkedForDays(request.getStartDate(), 180))
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.EMPLOYEE_WORK_DAYS_LESS);

        }

        else if(request.leaveType == LeaveType.MATERNITY
                && request.employee.getChild() >= 2)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.MATERNITY_LEAVE_UPTO_2_CHILDREN);

        }

        else if(request.leaveType == LeaveType.SABBATICAL
                && !request.employee.hasWorkedForDays(request.getStartDate(), 385 * 2)) {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.EMPLOYEE_WORK_DAYS_LESS);

        }

        else if(request.leaveType == LeaveType.SABBATICAL
                && ChronoUnit.DAYS.between(request.getRequestDate(), request.getStartDate()) < 45) {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.LATE_REQUEST);

        }

        else {
            return new LeaveResponse(LeaveStatus.ACCEPTED, LeaveResponses.VALID_BLANKET_COVERAGE);
        }
    }
}
