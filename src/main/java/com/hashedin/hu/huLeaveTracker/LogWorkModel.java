package com.hashedin.hu.huLeaveTracker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class LogWorkModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    LocalDate logDate;
    LocalTime startTime;
    LocalTime endTime;

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

    public LogWorkModel(LocalDate logDate, LocalTime startTime, LocalTime endTime) {
        this.logDate = logDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}