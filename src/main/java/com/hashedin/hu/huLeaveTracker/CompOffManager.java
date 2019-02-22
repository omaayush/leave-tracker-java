package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;
import java.util.List;

public class CompOffManager {
    LogWorkStore logWorkStore;

    CompOffManager() {
        this.logWorkStore = new LogWorkStore();
    }

    public LogWorkStatus putLogWork(Employee employee, LogWorkModel logWork) {
        return this.logWorkStore.putLogWorkInStore(employee, logWork);
    }

    public CompOffProcessStatus areLoggedHoursGreaterThanCompOffHours(Employee employee, LocalDate date) {

        long hours = this.logWorkStore.getLogWorkHours(employee, date);

        if(hours == -1) {
            return CompOffProcessStatus.LOG_DOES_NOT_EXIST_FOR_THIS_DATE;
        }

        if(hours < 8) {
            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
        }

        else {
            return CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS;
        }

    }

    public CompOffProcessStatus addingCompOffToCompOffBalance(Employee employee, LogWorkModel logWork) {

        List<CompOffModel> compOffList = employee.getCompOffBalance();

        if(isNotAHoliday(logWork.logDate)) {
            return CompOffProcessStatus.COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY;
        }

        if(areLoggedHoursGreaterThanCompOffHours(employee, logWork.logDate)
                != CompOffProcessStatus.SUFFICIENT_COMPOFF_HOURS) {

            return CompOffProcessStatus.INSUFFICIENT_COMPOFF_HOURS;
        }

        CompOffModel compOff = new CompOffModel(logWork,
                                                logWork.logDate.plusDays(30),
                                                CompOffStatus.AVAILABLE);
        compOffList.add(compOff);
        employee.setCompOffBalance(compOffList);
        return CompOffProcessStatus.COMPOFF_ADDED_TO_EMPLOYEE;
    }

    private boolean isNotAHoliday(LocalDate date) {
        PublicHolidays calender = new PublicHolidays();
        return date.getDayOfWeek().name()
                != "SATURDAY" && date.getDayOfWeek().name()
                != "SUNDAY" && !calender.isAPublicHoliday(date);
    }

}
