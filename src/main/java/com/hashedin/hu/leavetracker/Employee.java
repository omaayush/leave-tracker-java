package com.hashedin.hu.leavetracker;

public class Employee {
    int id;
    int leavesLeft;

    public Employee(int id, int leavesLeft) {
        this.id = id;
        this.leavesLeft = leavesLeft;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeavesLeft() {
        return leavesLeft;
    }

    public void setLeavesLeft(int leavesLeft) {
        this.leavesLeft = leavesLeft;
    }
}
