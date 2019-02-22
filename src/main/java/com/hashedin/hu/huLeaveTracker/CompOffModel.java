package com.hashedin.hu.huLeaveTracker;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class CompOffModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne
    LogWorkModel associatedLog = new LogWorkModel();

    @Column(name = "validUpto")
    LocalDate validUpto;

    @Column(name = "status")
    CompOffStatus status;

    public CompOffModel(LogWorkModel associatedLog, LocalDate validUpto, CompOffStatus status) {
        this.associatedLog = associatedLog;
        this.validUpto = validUpto;
        this.status = status;
    }
}
