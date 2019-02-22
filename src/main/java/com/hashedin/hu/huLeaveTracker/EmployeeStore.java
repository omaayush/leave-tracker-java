    package com.hashedin.hu.huLeaveTracker;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;
    import java.util.ArrayList;

    @Component
    public class EmployeeStore {

        private ArrayList <Employee> employees;

        public ArrayList<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(ArrayList<Employee> employees) {
            this.employees = employees;
        }

        @Autowired
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
