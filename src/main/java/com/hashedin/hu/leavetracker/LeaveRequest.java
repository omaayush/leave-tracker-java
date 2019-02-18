package com.hashedin.hu.leavetracker;

import java.time.LocalDate;

public class LeaveRequest {
    LocalDate startDate;
    LocalDate endDate;
    Employee employee;
    public int employeeId;

    public LeaveRequest(LocalDate startDate, LocalDate endDate, int employeeId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeId=employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
