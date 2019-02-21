package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;

public class CompOffModel {
    LogWorkModel associatedLog;
    LocalDate validUpto;
    CompOffStatus status;

    public CompOffModel(LogWorkModel associatedLog, LocalDate validUpto, CompOffStatus status) {
        this.associatedLog = associatedLog;
        this.validUpto = validUpto;
        this.status = status;
    }
}
