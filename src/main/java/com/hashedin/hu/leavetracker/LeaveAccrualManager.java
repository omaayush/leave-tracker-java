package com.hashedin.hu.leavetracker;

//import java.util.HashSet;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
//import static java.util.concurrent.TimeUnit.SECONDS;

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

    private EmployeeStore employeeStore;

    public LeaveAccrualManager(EmployeeStore employeeStore) {
        this.employeeStore = employeeStore;
    }

    public void creditLeavesPeriodically(LocalDate today) {
        ArrayList <Employee> allEmployees = employeeStore.getAllEmployees();
        for(int i=0; i<allEmployees.size(); i++) {
            allEmployees.get(i).setGeneralLeaves(
                    allEmployees.get(i).getGeneralLeaves()
                            +creditLeavesToEmployee(allEmployees.get(i),
                            today));
        }
//        HashSet<Employee> allEmployees = employeeStore.getAllEmployees();
//        for(int i=0; i<allEmployees.size(); i++) {
//            allEmployees(i).generalLeaves +=
//                    creditLeavesToEmployee(allEmployees.get(i), today);
//        }
    }

    private int creditLeavesToEmployee(Employee employee, LocalDate today) {
        if(didEmployeeJoinTheSameMonth(employee, today) && didEmployeeJoinAfter15(employee)) {
            return 1;
        }
        return 2;
    }

    private boolean didEmployeeJoinAfter15(Employee employee) {
        return employee.getDateOfJoining().getDayOfMonth() > 15;
    }

    private boolean didEmployeeJoinTheSameMonth(Employee employee, LocalDate today) {
        return employee.getDateOfJoining().getYear() == today.getYear()
                && today.getMonthValue() - employee.getDateOfJoining().getMonthValue() == 1;
    }
}
