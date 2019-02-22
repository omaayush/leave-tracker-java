package com.hashedin.hu.huLeaveTracker;

enum LogWorkStatus {
    LOG_WORK_ADDED("Log Work successfully added to store"),
    LOG_WORK_REMOVED("Log Work Removed"),
    LOG_NOT_FOUND("Log not found");

    private String logWorkStatus;
    LogWorkStatus(String status) {
        this.logWorkStatus = status;
    }
}