    package com.hashedin.hu.huLeaveTracker;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;
    //import org.springframework.stereotype.Service;

    //import java.time.LocalDate;
    import java.util.ArrayList;
    //import java.util.HashMap;

    @Component
    public class EmployeeStore {

        private ArrayList <Employee> employees;
        //Employee e1=new Employee(1,"Aayush",5, LocalDate.of(2018,2,2),Gender.Male);


        @Autowired
        public EmployeeStore() {
            this.employees = new ArrayList<>();
            }

        public void addEmployeeToStore(Employee employee) {
            this.employees.add(employee);
        }

        public ArrayList<Employee> getAllEmployees() {
            //employees.add(e1);

            return this.employees;
        }
    }
