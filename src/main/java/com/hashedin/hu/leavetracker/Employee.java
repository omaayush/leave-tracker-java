package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee {
    int id;
    Gender gender;
    int child;
    LocalDate dateOfJoining;
    long generalLeaves;
    long compOffLeaves;
    long sabaticalLeaves;
    long maternalLeaves;
    long paternalLeaves;

    public Employee(int id, Gender gender, int child, LocalDate dateOfJoining, long generalLeaves) {
        this.id = id;
        this.gender = gender;
        this.child = child;
        this.dateOfJoining = dateOfJoining;
        this.generalLeaves = generalLeaves;
        this.maternalLeaves=0;
        this.paternalLeaves=0;
        if(this.gender==Gender.FEMALE)
        {
            if(this.child==0 || this.child==1)
                this.maternalLeaves=180;
        }
        else
            {
                if(this.child==0 || this.child==1)
                    this.maternalLeaves=30;
        }
    }

    public long getSabaticalLeaves() {
        return sabaticalLeaves;
    }

    public void setSabaticalLeaves(long sabaticalLeaves) {
        this.sabaticalLeaves = sabaticalLeaves;
    }

    public long getMaternalLeaves() {
        return maternalLeaves;
    }

    public void setMaternalLeaves(long maternalLeaves) {
        this.maternalLeaves = maternalLeaves;
    }

    public long getPaternalLeaves() {
        return paternalLeaves;
    }

    public void setPaternalLeaves(long paternalLeaves) {
        this.paternalLeaves = paternalLeaves;
    }

    public void setChild(int child) {    this.child = child;    }

    public int getChild() {  return this.child;  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGeneralLeaves() {
        return generalLeaves;
    }

    public void setGeneralLeaves(long generalLeaves) {
        this.generalLeaves = generalLeaves;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public long getCompOffLeaves() {
        return compOffLeaves;
    }

    public void setCompOffLeaves(long compOffLeaves) {
        this.compOffLeaves = compOffLeaves;
    }

    public long experience(){
        return ChronoUnit.YEARS.between(this.getDateOfJoining(),new LocalDate.now());
        }

}
