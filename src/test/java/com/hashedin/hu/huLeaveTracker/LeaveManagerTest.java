package com.hashedin.hu.huLeaveTracker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LeaveManagerTest {
    private LeaveManager manager;
    private Employee employee;
    private LeaveRequest request;
    private CompOffManager compOffManager;
    private BlanketCoverageManager blanketCoverageManager;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveManagerTest() {
        this.manager = new LeaveManager(leaveRequestRepository,employeeRepository);
        this.compOffManager = new CompOffManager();
        this.employee = new Employee(103, "Aayush", 2, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        this.request = new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, null, null);
        this.blanketCoverageManager = new BlanketCoverageManager();
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

    @Test
    public void testIfTheLeaveBalanceIsPositive() {
        setRequest(new LeaveRequest(1, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.MAY ,20), LocalDate.of(2019, Month.MAY, 21)));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);

        assertEquals(leaveResponse.reason, manager.apply(this.request).reason);
    }

    @Test
    public void testIfTheLeaveBalanceIsNotPositive() {
        setEmployee(new Employee(103, "Aayush", 0, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.MAY ,20), LocalDate.of(2019, Month.MAY, 21)));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);

        assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    @Test
    public void testIfTheLeaveBalanceNotSufficient() {
        setEmployee(new Employee(103, "Aayush", 1, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.APRIL, 25)));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);

        assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    @Test
    public void testIfTheLeaveBalanceSufficient() {
        setEmployee(new Employee(103, "Aayush", 5, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.AUGUST, 12), LocalDate.of(2019, Month.AUGUST, 17)));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);

        assertEquals(leaveResponse.reason, manager.apply(this.request).reason);
    }

    //Test cases for overlapping Leaves Conditions

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 20 feb to 26 feb
    @Test
    public void testForOverlappingLeavesWhenStartDateIsBetweenAlreadyApprovedDuration() {
        setEmployee(new Employee(103, "Aayush", 10, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);

        LeaveResponse leaveResponse = manager.apply((this.request));

        // setting the start day
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today().plusDays(3), today().plusDays(8)));


        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 16 feb to 23 feb
    @Test
    public void testForOverlappingLeavesWhenStartDateIsBeforeAlreadyApprovedDuration() {
        setEmployee(new Employee(103, "Ankit", 10, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);

        LeaveResponse leaveResponse = manager.apply((this.request));

        // setting the start day
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(5)));

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // It covers the case when the start date is before the already assigned start date and the end date is
    // after the end date of already assigned duration
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 16 feb to 26 feb
    @Test
    public void testForOverlappingLeavesWhenRequestDurationContainsPreviousDuration() {
        setEmployee(new Employee(103, "Aayush", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7)));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);

        LeaveResponse leaveResponse = manager.apply((this.request));

        // setting the start day
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(8)));

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    // This test case tests for assigning a leave on a pubic holiday
    // for a single day holiday on a public holiday
    @Test
    public void testToAssignSingleLeaveOnAPublicHoliday() {
        setEmployee(new Employee(103, "Ankit", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male));
        setRequest(new LeaveRequest(103, TypeOfLeaves.OUT_OF_OFFICE,
                LocalDate.of(2019, Month.AUGUST, 15), //start date fixed to a holiday
                LocalDate.of(2019, Month.AUGUST, 15))); // end date fixed to a holiday

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.PUBLIC_HOLIDAY);

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(this.request).statusOfLeaveRequest);
    }

    // test to check for a positive compoff request
    @Test
    public void testToApplyForAPositiveCompOffRequest() {
        // adding compoff to employee balance
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork);

        LeaveRequest leaveRequest = new LeaveRequest(103, TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.FEBRUARY, 1), LocalDate.of(2019, Month.FEBRUARY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);
        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(leaveRequest).statusOfLeaveRequest);
    }

    // test to check for a negative compoff request due to insufficient compoff balance
    @Test
    public void testToApplyForANegativeCompOffRequest() {
        // adding compoff to employee balance
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork);

        LeaveRequest leaveRequest = new LeaveRequest(103, TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.FEBRUARY, 1), LocalDate.of(2019, Month.FEBRUARY, 3));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);

        LeaveResponse actualResonse = this.manager.apply(leaveRequest);
        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test to check for a negative compoff request due to insufficient compoff balance
    @Test
    public void testToApplyForANegativeCompOffRequestDueToCompoffExpiry() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork);

        LeaveRequest leaveRequest = new LeaveRequest(103, TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.MARCH, 1), LocalDate.of(2019, Month.MARCH, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);
        LeaveResponse actualResonse = this.manager.apply(leaveRequest);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test to check if the maternity leaves can be sanctioned
    @Test
    public void testIfMaternityLeaveIsSanctioned() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);
        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED);

        assertEquals(expectedResponse.reason, this.manager.apply(request).reason);
    }

    @Test
    public void testToFailMAternityRequestDueToWrongGender() {
        Employee employee = new Employee(112, "Male", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Male);
        LeaveRequest request = new LeaveRequest(112, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResponse.reason);
    }

    @Test
    public void testToFailPaternityRequestDueToWrongGender() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(112, TypeOfLeaves.PATERNITY, LocalDate.of(2018, Month.MAY, 20), LocalDate.of(2018, Month.MAY, 30));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResponse.reason);
    }

    @Test
    public void testToFailMaternityRequestDueToNotCompleting180Days() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2018, Month.JANUARY, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(112, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    @Test
    public void testToFailMaternityRequestDueToAlreadyAvailedMaxNumberOfTimes() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2016, Month.JANUARY, 19), Gender.Female);

        employee.setNumberOfChildren(2);

        LeaveRequest request = new LeaveRequest(112, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.MATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    @Test
    public void testToSetEndDateOfMaternityRequest() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);
        LeaveResponse expectedResponse = this.manager.apply(request);

        assertEquals(Month.NOVEMBER, request.endDate.getMonth());
    }

    @Test
    public void testToRequestPaternityLeaveForMoreThan10Leaves() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.PATERNITY, LocalDate.of(2018, Month.APRIL, 20), LocalDate.of(2018, Month.JUNE, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(StatusOfLeaveRequest.REJECTED, actualResponse.statusOfLeaveRequest);
        assertEquals(ReasonsForLeaveResponse.PATERNITY_LEAVE_CANNOT_EXCEED_10, actualResponse.reason);
    }

    @Test
    public void testToRequestPaternityLeaveForMoreThanMaxChildren() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.PATERNITY, LocalDate.of(2018, Month.MAY, 20), LocalDate.of(2018, Month.JUNE, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(StatusOfLeaveRequest.REJECTED, actualResonse.statusOfLeaveRequest);
        assertEquals(ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN, actualResonse.reason);
    }

    @Test
    public void testSabbaticalApplication() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2014, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.APPROVED_LEAVE);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // test to fail sabbatical request for requesting before completing 2 years of service
    @Test
    public void testSabbaticalFailForRequestBefore2Years() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // test to fail the sabbatical request due to not requesting before 45 days

    @Test
    public void testSabbaticalFailForRequestNot45DaysBefore() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.MARCH, 10), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // Optional Leaves Testing
    @Test
    public void testOptionalLeaveSuccess() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 20));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.APPROVED_LEAVE);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test case to reject the optional leave request because the alternate optional request was availed

    @Test
    public void testOptionalLeaveFailureOtherOptionalLeaveAvailed() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 20));

        LeaveResponse actualResonse;

        this.manager.apply(request);

        request = new LeaveRequest(110, TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.JUNE, 18), LocalDate.of(2019, Month.JUNE, 18));

        actualResonse = this.manager.apply(request);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.OPTIONAL_LEAVE_ALREADY_AVAILED);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // Optional Leaves Testing to reject if number of leaves is greater than 1
    @Test
    public void testOptionalLeaveFailedForLeavesGreaterThan1() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        LeaveRequest request = new LeaveRequest(110, TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 25));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.OPTIONAL_LEAVES_GREATER_THAN_1);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    private LocalDate dayAfterTomorrow() {
        return today().plusDays(2);
    }

    private LocalDate today() {
        return LocalDate.of(2019, Month.MAY, 20);
    }
}
