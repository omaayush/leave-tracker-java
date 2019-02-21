//package com.hashedin.hu.leavetracker;
//
////import java.time.LocalDate;
////import java.time.temporal.ChronoUnit;
////import java.util.ArrayList;
////import java.util.HashMap;
//
//public class LogDatabase {
//
//
////    HashMap<Employee, ArrayList<LogWorkModel>> listOfLogWorksReported;
////
////    LogDatabase() {
////        this.listOfLogWorksReported = new HashMap<Object, ArrayList<LogWorkModel>>();
////    }
////
////    public LogWorkStatus putLogWorkInStore(Employee employee, com.hashedin.hu.huLeaveTracker.LogWork logWork) {
////
////        // first time case
////        this.listOfLogWorksReported.putIfAbsent(employee, new ArrayList<LogWorkModel>());
////
////        ArrayList<LogWorkModel> currentlogWork = this.listOfLogWorksReported.get(employee);
////
////        currentlogWork.add(logWork);
////
////        this.listOfLogWorksReported.put(employee, currentlogWork);
////
////        return LogWorkStatus.LOG_WORK_ADDED;
////    }
////
////    public long getLogWorkHours(Employee employee, LocalDate date) {
////        ArrayList<LogWorkModel> currentlogWork = this.listOfLogWorksReported.get(employee);
////        for(int i=0 ; i<currentlogWork.size(); i++)
////        {
////            LogWorkModel currentLog = currentlogWork.get(i);
////            if(currentLog.logDate.isEqual(date)) {
////                return ChronoUnit.HOURS.between(currentLog.startTime, currentLog.endTime);
////            }
////        }
////
////        return -1;
////    }
//}
//
//
