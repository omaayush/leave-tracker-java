package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
//import java.time.LocalDate;

public class LeavesStore {
    private ArrayList<LeaveGrant> leaves;
    private ArrayList<CompOffManager> compOffs;
    private HashSet<LocalDate> holidays;
    private ArrayList<OptionalHolidays> optionalHolidays;

    public LeavesStore() {
        leaves = new ArrayList<LeaveGrant>();
        holidays = new HashSet<LocalDate>();
        compOffs = new ArrayList<CompOffManager>();
        optionalHolidays=new ArrayList<OptionalHolidays>();
    }

    public ArrayList<LeaveGrant> getLeaves() {
        return leaves;
    }

    public void setLeaves(ArrayList<LeaveGrant> leaves) {
        this.leaves = leaves;
    }

    public boolean addLeave(LeaveGrant leaveGrant) {
        return this.leaves.add(leaveGrant);
    }

    public HashSet<LocalDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(HashSet<LocalDate> holidays) {
        this.holidays = holidays;
    }

    public boolean addHoliday(LocalDate holidayDate) {
        return this.holidays.add(holidayDate);
    }

    public ArrayList<CompOffManager> getCompOffs() {
        return compOffs;
    }

    public boolean addCompOff(CompOffManager compOff) {
        return this.compOffs.add(compOff);
    }

    public void setCompOffs(ArrayList<CompOffManager> compOffs) {
        this.compOffs = compOffs;
    }

//    public void updateCompOff(CompOffManager compOff){
//        int i = 0;
//        for (i=0; i< compOffs.size(); i++) {
//            if(compOffs.get(i).getId() == compOff.getId()) {
//                break;
//            }
//        }
//        compOffs.set(i, compOff);
//    }
    public ArrayList<OptionalHolidays> getOptionalHolidays() {
            return optionalHolidays;
    }

    public boolean addOptionalHoliday(OptionalHolidays optionalLeave) {
        return this.optionalHolidays.add(optionalLeave);
    }

    public void setOptionalHolidays(ArrayList<OptionalHolidays> optionalHoliday) {
        this.optionalHolidays = optionalHoliday;
    }

}
