package com.hashedin.hu.huLeaveTracker;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "logwork")
public class LogWorkModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int employeeId;

    @Column
    private LocalDate logDate;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LogWorkModel() {}

    public LogWorkModel(int employeeId, LocalDate logDate, LocalTime startTime, LocalTime endTime) {
        this.employeeId = employeeId;
        this.logDate = logDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
