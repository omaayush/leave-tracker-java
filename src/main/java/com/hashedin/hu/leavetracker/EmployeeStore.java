package com.hashedin.hu.leavetracker;

        import java.util.ArrayList;
        import java.util.HashMap;

public class EmployeeStore {
    ArrayList <Employee> employees;

    public EmployeeStore() {
        this.employees = new ArrayList<>();
    }

    public void addEmployeeToStore(Employee employee) {
        this.employees.add(employee);
    }

    public ArrayList<Employee> getAllEmployees() {
        return this.employees;
    }
}