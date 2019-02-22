package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;
import java.time.LocalTime;

public class LogWorkModel {
    private LocalDate logDate;
    private LocalTime startTime;
    private LocalTime endTime;

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

    public LogWorkModel(LocalDate logDate, LocalTime startTime, LocalTime endTime) {
        this.logDate = logDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
