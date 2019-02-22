package com.hashedin.hu.huLeaveTracker;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="employee")
@Access( AccessType.FIELD )
public class Employee {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "leavesBalance")
    private int leavesBalance;


    //    @JoinColumn(name="compoff_id")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "compoff_id")
    private Collection<CompOffModel> compOffBalance = new ArrayList<>();


    @Column(name = "joiningDate")
    private LocalDate joiningDate;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "isOnBlanketCoverage")
    private boolean isOnBlanketCoverageLeave;

    @Column(name = "numberOfChildren")
    private int numberOfChildren;

    @ElementCollection
    private List<LocalDate> optionalLeavesAvailed = new ArrayList<>();

    public Employee() {
    }

    public Employee(String name, int leavesBalance, LocalDate joiningDate, Gender gender) {
        this.name = name;
        this.leavesBalance = leavesBalance;
        this.compOffBalance = new ArrayList<>();
        this.joiningDate = joiningDate;
        this.gender = gender;
        this.isOnBlanketCoverageLeave = false;
        this.numberOfChildren = 0;
        this.optionalLeavesAvailed = new ArrayList<>();
    }

    public void setOptionalLeavesAvailed(List<LocalDate> optionalLeavesAvailed) {
        this.optionalLeavesAvailed = optionalLeavesAvailed;
    }

    public Employee(int id, String name, int leavesBalance, LocalDate joiningDate, Gender gender) {
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

    public List<LocalDate> getOptionalLeavesAvailed() {
        return optionalLeavesAvailed;
    }

    public void setOptionalLeavesAvailed(ArrayList<LocalDate> optionalLeavesAvailed) {
        this.optionalLeavesAvailed = optionalLeavesAvailed;
    }

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
                '}';
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }


    public Collection<CompOffModel> getCompOffBalance() {
        return compOffBalance;
    }

    public void setCompOffBalance(Collection<CompOffModel> compOffBalance) {
        this.compOffBalance = compOffBalance;
    }

    public List<CompOffModel> getValidCompOffBalance(LocalDate startDate) {

        ArrayList<CompOffModel> validCompOffBalance = new ArrayList<>();

        List<CompOffModel> compOffBalance = new ArrayList<>();

        compOffBalance.addAll(this.getCompOffBalance());

        for(int i=0; i<compOffBalance.size(); i++) {

            if(compOffBalance.get(i).getValidUpto().isAfter(startDate)
                    && compOffBalance.get(i).getStatus() == CompOffStatus.AVAILABLE) {

                validCompOffBalance.add(compOffBalance.get(i));
            }
        }
        return validCompOffBalance;
    }

    public long getValidCompOffBalanceSize(LocalDate startDate) {
        return getValidCompOffBalance(startDate).size();
    }

    public void updateCompOffBalance(LocalDate startDate, long numberOfLeavesRequested) {
        int count = 0;

        List<CompOffModel> compOffBalance = new ArrayList<>();
        compOffBalance.addAll(this.compOffBalance);
//        Collections.sort(this.compOffBalance, (CompOffModel compOffData1, CompOffModel compOff2) -> {
//            return (compOffData1.getValidUpto().isAfter(compOff2.getValidUpto())) ? 1 : 0;
//        });
        while(count != numberOfLeavesRequested) {
            if(startDate.isBefore(compOffBalance.get(count).getValidUpto())) {
                compOffBalance.get(count).setStatus(CompOffStatus.CLAIMED);
                count++;
            }
        }
    }


    public boolean hasWorkedForGivenDays(LocalDate startDate, int numberofDaysRequiredForWork) {
        long numberOfdaysWorked = ChronoUnit.DAYS.between(joiningDate, startDate);
        return numberOfdaysWorked >= numberofDaysRequiredForWork;
    }
}