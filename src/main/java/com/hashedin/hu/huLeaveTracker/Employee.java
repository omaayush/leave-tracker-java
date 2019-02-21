package com.hashedin.hu.huLeaveTracker;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

enum Gender {
    Male("Male"),
    Female("Female");

    private String gender;
    Gender(String gender) {
        this.gender = gender;
    }
}

@Resource
public class Employee {
    private int id;
    private String name;
    private int leavesBalance;
    private ArrayList<CompOffModel> compOffBalance;
    private LocalDate joiningDate;
    private Gender gender;
    private boolean isOnBlanketCoverageLeave;
    private int numberOfChildren;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeavesBalance() {
        return leavesBalance;
    }

    public void setLeavesBalance(int leavesBalance) {
        this.leavesBalance = leavesBalance;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isOnBlanketCoverageLeave() {
        return isOnBlanketCoverageLeave;
    }

    public void setOnBlanketCoverageLeave(boolean onBlanketCoverageLeave) {
        isOnBlanketCoverageLeave = onBlanketCoverageLeave;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public ArrayList<LocalDate> getOptionalLeavesAvailed() {
        return optionalLeavesAvailed;
    }

//    public void setOptionalLeavesAvailed(ArrayList<LocalDate> optionalLeavesAvailed) {
//        this.optionalLeavesAvailed = optionalLeavesAvailed;
//    }
//
//    public Comparator<? super CompOffModel> getCompareWrtDate() {
//        return compareWrtDate;
//    }
//
//    public void setCompareWrtDate(Comparator<? super CompOffModel> compareWrtDate) {
//        this.compareWrtDate = compareWrtDate;
//    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leavesBalance=" + leavesBalance +
                ", compOffBalance=" + compOffBalance +
                ", joiningDate=" + joiningDate +
                ", gender=" + gender +
                ", isOnBlanketCoverageLeave=" + isOnBlanketCoverageLeave +
                ", numberOfChildren=" + numberOfChildren +
                ", optionalLeavesAvailed=" + optionalLeavesAvailed +
                //", compareWrtDate=" + compareWrtDate +
                '}';
    }

    private ArrayList<LocalDate> optionalLeavesAvailed;


    Employee(int id, String name, int leavesBalance, LocalDate joiningDate, Gender gender) {
        this.id = id;
        this.name = name;
        this.leavesBalance = leavesBalance;
        this.compOffBalance = new ArrayList<>();
        this.joiningDate = joiningDate;
        this.gender = gender;
        this.isOnBlanketCoverageLeave = false;
        this.numberOfChildren = 0;
        this.optionalLeavesAvailed = new ArrayList<>();
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public ArrayList<CompOffModel> getCompOffBalance() {
        return compOffBalance;
    }

    public void setCompOffBalance(ArrayList<CompOffModel> compOffBalance) {
        this.compOffBalance = compOffBalance;
    }

//    public ArrayList<CompOffModel> getValidCompOffBalance(LocalDate startDate) {
//        ArrayList<CompOffModel> validCompOffBalance = new ArrayList<CompOffModel>();
//        for(int i=0; i<compOffBalance.size(); i++) {
//            if(compOffBalance.get(i).getValidUpto().isAfter(startDate) && compOffBalance.get(i).status == CompOffStatus.AVAILABLE) {
//                validCompOffBalance.add(compOffBalance.get(i));
//            }
//        }
//        return validCompOffBalance;
//    }

//    public long getValidCompOffBalanceSize(LocalDate startDate) {
//        return getValidCompOffBalance(startDate).size();
//    }
//
//    public void updateCompOffBalance(LocalDate startDate, long numberOfLeavesRequested) {
//        int count = 0;
//        Collections.sort(this.compOffBalance, compareWrtDate);
//        while(count != numberOfLeavesRequested) {
//            if(startDate.isBefore(this.compOffBalance.get(count).getValidUpto())) {
//                this.compOffBalance.get(count).status = CompOffStatus.CLAIMED;
//                count++;
//            }
//        }
//    }

//    private Comparator<? super CompOffModel> compareWrtDate = new Comparator<CompOffModel>() {
//        @Override
//        public int compare(CompOffModel compOffData1, CompOffModel compOff2) {
//            return (compOffData1.validUpto.isAfter(compOff2.validUpto)) ? 1 : 0;
//        }
//    };
//
//    public boolean hasWorkedForGivenDays(LocalDate startDate, int numberofDaysRequiredForWork) {
//        long numberOfdaysWorked = ChronoUnit.DAYS.between(joiningDate, startDate);
//        return numberOfdaysWorked >= numberofDaysRequiredForWork;
//    }
}
