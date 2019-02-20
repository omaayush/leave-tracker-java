package com.hashedin.hu.leavetracker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

//public class LeaveAccrualManager {
//    EmployeeStore employeeStore;
//    LeaveAccrualManager(EmployeeStore employeeStore)
//    {
//        this.employeeStore=employeeStore;
//    }
//
//    public void creditLeaves(LocalDate now)
//    {
//
//
//    }
//
//}

import java.time.LocalDate;
import java.util.ArrayList;

public class LeaveAccrualManager {

    EmployeeStore employeeStore;

    public LeaveAccrualManager(EmployeeStore employeeStore) {
        this.employeeStore = employeeStore;
    }

    public void creditLeavesPeriodically(LocalDate today) {
        ArrayList <Employee> allEmployees = employeeStore.getAllEmployees();
        for(int i=0; i<allEmployees.size(); i++) {
            allEmployees.get(i).generalLeaves +=
                    creditLeavesToEmployee(allEmployees.get(i), today);
        }
    }

    private int creditLeavesToEmployee(Employee employee, LocalDate today) {
        if(didEmployeeJoinTheSameMonth(employee, today) && didEmployeeJoinAfter15(employee)) {
            return 1;
        }
        return 2;
    }

    private boolean didEmployeeJoinAfter15(Employee employee) {
        return employee.dateOfJoining.getDayOfMonth() > 15;
    }

    private boolean didEmployeeJoinTheSameMonth(Employee employee, LocalDate today) {
        return employee.dateOfJoining.getYear() == today.getYear()
                && today.getMonthValue() - employee.dateOfJoining.getMonthValue() == 1;
    }
}
