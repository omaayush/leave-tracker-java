package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveRequest {
    Employee employee;
    LocalDate startDate;
    LocalDate endDate;
    LeaveType leaveType;

    public LeaveRequest(Employee employee,
                        LocalDate startDate,
                        LocalDate endDate,
                        LeaveType leaveType ) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {     return startDate;  }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {    return endDate;   }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {   return leaveType;    }

    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType;  }

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
            if(this.employee.getGeneralLeaves()>0)
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
            if(this.employee.getCompOffLeaves()>0)
                return true;
        }

        return false;
    }
}