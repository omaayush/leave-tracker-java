package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class LogWorkStore {

    @Autowired
    private LogWorkRepository logWorkRepository;

    protected LogWorkStore() {}

    LogWorkStore (LogWorkRepository logWorkRepository) {
        this.logWorkRepository = logWorkRepository;
    }


    public LogWorkModel getLogWorkById(int logWorkId) {
        LogWorkModel logwork = this.logWorkRepository.findById(logWorkId).get();
        return logwork;
    }

    public LogWorkStatus putLogWorkInStore(Employee employee, LogWorkModel logWork) {

        this.logWorkRepository.save(logWork);

        return LogWorkStatus.LOG_WORK_ADDED;
    }

    public long getLogWorkHours(Employee employee, LocalDate date) {
        ArrayList<LogWorkModel> currentlogWork = this.logWorkRepository.findAllByEmployeeId(employee.getId());
        for(int i=0 ; i<currentlogWork.size(); i++)
        {
            LogWorkModel currentLog = currentlogWork.get(i);
            if(currentLog.getLogDate().isEqual(date)) {
                return ChronoUnit.HOURS.between(currentLog.getStartTime(), currentLog.getEndTime());
            }
        }

        return -1;
    }

    public ArrayList <LogWorkModel> getLogWorkListOfEmployee(Employee employee) {
        return this.logWorkRepository.findAllByEmployeeId(employee.getId());
    }

    public LogWorkModel getLogWork(Employee employee, LocalDate date) {
        ArrayList <LogWorkModel> currentLogWork = this.logWorkRepository.findAllByEmployeeId(employee.getId());
        for(int i = 0; i<currentLogWork.size(); i++) {
            if(currentLogWork.get(i).getLogDate() == date) {
                return currentLogWork.get(i);
            }
        }
        return null;
    }

    public LogWorkStatus removeLogWork(LogWorkModel logWork) {

        this.logWorkRepository.delete(logWork);

        return LogWorkStatus.LOG_WORK_REMOVED;
    }
}