package com.hashedin.hu.huLeaveTracker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class LogWorkStore {
    HashMap<Employee, ArrayList<LogWorkModel>> listOfLogWorksReported;


    LogWorkStore() {
        this.listOfLogWorksReported = new HashMap<>();
    }

    public LogWorkStatus putLogWorkInStore(Employee employee, LogWorkModel logWork) {

        // first time case
        this.listOfLogWorksReported.putIfAbsent(employee, new ArrayList<>());

        ArrayList<LogWorkModel> currentlogWork = this.listOfLogWorksReported.get(employee);

        currentlogWork.add(logWork);

        this.listOfLogWorksReported.put(employee, currentlogWork);

        return LogWorkStatus.LOG_WORK_ADDED;
    }

    public long getLogWorkHours(Employee employee, LocalDate date) {
        ArrayList<LogWorkModel> currentlogWork = this.listOfLogWorksReported.get(employee);
        for(int i=0 ; i<currentlogWork.size(); i++)
        {
            LogWorkModel currentLog = currentlogWork.get(i);
            if(currentLog.logDate.isEqual(date)) {
                return ChronoUnit.HOURS.between(currentLog.startTime, currentLog.endTime);
            }
        }

        return -1;
    }

    public ArrayList <LogWorkModel> getLogWorkListOfEmployee(Employee employee) {
        return this.listOfLogWorksReported.get(employee);
    }

    public LogWorkModel getLogWork(Employee employee, LocalDate date) {
        ArrayList <LogWorkModel> currentLogWork = this.listOfLogWorksReported.get(employee);
        for(int i = 0; i<currentLogWork.size(); i++) {
            if(currentLogWork.get(i).logDate == date) {
                return currentLogWork.get(i);
            }
        }
        return null;
    }

    public LogWorkStatus removeLogWork(Employee employee, LogWorkModel logWork){
        ArrayList <LogWorkModel> employeeLogs = this.listOfLogWorksReported.get(employee);
        if(employeeLogs != null && employeeLogs.contains(logWork)){
            employeeLogs.remove(logWork);
            return LogWorkStatus.LOG_WORK_REMOVED;
        }

        return LogWorkStatus.LOG_NOT_FOUND;
    }
}
