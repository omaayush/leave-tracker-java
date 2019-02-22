package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class BlanketCoverageManager {

    @Autowired
    EmployeeRepository employeeRepository;

    public LeaveResponse requestValidatorForBlanketCoverage(LeaveRequest request) {
        LeaveResponse response = new LeaveResponse();
        Employee employee = this.employeeRepository.findById(request.employee).get();

        if(request.typeOfLeaves == TypeOfLeaves.MATERNITY && employee.gender == Gender.Male) {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE;
        }

        else if(request.typeOfLeaves == TypeOfLeaves.MATERNITY
                && !employee.hasWorkedForGivenDays(request.startDate, 180))
        {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS;
        }

        else if(request.typeOfLeaves == TypeOfLeaves.MATERNITY
                && employee.numberOfChildren >= 2)
        {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.MATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN;
        }

        else if(request.typeOfLeaves == TypeOfLeaves.SABBATICAL
                && !employee.hasWorkedForGivenDays(request.startDate, 365 * 2)) {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS;
        }

        else if(request.typeOfLeaves == TypeOfLeaves.SABBATICAL
                && ChronoUnit.DAYS.between(request.requestDate, request.startDate) < 45) {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.REQUESTED_LATE_FOR_LEAVE;
        }

        else {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.APPROVED;
            response.reason = ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED;
        }
        return response;
    }
}
