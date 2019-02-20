package com.hashedin.hu.leavetracker;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.HashSet;

public class EmployeeStore {
    ArrayList<Employee> employees;

    public EmployeeStore() {
        this.employees = new ArrayList<Employee>();
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployeeToStore(Employee employee) {
        this.employees.add(employee);
    }

    public ArrayList<Employee> getAllEmployees() {
        return this.employees;
    }

    public Employee findById(int id) {
        for(Employee employee : this.employees ) {
            if(employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    public Employee updateEmployee(Employee employee) {
        this.employees.remove(employee);
        this.employees.add(employee);
        return  null;
    }
}
