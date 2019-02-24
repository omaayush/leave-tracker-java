package com.hashedin.hu.huLeaveTracker;

import org.junit.jupiter.api.Assertions;
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

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CompOffManagerTest {

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

    @BeforeEach
    public void setup() {
        employeeService = new EmployeeService(employeeRepository);

        employee = new Employee(1, "ankit", 10, LocalDate.of(2019, Month.MAY, 12), Gender.Male);
        Employee employee2 = new Employee(2, "Ankit", 0, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);
        Employee employee3 = new Employee(3, "Ankit", 1, LocalDate.of(2019, Month.APRIL, 15), Gender.Male);

        employeeService.addEmployee(employee);
        employeeService.addEmployee(employee2);
        employeeService.addEmployee(employee3);

        manager = new LeaveManager(leaveRequestRepository, employeeRepository);
        logWorkStore = new LogWorkStore(logWorkRepository);
        compOffManager = new ManagerCompOff(logWorkStore, employeeService, compOffRepository);
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
    void testIfCompOffLogWorkGetsCreated() {

        // creates a new log work and a store
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
        LogWorkStore logWorkStore = new LogWorkStore(logWorkRepository);

        // checks if the log work gets added to store
        Assertions.assertEquals(LogWorkStatus.LOG_WORK_ADDED, logWorkStore.putLogWorkInStore(this.employee, logWork));
    }


    // positive test case for checking if the log hours are greater than comp off hours
    @Test
    void testIfLogHoursAreGreaterThanCompOffHours() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.now().minusDays(27), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS,
                this.compOffManager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(27)));
    }

    // negative test case for checking if the log hours are greater than comp off hours
    @Test
    void testIfLogHoursAreLesserThanCompOffHours() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.now().minusDays(27), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS,
                this.compOffManager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(27)));
    }

    // negative test case for checking the status that is returned for an invalid date
    @Test
    void testStatusForWrongLogDate() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.now().minusDays(27), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.LOG_DOES_NOT_EXIST_FOR_THIS_DATE,
                this.compOffManager.areLoggedHoursGreaterThanCompOffHours(this.employee, LocalDate.now().minusDays(2)));
    }

    // positive test case to check if the compOff is added to employees balance
    @Test
    void testIfCompOffBalanceGetsUpdatedAfterPuttingLogWork() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(12,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.COMPOFF_ADDED_TO_EMPLOYEE, this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork.getId()));
    }

    // negative test case to check if the compOff is added to employees balance
    @Test
    void testIfCompOffBalanceDoesNotGetUpdatedAfterPuttingLogWork() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.of(2019, Month.JANUARY, 26), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS, this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork.getId()));
    }

    // negative test case to check if the compOff is added to employees balance
    @Test
    void testIfCompOffBalanceDoesNotGetUpdatedForLogWorkOnWorkingDay() {
        LogWorkModel logWork = new LogWorkModel(1, LocalDate.of(2019, Month.FEBRUARY, 18), LocalTime.of(18,0), LocalTime.of(21, 0));
        this.compOffManager.putLogWork(this.employee, logWork);

        Assertions.assertEquals(CompOffProcessStatus.COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY, this.compOffManager.addingCompOffToCompOffBalance(this.employee, logWork.getId()));
    }
}