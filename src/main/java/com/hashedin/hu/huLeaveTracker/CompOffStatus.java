package com.hashedin.hu.huLeaveTracker;

enum CompOffStatus {
    AVAILABLE("available"),
    CLAIMED("CLAIMED");

    private String compoffStatus;

    CompOffStatus(String compoffStatus) {
        this.compoffStatus = compoffStatus;
    }
}