package com.hashedin.hu.huLeaveTracker;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Entity
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int employee;

    private TypeOfLeaves typeOfLeaves;
    private StatusOfLeaveRequest statusOfLeaveRequest;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate requestDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public TypeOfLeaves getTypeOfLeaves() {
        return typeOfLeaves;
    }

    public void setTypeOfLeaves(TypeOfLeaves typeOfLeaves) {
        this.typeOfLeaves = typeOfLeaves;
    }

    public StatusOfLeaveRequest getStatusOfLeaveRequest() {
        return statusOfLeaveRequest;
    }

    public void setStatusOfLeaveRequest(StatusOfLeaveRequest statusOfLeaveRequest) {
        this.statusOfLeaveRequest = statusOfLeaveRequest;
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

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    protected LeaveRequest() {}

    public LeaveRequest(TypeOfLeaves typeOfLeaves,  LocalDate startDate, LocalDate endDate) {
        this.typeOfLeaves = typeOfLeaves;
        this.statusOfLeaveRequest = StatusOfLeaveRequest.APPLIED;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestDate = LocalDate.now();
    }

    public LeaveRequest(int employee, TypeOfLeaves typeOfLeaves,  LocalDate startDate, LocalDate endDate) {
        this.employee = employee;
        this.typeOfLeaves = typeOfLeaves;
        this.statusOfLeaveRequest = StatusOfLeaveRequest.APPLIED;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestDate = LocalDate.now();
    }

}
