package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;

public class Holiday {
    String name;
    boolean isOptional;
    LocalDate groupedWith;

    public Holiday(String name, boolean isOptional, LocalDate groupedWith) {
        this.name = name;
        this.isOptional = isOptional;
        this.groupedWith = groupedWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public LocalDate getGroupedWith() {
        return groupedWith;
    }

    public void setGroupedWith(LocalDate groupedWith) {
        this.groupedWith = groupedWith;
    }
}
