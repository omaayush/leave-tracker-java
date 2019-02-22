package com.hashedin.hu.leavetracker;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class CompOffManager {
    private long compOffHours =8;
    private Employee employee;
    private LocalDate compOffLeaveRequest;
    private LocalTime startTime;
    private LocalTime endTime;

    CompOffManager()
    {

    }

    public void currentCompOffLeaves(Employee employee){
        if(employee.getCompOffDates().isEmpty()) {
            this.employee.setCompOffLeaves(0);
        } else {
            this.employee.setCompOffLeaves(employee.getCompOffDates().size());
        }
    }

    public LeaveResponse add(Employee employee, LocalDate compOffLeaveRequest, LocalTime startTime, LocalTime endTime)
    {
        this.compOffLeaveRequest = compOffLeaveRequest;
        this.startTime = startTime;
        this.endTime = endTime;
        if(ChronoUnit.HOURS.between(this.startTime, this.endTime)>= compOffHours) {
            this.employee.compOffDates.add(this.compOffLeaveRequest);
            currentCompOffLeaves(employee);
            return new LeaveResponse(LeaveStatus.ACCEPTED, CompOffStatus.SUFFICIENT_HOURS);
        }
        return new LeaveResponse(LeaveStatus.REJECTED, CompOffStatus.INSUFFICIENT_HOURS);
    }

    public LeaveResponse requestLeave(LocalDate compOffLeaveRequest)
    {
        long hasCompOff=allowedCompOffFromRequestedDate(employee,compOffLeaveRequest);
        if(hasCompOff>0)
        {
            removeAllowedCompOffForRequestedDate(compOffLeaveRequest);
            return new LeaveResponse(LeaveStatus.ACCEPTED, CompOffStatus.APPROVED_FOR_THIS_DATE);

        }
            return new LeaveResponse(LeaveStatus.REJECTED, CompOffStatus.NO_COMPOFF_ON_THIS_DATE);

    }

    public boolean isEmpty(Employee employee) {
        return (this.employee.compOffDates.isEmpty());
    }

    public long currentCompOffLeft(Employee employee){
        for(LocalDate dateInCompOffArray:this.employee.compOffDates ) {
            if (ChronoUnit.DAYS.between(
                    dateInCompOffArray, LocalDate.now()) > 30) {
                this.employee.compOffDates.remove(dateInCompOffArray);

            }
        }
        this.employee.compOffDates.sort(LocalDate::compareTo);
        return this.employee.compOffDates.size();
    }

    public long allowedCompOffFromRequestedDate(Employee employee,LocalDate compOffLeaveRequest)
    {long count=0;
        this.employee.compOffDates.sort(LocalDate::compareTo);
        for(LocalDate dateInCompOffArray:this.employee.compOffDates ) {
            if(ChronoUnit.DAYS.between(
                    dateInCompOffArray, compOffLeaveRequest)<=30) {
                count++;
            }
        }
        return count;
    }
    public void removeAllowedCompOffForRequestedDate(LocalDate compOffLeaveRequest)
    {
        for(LocalDate dateInCompOffArray:this.employee.compOffDates ) {
            if(ChronoUnit.DAYS.between(
                    dateInCompOffArray, compOffLeaveRequest)<=30) {
                this.employee.compOffDates.remove(dateInCompOffArray);
                break;
            }
        }
    }}
