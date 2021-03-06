package com.hashedin.hu.huLeaveTracker;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employees= new ArrayList<Employee>(Arrays.asList(
            new Employee(1,"Aayush",10, LocalDate.of(2019,2,1),Gender.Male),
            new Employee(2,"Ankesh",22, LocalDate.of(2019,2,1),Gender.Male),
            new Employee(3,"Piyush",12, LocalDate.of(2019,2,1),Gender.Male),
            new Employee(4,"Prabhat",3, LocalDate.of(2019,2,1),Gender.Male),
            new Employee(5,"Utkarsh",5, LocalDate.of(2019,2,1),Gender.Male)
    ));

//    @Autowired
//    private EmployeeStore employeeStore;
//
//    @Autowired
//    public EmployeeService(EmployeeStore employeeStore) {
//        this.employeeStore = employeeStore;
//    }

    public List<Employee> getAllEmployees() {
        return employees;
        //return this.employeeStore.getAllEmployees();
    }

    public Employee getEmployee(int id) {
        return employees.stream().filter(e -> e.getId()==id ).findFirst().get();
    }

    public void addEmployee(Employee e)
    {
        employees.add(e);
    }

    public void updateEmployee(int id, Employee employeeFromSource) {
        id--;
        for(int i=0;i<employees.size();i++)
        {
            Employee employee;
            employee = employees.get(i);
            if(employee.getId()==id)
            {
                employees.set(id,employeeFromSource);
                return;
            }
        }
    }

    public void deleteEmployee(int id) {
        employees.removeIf(e->e.getId()==id);
    }
}
