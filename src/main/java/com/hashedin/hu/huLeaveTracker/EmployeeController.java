//package com.hashedin.hu.huLeaveTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class EmployeeController {
//
//    @RequestMapping("/employees")
//    public ArrayList<Employee> getAllEmployees() {
//        ArrayList<Employee> allEmployees=new ArrayList<>();
//        allEmployees.add(new Employee(1,"Aayush",2, LocalDate.of(2019,2,1)));
//        return allEmployees;
//    }
//}

//package com.hashedin.hu.huLeaveTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestBody;
//
//
////import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class EmployeeController {
//
////    @Autowired
////    private EmployeeStore employeeStore;
//
//    //@Autowired
//    private EmployeeService employeeService;
//
//    @Autowired
//    //public EmployeeController(EmployeeStore employeeStore, EmployeeService employeeService) {
//    public EmployeeController(EmployeeService employeeService) {
//
//            //this.employeeStore = employeeStore;
//        this.employeeService = employeeService;
//        //CSVFileReader reader = new CSVFileReader(this.employeeStore);
//        //String path = "/home/aayush_varshney/leave-tracker-java/src/main/CSV_Files/employees.csv";
//        //reader.loadEmployeeFromCSV(path);
//    }
//
//    @RequestMapping("/employees")
//    public List<Employee> getAllEmployees() {
//        List<Employee> allEmployees;
//        allEmployees = this.employeeService.getAllEmployees();
//        //System.out.println(allEmployees);
//        return allEmployees;
//    }
//
//    @RequestMapping("/employees/{id}")
//    public Employee getEmployee(@PathVariable int id) {
//        return employeeService.getEmployee(id);
//    }
//
//    @RequestMapping(method = RequestMethod.POST,value = "/employees")
//    public void addEmployee(@RequestBody Employee e)
//    {
//        employeeService.addEmployee(e);
//    }
//
//    @RequestMapping(method = RequestMethod.PUT,value = "/employees/{id}")
//    public void updateEmployee(@RequestBody Employee e,@PathVariable int id)
//    {
//        employeeService.updateEmployee(id,e);
//    }
//
//    @RequestMapping(method = RequestMethod.DELETE,value = "/employees/{id}")
//    public void deleteEmployee(@PathVariable int id)
//    {
//        employeeService.deleteEmployee(id);
//    }
//
//}
package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeStore employeeStore;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeStore employeeStore, EmployeeService employeeService) {
        this.employeeStore = employeeStore;
        this.employeeService = employeeService;
        CSVFileReader reader = new CSVFileReader(this.employeeStore);
        String path = "/home/aayush_varshney/leave-tracker-java/src/main/CSV_Files/employees.csv";

        reader.loadEmployeeFromCSV(path);

        this.employeeStore.getAllEmployees().forEach(employee -> {
            employeeService.addEmployee(employee);
        });
    }
    @RequestMapping("/employees")
    public List<Employee> getAllEmployees() {
        List<Employee> allEmployees = this.employeeService.getAllEmployees();
        System.out.println(allEmployees);
        return allEmployees;
    }

    @RequestMapping("/employees/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @RequestMapping(value = "/employees", method= RequestMethod.POST)
    public void addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

    @RequestMapping(value = "/employees/{id}", method=RequestMethod.DELETE)
    public void deleteEmployeeById(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
