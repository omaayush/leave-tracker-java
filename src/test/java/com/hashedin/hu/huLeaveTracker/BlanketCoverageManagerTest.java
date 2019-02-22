package com.hashedin.hu.huLeaveTracker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class BlanketCoverageManagerTest {

    private BlanketCoverageManager manager;

    public BlanketCoverageManagerTest() {
        this.manager = new BlanketCoverageManager();
    }

    @Test
    void testIfTheRequestPassesValidationForMaternityLEave() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(employee.id, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED);
        LeaveResponse actualResponse = this.manager.requestValidatorForBlanketCoverage(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, this.manager.requestValidatorForBlanketCoverage(request).reason);
    }

    @Test
    void testToFailMAternityRequestDueToWrongGender() {
        Employee employee = new Employee(112, "Male", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Male);
        LeaveRequest request = new LeaveRequest(employee.id, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE);
        LeaveResponse actualResponse = this.manager.requestValidatorForBlanketCoverage(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, this.manager.requestValidatorForBlanketCoverage(request).reason);
    }

    @Test
    void testToFailMaternityRequestDueToNotCompleting180Days() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2018, Month.JANUARY, 19), Gender.Female);
        LeaveRequest request = new LeaveRequest(employee.id, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResponse = this.manager.requestValidatorForBlanketCoverage(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, this.manager.requestValidatorForBlanketCoverage(request).reason);
    }

    @Test
    void testToFailMaternityRequestDueToAlreadyClaimedForMaxChildren() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2016, Month.JANUARY, 19), Gender.Female);
        employee.setNumberOfChildren(2);
        LeaveRequest request = new LeaveRequest(employee.id, TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.MATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        LeaveResponse actualResponse = this.manager.requestValidatorForBlanketCoverage(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, this.manager.requestValidatorForBlanketCoverage(request).reason);
    }
}