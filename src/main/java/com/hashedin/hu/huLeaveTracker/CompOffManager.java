//package com.hashedin.hu.huLeaveTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.Collection;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class CompOffManager {
//    LogWorkStore logWorkStore;
//
//    @Autowired
//    private LogWorkStore logWorkStore;
//
//    @Autowired EmployeeService employeeService;
//
//    @Autowired CompOffRepository compOffRepository;
//
//    CompOffManager() {}
//
//    CompOffManager(LogWorkStore logWorkStore, EmployeeService employeeService, CompOffRepository compOffRepository) {
//        this.logWorkStore = logWorkStore;
//        this.employeeService = employeeService;
//        this.compOffRepository = compOffRepository;
//    }
//
//    public LogWorkStatus putLogWork(Employee employee, LogWorkModel logWork) {
//        return this.logWorkStore.putLogWorkInStore(employee, logWork);
//    }
//
//    public CompOffProcessStatus areLoggedHoursGreaterThanCompOffHours(Employee employee, LocalDate date) {
//
//        long hours = this.logWorkStore.getLogWorkHours(employee, date);
//
//        if(hours == -1) {
//            return CompOffProcessStatus.LOG_DOES_NOT_EXIST_FOR_THIS_DATE;
//        }
//
//        if(hours < 8) {
//            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
//        }
//
//        else {
//            return CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS;
//        }
//
//    }
//
//    //public CompOffProcessStatus addingCompOffToCompOffBalance(Employee employee, LogWorkModel logWork) {
//    public CompOffProcessStatus addingCompOffToCompOffBalance(Employee employee, int logWorkId) {
//        //List<CompOffModel> compOffList = employee.getCompOffBalance();
//        Collection<CompOffModel> compOffList = employee.getCompOffBalance();
//        LogWorkModel logWork = logWorkStore.getLogWorkById(logWorkId);
//
//        if(isNotAHoliday(logWork.getLogDate())) {
//
//            return CompOffProcessStatus.COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY;
//        }
//
//        if(areLoggedHoursGreaterThanCompOffHours(employee, logWork.getLogDate())
//                != CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS) {
//
//            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
//        }
//
////        CompOffModel compOff = new CompOffModel(logWork,
////                                                logWork.logDate.plusDays(30),
////                                                CompOffStatus.AVAILABLE);
//        CompOffModel compOff = new CompOffModel(employee.getId(),logWork.getId(),
//                logWork.getLogDate().plusDays(30),
//                CompOffStatus.AVAILABLE);
//
//        compOffList.add(compOff);
//        compOffRepository.save(compOff);
//        compOffList = compOffRepository.findByAssociatedLogId(logWork.getId());
//        employee.setCompOffBalance(compOffList);
//        this.employeeService.addEmployee(employee)
//        return CompOffProcessStatus.COMPOFF_ADDED_TO_EMPLOYEE;
//    }
//
//    private boolean isNotAHoliday(LocalDate date) {
//        PublicHolidays calender = new PublicHolidays();
//        return date.getDayOfWeek().name() != "SATURDAY"
//                && date.getDayOfWeek().name() != "SUNDAY"
//                && !calender.isAPublicHoliday(date);
//
//    }
//
//}
package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class CompOffManager {

    @Autowired private LogWorkStore logWorkStore;

    @Autowired EmployeeService employeeService;

    @Autowired CompOffRepository compOffRepository;

    CompOffManager() {}

    CompOffManager(LogWorkStore logWorkStore, EmployeeService employeeService, CompOffRepository compOffRepository) {
        this.logWorkStore = logWorkStore;
        this.employeeService = employeeService;
        this.compOffRepository = compOffRepository;
    }

    public LogWorkStatus putLogWork(Employee employee, LogWorkModel logWork) {
        return this.logWorkStore.putLogWorkInStore(employee, logWork);
    }

    public CompOffProcessStatus areLoggedHoursGreaterThanCompOffHours(Employee employee, LocalDate date) {

        long hours = this.logWorkStore.getLogWorkHours(employee, date);

        if(hours == -1) {
            return CompOffProcessStatus.LOG_DOES_NOT_EXIST_FOR_THIS_DATE;
        }

        if(hours < Constants.MIN_COMP_OFF_HOURS) {
            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
        }

        else {
            return CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS;
        }

    }

    public CompOffProcessStatus addingCompOffToCompOffBalance(Employee employee, int logWorkId) {

        Collection<CompOffModel> compOffList = employee.getCompOffBalance();

        LogWorkModel logWork = logWorkStore.getLogWorkById(logWorkId);

        if(isNotAHoliday(logWork.getLogDate())) {
            return CompOffProcessStatus.COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY;
        }

        if(areLoggedHoursGreaterThanCompOffHours(employee, logWork.getLogDate())
                != CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS) {

            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
        }


        CompOffModel compOff = new CompOffModel(employee.getId(),logWork.getId(),
                logWork.getLogDate().plusDays(30),
                CompOffStatus.AVAILABLE);


        compOffList.add(compOff);
        compOffRepository.save(compOff);
        compOffList = compOffRepository.findByAssociatedLogId(logWork.getId());
        employee.setCompOffBalance(compOffList);

        //updating the employee
        this.employeeService.addEmployee(employee);
        return CompOffProcessStatus.COMPOFF_ADDED_TO_EMPLOYEE;
    }

    private boolean isNotAHoliday(LocalDate date) {
        PublicHolidays calender = new PublicHolidays();
        return date.getDayOfWeek().name() != "SATURDAY"
                && date.getDayOfWeek().name() != "SUNDAY"
                && !calender.isAPublicHoliday(date);
    }

}

