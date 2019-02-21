package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;

public class CompOffModel {
    private LogWorkModel associatedLog;

    public LogWorkModel getAssociatedLog() {
        return associatedLog;
    }

    public void setAssociatedLog(LogWorkModel associatedLog) {
        this.associatedLog = associatedLog;
    }

    public LocalDate getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(LocalDate validUpto) {
        this.validUpto = validUpto;
    }

    public CompOffStatus getStatus() {
        return status;
    }

    public void setStatus(CompOffStatus status) {
        this.status = status;
    }

    private LocalDate validUpto;
    private CompOffStatus status;

    public CompOffModel(LogWorkModel associatedLog, LocalDate validUpto, CompOffStatus status) {
        this.associatedLog = associatedLog;
        this.validUpto = validUpto;
        this.status = status;
    }
}
