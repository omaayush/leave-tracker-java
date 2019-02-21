package com.hashedin.hu.huLeaveTracker;
enum CompOffStatus {
    AVAILABLE("AVAILABLE"),
    CLAIMED("CLAIMED");

    private String compOffStatus;

    CompOffStatus(String compOffStatus) {
        this.compOffStatus = compOffStatus;
    }
}