package com.hashedin.hu.huLeaveTracker;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.LocalDate;


@Entity
@Table(name="compoff")
@Access( AccessType.FIELD )
public class CompOffModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    private int employee;

    private int associatedLogId;

    @Column(name = "validUpto")
    private LocalDate validUpto;

    @Column(name = "status")
    private CompOffStatus status;

    public CompOffModel() {}

    public CompOffModel(int employeeId, int associatedLog, LocalDate validUpto, CompOffStatus status) {
        this.employee = employeeId;
        this.associatedLogId = associatedLog;
        this.validUpto = validUpto;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssociatedLogId() {
        return associatedLogId;
    }

    public void setAssociatedLogId(int associatedLog) {
        this.associatedLogId = associatedLog;
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
}
