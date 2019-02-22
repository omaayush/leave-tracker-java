package com.hashedin.hu.huLeaveTracker;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeServiceTest {

    @Autowired private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @Before
    public void setup() {

    }


    @Test
    void testToSaveEmployee() {
        employeeService = new EmployeeService(employeeRepository);
        Employee employee = new Employee(1, "ankit", 10, LocalDate.of(2019, Month.MAY, 12), Gender.Male);
        employeeService.addEmployee(employee);
        assertEquals(employee.getName(), employeeService.getEmployeeById(1).get().getName());
    }
}