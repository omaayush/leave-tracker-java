package com.hashedin.hu.leavetracker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static junit.framework.TestCase.assertEquals;

public class LeaveManagerTest {
    LeaveManager manager;
    Employee employee;
    LeaveRequest request;
    CompOffManager compOffManager;
    LeaveResponse leaveResponse;

    public LeaveManagerTest() {
        this.manager = new LeaveManager();
        this.compOffManager = new CompOffManager();
        this.employee = new Employee(100, Gender.MALE, 2, LocalDate.of(2019, Month.APRIL, 15),5);
        this.request = new LeaveRequest(employee, today(), after4Days(),LeaveType.GENERAL);
        this.leaveResponse=manager.applyForLeave(request);
    }

    @Test
    public void DateConflict() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,dateOfJoining(),5);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Number of leaves not permissible",response.leaveStatus,LeaveStatus.ACCEPTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionText() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,dateOfJoining(),5);
        LeaveRequest request=new LeaveRequest(employee,after4Days(), today(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Number of leaves not permissible",response.leaveStatus,LeaveStatus.REJECTED);
    }

//-----------------general leaves-----------------------
    @Test
    public void generalLeaveMaleInsufficientLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,dateOfJoining(),3);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals(response.leaveResponses.toString(),response.leaveStatus,LeaveStatus.REJECTED);
    }

    @Test
    public void generalLeaveMaleSufficientLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,dateOfJoining(),5);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.GENERAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals(response.leaveResponses.toString(),response.leaveStatus,LeaveStatus.ACCEPTED);
    }


    //-----------------sabbatical leaves-----------------------
    @Test
    public void sabbaticalLeaveMaleInsufficientLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,LocalDate.of(2019,2,12),3);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.SABBATICAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals(response.leaveResponses.toString(),response.leaveStatus,LeaveStatus.REJECTED);
    }

    @Test
    public void sabbaticalLeaveMaleSufficientLeaves() {
        LeaveManager manager=new LeaveManager();
        Employee employee=new Employee(1, Gender.MALE,2,LocalDate.of(2012,2,12),5);
        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.SABBATICAL);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals(response.leaveResponses.toString(),response.leaveStatus,LeaveStatus.ACCEPTED);
    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void exceptionTest(){
//        LeaveManager manager=new LeaveManager();
//        Employee employee=new Employee(1,5);
//        LeaveRequest request=new LeaveRequest(employee, after4Days(),today());
//        LeaveResponse response=manager.applyForLeave(request);
//        //assertEquals("Exception Occurred",response);
//    }
//
//    @Test
//    public void EmployeeLeavesLeftChangedLater(){
//        LeaveManager manager=new LeaveManager();
//        Employee employee=new Employee(2,5);
//        LeaveRequest request=new LeaveRequest(employee,today(),after4Days());
//        employee.setGeneralLeaves(0);
//        LeaveResponse response=manager.applyForLeave(request);
//        assertEquals("Employee LeaveType changed later",response.getLeaveStatus(),LeaveStatus.REJECTED);
//    }
//
//    @Test
//    public void generalLeaveMaleDateConflict() {
//        LeaveManager manager=new LeaveManager();
//        Employee employee=new Employee(1,Gender.MALE,1,LocalDate.now(),5);
//        LeaveRequest request=new LeaveRequest(employee,today(), after4Days(),LeaveType.GENERAL);
//        LeaveResponse response=manager.applyForLeave(request);
//        assertEquals("Number of leaves not permissible",response.getLeaveStatus(),LeaveStatus.ACCEPTED);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void exceptionTest(){
//        LeaveManager manager=new LeaveManager();
//        Employee employee=new Employee(1,5);
//        LeaveRequest request=new LeaveRequest(employee, after4Days(),today(),LeaveType.GENERAL);
//        LeaveResponse response=manager.applyForLeave(request);
//        //assertEquals("Exception Occurred",response);
//    }
//
//    @Test
//    public void EmployeeLeavesLeftChangedLater(){
//        LeaveManager manager=new LeaveManager();
//        Employee employee=new Employee(2,5);
//        LeaveRequest request=new LeaveRequest(employee,today(),after4Days());
//        employee.setGeneralLeaves(0);
//        LeaveResponse response=manager.applyForLeave(request);
//        assertEquals("Employee LeaveType changed later",response.getLeaveStatus(),LeaveStatus.REJECTED);
//    }


//    @Test()
//    public void testIfTheLeaveBalanceIsPositive() {
//        assertEquals(leaveResponse.leaveResponses.toString(),leaveResponse.leaveStatus,LeaveStatus.ACCEPTED);
//    }

//    @Test()
//    public void testIfTheLeaveBalanceIsNotPositive() {
//        setEmployee(new Employee(2,Gender.MALE, 0,LocalDate.of(2019, Month.APRIL, 15),0));
//        assertEquals(leaveResponse.leaveResponses.toString(),leaveResponse.leaveStatus,LeaveStatus.REJECTED);
//    }

//    @Test
//    void testIfTheLeaveBalanceNotSufficient() {
//        setEmployee(new Employee(103, "Ankit", 1, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.APRIL, 25)));
//
//        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);
//
//        Assertions.assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    @Test
//    void testIfTheLeaveBalanceSufficient() {
//        setEmployee(new Employee(103, "Ankit", 5, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));
//
//        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
//                ReasonsForLeaveResponse.APPROVED_LEAVE);
//
//        Assertions.assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    //Test cases for overlapping Leaves Conditions
//
//    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
//    // For eg. the approved leaves are: 18 feb to 25 feb
//    // the new requested leaves are 20 feb to 26 feb
//    @Test
//    void testforOverlappingLeavesWhenStartDateIsBetweenAlreadyApprovedDuration() {
//        setEmployee(new Employee(103, "Ankit", 10, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
//
//        LeaveResponse leaveResponse = manager.apply((this.request));
//
//        // setting the start day
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today().plusDays(3), today().plusDays(8)));
//
//
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
//    // For eg. the approved leaves are: 18 feb to 25 feb
//    // the new requested leaves are 16 feb to 23 feb
//    @Test
//    void testforOverlappingLeavesWhenStartDateIsBeforeAlreadyApprovedDuration() {
//        setEmployee(new Employee(103, "Ankit", 10, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
//
//        LeaveResponse leaveResponse = manager.apply((this.request));
//
//        // setting the start day
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(5)));
//
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
//    // It covers the case when the start date is before the already assigned start date and the end date is
//    // after the end date of already assigned duration
//    // For eg. the approved leaves are: 18 feb to 25 feb
//    // the new requested leaves are 16 feb to 26 feb
//    @Test
//    void testforOverlappingLeavesWhenRequestDurationContainsThePreviousDuration() {
//        setEmployee(new Employee(103, "Ankit", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
//
//        LeaveResponse leaveResponse = manager.apply((this.request));
//
//        // setting the start day
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(8)));
//
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    // This test case tests for assigning a leave on a pubic holiday
//    // for a single day holiday on a public holiday
//    @Test
//    void testToAssignSingleLeaveOnAPublicHoliday() {
//        setEmployee(new Employee(103, "Ankit", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
//        setRequest(new LeaveRequest(employee, TypeOfLeaves.OUT_OF_OFFICE,
//                LocalDate.of(2019, Month.FEBRUARY, 20), //start date fixed to a holiday
//                LocalDate.of(2019, Month.FEBRUARY, 20))); // end date fixed to a holiday
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.PUBLIC_HOLIDAY);
//
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
//    }
//
//    // test to check for a positive compoff request
//    @Test
//    void testToApplyForAPositiveCompOffRequest() {
//        // adding compoff to employee balance
//        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
//        this.compOffManager.putLogWork(this.employee, logWork);
//
//        this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork);
//
//        LeaveRequest leaveRequest = new LeaveRequest(this.employee, TypeOfLeaves.COMP_OFF, LocalDate.now(), LocalDate.now());
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
//                ReasonsForLeaveResponse.APPROVED_LEAVE);
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(leaveRequest).statusOfLeaveRequest);
//    }
//
//    // test to check for a negative compoff request due to insufficient compoff balance
//    @Test
//    void testToApplyForANegativeCompOffRequest() {
//        // adding compoff to employee balance
//        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
//        this.compOffManager.putLogWork(this.employee, logWork);
//
//        this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork);
//
//        LeaveRequest leaveRequest = new LeaveRequest(this.employee, TypeOfLeaves.COMP_OFF, LocalDate.now(), LocalDate.now().plusDays(3));
//
//        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
//                ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);
//        Assertions.assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(leaveRequest).statusOfLeaveRequest);
//        Assertions.assertEquals(expectedResponse.reason, manager.apply(leaveRequest).reason);
//    }

    private LocalDate today() {
        return LocalDate.now();
    }
    private LocalDate after4Days() {
    return LocalDate.now().plusDays(4);
    }
    private LocalDate dateOfJoining() {
        return LocalDate.of(2012,1,1);
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public LeaveRequest getRequest() {
        return request;
    }
    public void setRequest(LeaveRequest request) {
        this.request = request;
    }

}