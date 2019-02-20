package com.hashedin.hu.leavetracker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static junit.framework.TestCase.assertEquals;


public class LeaveAccrualManagerTest {

    private EmployeeStore employeeStore;
    public LeaveAccrualManagerTest() {
        this.employeeStore = new EmployeeStore();
        Employee employee1 = new Employee(101, Gender.MALE, 2, LocalDate.of(2018, Month.NOVEMBER, 15),2);
        Employee employee2 = new Employee(102, Gender.MALE, 2, LocalDate.of(2019, Month.JANUARY, 10), 2);
        Employee employee3 = new Employee(103,Gender.MALE, 2, LocalDate.of(2019, Month.JANUARY, 18), 2);
        Employee employee4 = new Employee(103,Gender.MALE, 2, LocalDate.of(2019, Month.JANUARY, 18), 2);
        Employee employee5 = new Employee(103,Gender.MALE, 2, LocalDate.of(2019, Month.JANUARY, 18), 2);
        employeeStore.addEmployeeToStore(employee1);
        employeeStore.addEmployeeToStore(employee2);
        employeeStore.addEmployeeToStore(employee3);
        employeeStore.addEmployeeToStore(employee4);
        employeeStore.addEmployeeToStore(employee5);
    }

    // Create test to check leave accrual for an employee who joined before accrual month
    @Test
    public void ForAnEmployeeWhoJoinedYearBefore () {
        LeaveAccrualManager leaveAccrualManager = new LeaveAccrualManager(this.employeeStore);
        leaveAccrualManager.creditLeavesPeriodically(LocalDate.of(2019, Month.FEBRUARY, 1));
        assertEquals(4, employeeStore.getAllEmployees().get(0).generalLeaves);
        assertEquals(4, employeeStore.getAllEmployees().get(1).generalLeaves);
        assertEquals(3, employeeStore.getAllEmployees().get(2).generalLeaves);
        assertEquals(3, employeeStore.getAllEmployees().get(3).generalLeaves);
        assertEquals(3, employeeStore.getAllEmployees().get(4).generalLeaves);

    }
}