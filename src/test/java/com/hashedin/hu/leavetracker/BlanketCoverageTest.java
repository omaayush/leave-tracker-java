package com.hashedin.hu.leavetracker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static junit.framework.TestCase.assertEquals;


public class BlanketCoverageTest {

    private BlanketCoverage manager;
    Employee employee;
    LeaveRequest leaveRequest;
    LeaveResponse leaveResponse;

    public BlanketCoverageTest() {
        this.manager = new BlanketCoverage();
        this.employee = new Employee(110, Gender.FEMALE, 0, LocalDate.of(2016, Month.AUGUST, 19),0);
        this.leaveRequest = new LeaveRequest(employee, LocalDate.of(2018, Month.MAY, 20), null,LeaveType.MATERNITY);
        this.leaveResponse=manager.ifBlanketCoverage(leaveRequest);

    }

    @Test
    public void maternityLeaveValidation() {
        assertEquals(LeaveResponses.VALID_BLANKET_COVERAGE.toString(),
                leaveResponse.leaveStatus,
                LeaveStatus.ACCEPTED);
        }


}