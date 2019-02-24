package com.hashedin.hu.huLeaveTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class LeaveManagerTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LogWorkRepository logWorkRepository;

    @Autowired
    private RepositoryCompOff compOffRepository;

    private LogWorkStore logWorkStore;

    private EmployeeService employeeService;

    private LeaveManager manager;

    private Employee employee;
    private LeaveRequest request;

    private ManagerCompOff compOffManager;


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee (Employee employee) {
        this.employee = employee;
    }

    public LeaveRequest getRequest() {
        return request;
    }

    public void setRequest(LeaveRequest request) {
        this.request = request;
    }

    @BeforeEach
    public void setup() {
        employeeService = new EmployeeService(employeeRepository);

        employee = new Employee(1, "Aayush", 10, LocalDate.of(2019, Month.MAY, 12), Gender.Male);
        Employee employee2 = new Employee(2, "Aayush", 0, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        Employee employee3 = new Employee(3, "Aayush", 1, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);

        employeeService.addEmployee(employee);
        employeeService.addEmployee(employee2);
        employeeService.addEmployee(employee3);

        manager = new LeaveManager(leaveRequestRepository, employeeRepository);
        logWorkStore = new LogWorkStore(logWorkRepository);
        compOffManager = new ManagerCompOff(logWorkStore, employeeService, compOffRepository);
    }

    @Test
    void TheStartDateIsNull() {
        Employee employee = employeeRepository.findById(1).get();

        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, null, null);
        LeaveResponse response = new LeaveResponse();

        assertThrows(IllegalArgumentException.class, () -> {
            manager.apply(request);
        });

    }

    @Test
    void TheStartDateIsAfterEndDate() {
        Employee employee = new Employee(2, "Aayush", 10, LocalDate.of(2019, Month.MAY, 12), Gender.Male);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, dayAfterTomorrow(), today());
        assertThrows(IllegalArgumentException.class, () -> {
            manager.apply(request);
        });
    }

    @Test
    void TheLeaveBalanceIsPositive() {
        Employee employee = new Employee(3, "Aayush", 10, LocalDate.of(2019, Month.MAY, 12), Gender.Male);
        employeeService.addEmployee(employee);

        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.MAY ,20), LocalDate.of(2019, Month.MAY, 21));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);


        assertEquals(leaveResponse.reason, manager.apply(request).reason);
    }

    @Test
    void IfTheLeaveBalanceIsNotPositive() {
        LeaveRequest request = new LeaveRequest(2, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.MAY ,20), LocalDate.of(2019, Month.MAY, 21));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);

        assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    @Test
    void IfTheLeaveBalanceNotSufficient() {
        Employee employee = new Employee(3, "Aayush", 1, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.APRIL, 25));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);

        assertEquals(leaveResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    @Test
    void TheLeaveBalanceSufficient() {
        LeaveRequest request = new LeaveRequest(1, TypeOfLeaves.OUT_OF_OFFICE, LocalDate.of(2019, Month.AUGUST, 12), LocalDate.of(2019, Month.AUGUST, 17));

        LeaveResponse leaveResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);

        assertEquals(leaveResponse.reason, manager.apply(request).reason);
    }

    //Test cases for overlapping Leaves Conditions

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 20 feb to 26 feb
    @Test
    void OverlappingLeavesWhenStartDateIsBetweenAlreadyApprovedDuration() {
        LeaveRequest request = new LeaveRequest(1, TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);

        LeaveResponse leaveResponse = manager.apply((request));

        // setting the start day
        request = new LeaveRequest(1, TypeOfLeaves.OUT_OF_OFFICE, today().plusDays(3), today().plusDays(8));


        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 16 feb to 23 feb
    @Test
    void OverlappingLeavesWhenStartDateIsBeforeAlreadyApprovedDuration() {
        Employee employee = new Employee(1, "Aayush", 10, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        LeaveRequest request =new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);


        LeaveResponse leaveResponse = manager.apply((request));

        // setting the start day
        request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(5));

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    // test case to overlap leaves and getting the rejected status even though the leave balance is sufficient
    // It covers the case when the start date is before the already assigned start date and the end date is
    // after the end date of already assigned duration
    // For eg. the approved leaves are: 18 feb to 25 feb
    // the new requested leaves are 16 feb to 26 feb
    @Test
    void OverlappingLeavesWhenRequestDurationContainsPreviousDuration() {
        Employee employee = new Employee(1, "Aayush", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, today(), today().plusDays(7));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
        LeaveResponse leaveResponse = manager.apply((request));

        // setting the start day
        request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE, today().minusDays(2), today().plusDays(8));

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    // This test case tests for assigning a leave on a pubic holiday
    // for a single day holiday on a public holiday
    @Test
    void AssignSingleLeaveOnAPublicHoliday() {
        Employee employee = new Employee(1, "Aayush", 15, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OUT_OF_OFFICE,
                LocalDate.of(2019, Month.AUGUST, 15), //start date fixed to a holiday
                LocalDate.of(2019, Month.AUGUST, 15)); // end date fixed to a holiday

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.PUBLIC_HOLIDAY);

        assertEquals(expectedResponse.statusOfLeaveRequest, manager.apply(request).statusOfLeaveRequest);
    }

    // test to check for a positive compoff request
    @Test
    void ApplyForAPositiveCompOffRequest() {
        // adding compoff to employee balance
        Employee employee = this.employeeRepository.findById(1).get();
        LogWorkModel logWork = new LogWorkModel(1,LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));

        this.compOffManager.putLogWork(employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(employee, logWork.getId());

        LeaveRequest leaveRequest = new LeaveRequest(1, TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.FEBRUARY, 1), LocalDate.of(2019, Month.FEBRUARY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED,
                ReasonsForLeaveResponse.APPROVED_LEAVE);


        assertEquals(expectedResponse.reason, manager.apply(leaveRequest).reason);
    }

    // test to check for a negative compoff request due to insufficient compoff balance
    @Test
    void ApplyForANegativeCompOffRequest() {
        // adding compoff to employee balance
        Employee employee = this.employeeRepository.findById(1).get();
        // adding compoff to employee balance
        LogWorkModel logWork = new LogWorkModel(employee.getId(), LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(employee, logWork.getId());

        LeaveRequest leaveRequest = new LeaveRequest(employee.getId(), TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.FEBRUARY, 1), LocalDate.of(2019, Month.FEBRUARY, 3));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);

        LeaveResponse actualResonse = this.manager.apply(leaveRequest);


        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test to check for a negative compoff request due to insufficient compoff balance
    @Test
    void ApplyForANegativeCompOffRequestDueToCompoffExpiry() {
        // adding compoff to employee balance
        Employee employee = this.employeeRepository.findById(1).get();
        LogWorkModel logWork = new LogWorkModel(employee.getId(),LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(employee, logWork);

        this.compOffManager.addingCompOffToCompOffBalance(employee, logWork.getId());

        LeaveRequest leaveRequest = new LeaveRequest(employee.getId(), TypeOfLeaves.COMP_OFF, LocalDate.of(2019, Month.MARCH, 1), LocalDate.of(2019, Month.MARCH, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED,
                ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);

        LeaveResponse actualResonse = this.manager.apply(leaveRequest);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test to check if the maternity leaves can be sanctioned
    @Test
    void MaternityLeaveIsSanctioned() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        employeeService.addEmployee(employee);

        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);
        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED);

        assertEquals(expectedResponse.reason, manager.apply(request).reason);
    }

    @Test
    void FailMAternityRequestDueToWrongGender() {
        Employee employee = new Employee(112, "Male", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Male);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResponse.reason);
    }

    @Test
    void FailPaternityRequestDueToWrongGender() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        employeeService.addEmployee(employee);

        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.PATERNITY, LocalDate.of(2018, Month.MAY, 20), LocalDate.of(2018, Month.MAY, 30));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResponse.reason);
    }

    @Test
    void FailMaternityRequestDueToNotCompleting180Days() {
        Employee employee = new Employee(112, "Female", 0, LocalDate.of(2018, Month.JANUARY, 19), Gender.Female);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }



    @Test
    void SetEndDateOfMaternityRequest() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);
        LeaveResponse expectedResponse = this.manager.apply(request);

        assertEquals(Month.NOVEMBER, request.getEndDate().getMonth());
    }

    @Test
    void RequestPaternityLeaveForMoreThan10Leaves() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.PATERNITY, LocalDate.of(2018, Month.APRIL, 20), LocalDate.of(2018, Month.JUNE, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        LeaveResponse actualResponse = this.manager.apply(request);

        assertEquals(StatusOfLeaveRequest.REJECTED, actualResponse.statusOfLeaveRequest);
        assertEquals(ReasonsForLeaveResponse.PATERNITY_LEAVE_CANNOT_EXCEED_10, actualResponse.reason);
    }



    @Test
    void SabbaticalApplication() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2014, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.APPROVED_LEAVE);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // test to fail sabbatical request for requesting before completing 2 years of service
    @Test
    void SabbaticalFailForRequestBefore2Years() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.APRIL, 20), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // test to fail the sabbatical request due to not requesting before 45 days

    @Test
    void SabbaticalFailForRequestNot45DaysBefore() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employee.setNumberOfChildren(3);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.SABBATICAL, LocalDate.of(2019, Month.MARCH, 10), LocalDate.of(2019, Month.JULY, 1));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
    }

    // Optional Leaves Testing
    @Test
    void OptionalLeaveSuccess() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 20));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.APPROVED_LEAVE);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // test case to reject the optional leave request because the alternate optional request was availed

    @Test
    void OptionalLeaveNotAvailedOtherOptionalLeaveAvailed() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 20));

        LeaveResponse actualResonse;

        this.manager.apply(request);

        request = new LeaveRequest(employee.getId(), TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.JUNE, 18), LocalDate.of(2019, Month.JUNE, 18));

        actualResonse = this.manager.apply(request);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.OPTIONAL_LEAVE_ALREADY_AVAILED);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    // Optional Leaves Testing to reject if number of leaves is greater than 1
    @Test
    void OptionalLeaveFailedForLeavesGreaterThan1() {
        Employee employee = new Employee(110, "Male", 0, LocalDate.of(2018, Month.AUGUST, 19), Gender.Male);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.OPTIONAL_LEAVE, LocalDate.of(2019, Month.FEBRUARY, 20), LocalDate.of(2019, Month.FEBRUARY, 25));

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.REJECTED, ReasonsForLeaveResponse.OPTIONAL_LEAVES_GREATER_THAN_1);
        LeaveResponse actualResonse = this.manager.apply(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResonse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, actualResonse.reason);
    }

    @Test
    void TheRequestPassesValidationForMaternityLEave() {
        Employee employee = new Employee(110, "Lady", 0, LocalDate.of(2016, Month.AUGUST, 19), Gender.Female);
        employeeService.addEmployee(employee);
        LeaveRequest request = new LeaveRequest(employee.getId(), TypeOfLeaves.MATERNITY, LocalDate.of(2018, Month.MAY, 20), null);

        LeaveResponse expectedResponse = new LeaveResponse(StatusOfLeaveRequest.APPROVED, ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED);
        LeaveResponse actualResponse = this.manager.requestValidatorForBlanketCoverage(request);

        assertEquals(expectedResponse.statusOfLeaveRequest, actualResponse.statusOfLeaveRequest);
        assertEquals(expectedResponse.reason, this.manager.requestValidatorForBlanketCoverage(request).reason);
    }


//

    private LocalDate dayAfterTomorrow() {
        return today().plusDays(2);
    }

    private LocalDate today() {
        return LocalDate.of(2019, Month.MAY, 20);
    }
}

