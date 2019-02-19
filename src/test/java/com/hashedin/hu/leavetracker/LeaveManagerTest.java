package com.hashedin.hu.leavetracker;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

public class LeaveManagerTest {

    @Test
    public void requestedDaysGreaterThanLeftLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1,5);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days());
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Number of leaves not permissible",response.getLeaveStatus(),LeaveStatus.ACCEPTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionTest(){
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1,5);
        LeaveRequest request=new LeaveRequest(employee, after4Days(),today());
        LeaveResponse response=manager.applyForLeave(request);
        //assertEquals("Exception Occurred",response);
    }

    @Test
    public void EmployeeLeavesLeftChangedLater(){
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(2,5);
        LeaveRequest request=new LeaveRequest(employee,today(),after4Days());
        employee.setGeneralLeaves(0);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Employee LeaveType changed later",response.getLeaveStatus(),LeaveStatus.REJECTED);
    }

    @Test
    public void requestedDaysGreaterThanLeftLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1,Gender.MALE,1,LocalDate.now(),5);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Number of leaves not permissible",response.getLeaveStatus(),LeaveStatus.ACCEPTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionTest(){
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1,5);
        LeaveRequest request=new LeaveRequest(employee, after4Days(),today(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        //assertEquals("Exception Occurred",response);
    }

    @Test
    public void EmployeeLeavesLeftChangedLater(){
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(2,5);
        LeaveRequest request=new LeaveRequest(employee,today(),after4Days());
        employee.setGeneralLeaves(0);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Employee LeaveType changed later",response.getLeaveStatus(),LeaveStatus.REJECTED);
    }
    private LocalDate today() {
        return LocalDate.now();
    }
    private LocalDate after4Days() {
    return LocalDate.now().plusDays(4);
    }


}