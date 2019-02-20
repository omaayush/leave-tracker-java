package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveRequest {
    Employee employee;
    LocalDate startDate;
    LocalDate endDate;
    LeaveType leaveType;
    LocalDate requestDate;

    public LeaveRequest(Employee employee,
                        LocalDate startDate,
                        LocalDate endDate,
                        LeaveType leaveType ) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.requestDate=LocalDate.now();
    }


    public long noOfHolidaysApplied() {
        return ChronoUnit.DAYS.between(this.startDate,this.endDate);
    }

    public boolean invalidDate() {
        return this.startDate.isAfter(this.endDate) || LocalDate.now().isAfter(this.startDate);
    }

    public boolean isAllowed()
    {
        if(this.leaveType==LeaveType.GENERAL)
        {
            if(this.employee.getGeneralLeaves()>0 && this.employee.getGeneralLeaves()>=this.noOfHolidaysApplied())
                return true;
        }
        else if(this.leaveType==LeaveType.SABBATICAL)
        {
            if(this.employee.experience()>=2)
                return true;
        }
        else if(this.leaveType==LeaveType.MATERNITY)
        {
            if(this.employee.getGender()==Gender.FEMALE
                    && this.employee.getChild()<2 &&
                    this.noOfHolidaysApplied()<=180)
                return true;
        }
        else if(this.leaveType==LeaveType.PATERNITY)
        {
            if(this.employee.getGender()==Gender.MALE
                    && this.employee.getChild()<2
                    && this.noOfHolidaysApplied()<=30)
                return true;
        }
        else if(this.leaveType==LeaveType.COMPOFF)
        {
            CompOffManager compOffManager=new CompOffManager();
            compOffManager.currentCompOffLeaves(this.employee);
            long hasCompOff=compOffManager.allowedCompOffFromRequestedDate(this.employee,this.startDate);
            if(hasCompOff>0)
            {
                return true;
            }
        }
        return false;
    }

    public LeaveResponse whyRejected()
    {
        if(this.leaveType==LeaveType.GENERAL)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.GENERAL_LEAVE_BALANCE_INSUFFICIENT);

        }
        else if(this.leaveType==LeaveType.SABBATICAL)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.SABBATICAL_LEAVE_BALANCE_INSUFFICIENT);
        }
        else if(this.leaveType==LeaveType.MATERNITY)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.MATERNITY_LEAVE_BALANCE_INSUFFICIENT);
        }
        else if(this.leaveType==LeaveType.PATERNITY)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.PATERNITY_LEAVE_BALANCE_INSUFFICIENT);
        }
        else if(this.leaveType==LeaveType.COMPOFF)
        {
            return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.COMPOFF_LEAVE_BALANCE_INSUFFICIENT);
        }
        return new LeaveResponse(LeaveStatus.REJECTED, LeaveResponses.LEAVE_BALANCE_INSUFFICIENT);
    }
}