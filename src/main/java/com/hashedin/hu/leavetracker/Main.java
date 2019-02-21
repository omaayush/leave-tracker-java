package com.hashedin.hu.leavetracker;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.text.ParseException;
//import java.util.concurrent.*;

public class Main {

//    void applyForLeaves(Employee employee,int a ){
//        if(employee.generalLeaves>=a)
//        {
//            employee.generalLeaves-=a;
//            System.out.println("LeaveType Granted");
//        }
//        else
//        {
//            System.out.println("Less Number of leaves left");
//        }
//    }
//
//    void leaveLeft(Employee employee)
//    {
//        System.out.println(employee.generalLeaves+ " Leaves Left");
//    }



    public static void main(String[] args) {
//        System.out.println("OMNAMAHSHIVAY");
//        #using Date format
//        Date today=new Date();
//        System.out.println(today);

//        #initial test
//        Main aayushLeaveManager = new Main();
//        Employee aayush=new Employee(1,10);
//        aayushLeaveManager.applyForLeaves(aayush,5);
//        aayushLeaveManager.leaveLeft(aayush);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

//        #use LocalDate only not Date (working)
//        LocalDate date=LocalDate.now();
//        System.out.println(date);
//        LeaveAccrualManager leaveCreditManager= (LeaveAccrualManager) Executors.newFixedThreadPool(10);
//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        EmployeeStore employeeStore = new EmployeeStore();
//        LeaveAccrualManager leaveAccrualManager = new LeaveAccrualManager(employeeStore);
//
//        final Runnable leaveAccrualService = new Runnable() {
//            public void run() {
//                // put the leave accrual credit method in here
//                leaveAccrualManager.creditLeavesPeriodically(LocalDate.now());
//            }
//        };
//
//        final ScheduledFuture<?> leaveAccrualServiceHandle =
//                scheduler.scheduleAtFixedRate(leaveAccrualService, LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth(), 30, TimeUnit.DAYS);
//
//
//
//
//
    }
}

//test all
//storeof Employees
//checkfromlastaddedholidays