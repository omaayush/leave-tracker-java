package com.hashedin.hu.huLeaveTracker;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class CompOffManagerTest {

    Employee employee;
    LeaveRequest request;
    CompOffManager manager;

    public CompOffManagerTest() {
        this.manager = new CompOffManager();
        this.employee = new Employee(103, "Ankit", 2, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        this.request = new LeaveRequest(employee.id, TypeOfLeaves.OUT_OF_OFFICE, null, null);
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

    // comp off conditions

    @Test
    public void testIfCompOffLogWorkGetsCreated() {

        // creates a new log work and a store
        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
        LogWorkStore logWorkStore = new LogWorkStore();

        // checks if the log work gets added to store
        assertEquals(LogWorkStatus.LOG_WORK_ADDED, logWorkStore.putLogWorkInStore(this.employee, logWork));
    }


    // positive test case for checking if the log hours are greater than comp off hours
    @Test
    public void testIfLogHoursAreGreaterThanCompOffHours() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS,
                this.manager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(27)));
    }

    // negative test case for checking if the log hours are greater than comp off hours
    @Test
    public void testIfLogHoursAreLesserThanCompOffHours() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS,
                this.manager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(27)));
    }

    // negative test case for checking the status that is returned for an invalid date
    @Test
    public void testStatusForWrongLogDate() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.now().minusDays(27), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.LOG_DOES_NOT_EXIST_FOR_THIS_DATE,
                this.manager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(2)));
    }

    // positive test case to check if the compOff is added to employees balance
    @Test
    public void testIfCompOffBalanceGetsUpdatedAfterPuttingLogWork() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.COMPOFF_ADDED_TO_EMPLOYEE, this.manager.addingCompOffToCompOffBalance(this.employee, logWork));
    }

    // negative test case to check if the compOff is added to employees balance
    @Test
    public void testIfCompOffBalanceDoesNotGetUpdatedAfterPuttingLogWork() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS, this.manager.addingCompOffToCompOffBalance(this.employee, logWork));
    }

    // negative test case to check if the compOff is added to employees balance
    @Test
    public void testIfCompOffBalanceDoesNotGetUpdatedForLogWorkOnWorkingDay() {
        LogWorkModel logWork = new LogWorkModel(LocalDate.of(2019, Month.FEBRUARY, 18), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.manager.putLogWork(this.employee, logWork);

        assertEquals(CompOffProcessStatus.COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY, this.manager.addingCompOffToCompOffBalance(this.employee, logWork));
    }
}