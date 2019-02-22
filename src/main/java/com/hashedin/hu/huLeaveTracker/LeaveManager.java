//package com.hashedin.hu.huLeaveTracker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Service
//public class LeaveManager {
//
//    @Autowired
//    private LeaveRequestRepository leaveRequestRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    ArrayList <LeaveRequest> listOfApprovedRequests;
//    CompOffManager compOffManager;
//    BlanketCoverageManager blanketCoverageManager;
//    PublicHolidays calender;
//
////    protected LeaveManager() {
////        this.listOfApprovedRequests = new ArrayList<LeaveRequest>();
////
////        this.compOffManager = new CompOffManager();
////        this.blanketCoverageManager = new BlanketCoverageManager();
////        this.calender = new PublicHolidays();
////    }
//
//    @Autowired
//    LeaveManager(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
//
//        this.leaveRequestRepository = leaveRequestRepository;
//        this.employeeRepository = employeeRepository;
//
//
//        this.listOfApprovedRequests = new ArrayList<LeaveRequest>();
//
//        this.compOffManager = new CompOffManager();
//        this.blanketCoverageManager = new BlanketCoverageManager();
//        this.calender = new PublicHolidays();
//
//    }
//
//    public List<LeaveRequest> getAllLeaveRequests() {
//        List<LeaveRequest> allLeaveRequests = new ArrayList<>();
//        leaveRequestRepository.findAll().forEach(allLeaveRequests::add);
//        return allLeaveRequests;
//    }
//
//    // currently this only checks if there is a leave balance available or not and
//    // if the leave balance is available it approves the leaves
//    public LeaveResponse apply(LeaveRequest request){
//
//        LeaveResponse response = new LeaveResponse();
//        Employee employee = this.employeeRepository.findById(request.employee).get();
//
//        // validate input Parameters
//        if(!isBlanketCoverage(request.typeOfLeaves) || givenDateIsNull(request.startDate)) {
//            if((givenDateIsNull(request.startDate) && givenDateIsNull(request.endDate))) {
//                response.reason = ReasonsForLeaveResponse.DATE_NULL;
//                throw new IllegalArgumentException("The start date and end date cannot be null");
//            }
//
//            if(startDateIsAfterEndDate(request.startDate, request.endDate)) {
//                response.reason = ReasonsForLeaveResponse.START_AFTER_END;
//                throw new IllegalArgumentException("The start date cannot be after end date");
//            }
//        }
//
//        response = leaveRequestsApprovalManager(request, employee ,response);
//
//        // add the approved request to the list
//        if(response.statusOfLeaveRequest == StatusOfLeaveRequest.APPROVED) {
//
//            request.statusOfLeaveRequest = StatusOfLeaveRequest.APPROVED;
//
//            if(request.typeOfLeaves == TypeOfLeaves.OUT_OF_OFFICE) {
//                employee.leavesBalance -= numberOfLeavesRequested(employee, request.startDate, request.endDate);
//            }
//
//            if(request.typeOfLeaves == TypeOfLeaves.COMP_OFF)
//            {
//                employee.updateCompOffBalance(request.startDate,
//                        numberOfLeavesRequested(employee, request.startDate, request.endDate));
//            }
//
//            if(request.typeOfLeaves == TypeOfLeaves.MATERNITY) {
//                request.endDate = LocalDate.of(request.startDate.getYear(),
//                        request.startDate.getMonth().plus(6), request.startDate.getDayOfMonth());
//            }
//
//            if(request.typeOfLeaves == TypeOfLeaves.PATERNITY) {
//                request.endDate = LocalDate.of(request.startDate.getYear(),
//                        request.startDate.getMonth(), request.startDate.plusDays(10).getDayOfMonth());
//            }
//
//            if(isBlanketCoverage(request.typeOfLeaves) && !employee.isOnBlanketCoverageLeave) {
//                employee.isOnBlanketCoverageLeave = true;
//            }
//
//            // check here for optional leaves logic
//
//            if(request.typeOfLeaves == TypeOfLeaves.OPTIONAL_LEAVE) {
//                employee.optionalLeavesAvailed.add(request.startDate);
//            }
//
//            this.listOfApprovedRequests.add(request);
//            this.leaveRequestRepository.save(request);
//        }
//
//        return response;
//    }
//
//    private boolean isOtherOptionalLeaveAvailed(List<LocalDate> optionalLeavesAvailed, Holiday currentHoliday) {
//        return optionalLeavesAvailed.contains(currentHoliday.groupedWith);
//    }
//
//    private LeaveResponse leaveRequestsApprovalManager(LeaveRequest request,
//                                                       Employee employee,
//                                                       LeaveResponse response) {
//
//        // this numberOfLeavesRequested handles all the case of weekends and public holidays
//        if( isBlanketCoverage(request.typeOfLeaves) == false
//                && request.typeOfLeaves == TypeOfLeaves.OUT_OF_OFFICE
//                && (employee.leavesBalance <= 0
//                || employee.leavesBalance < numberOfLeavesRequested(employee,
//                request.startDate,
//                request.endDate)))
//        {
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE;
//            return response;
//        }
//        if (isBlanketCoverage(request.typeOfLeaves) == false
//                && numberOfLeavesRequested(employee, request.startDate, request.endDate) == 0
//                && request.typeOfLeaves != TypeOfLeaves.OPTIONAL_LEAVE) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.PUBLIC_HOLIDAY;
//            return response;
//        }
//        if(isBlanketCoverage(request.typeOfLeaves) == false
//                && alreadyRequestedLeaveDuringPeriod(request)) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE;
//            return response;
//        }
//
//        if(isBlanketCoverage(request.typeOfLeaves) == false
//                && request.typeOfLeaves == TypeOfLeaves.COMP_OFF
//                && checkForCompOffAvailability(request, employee) == false) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE;
//            return response;
//        }
//
//        if(isBlanketCoverage(request.typeOfLeaves) == false
//                && request.typeOfLeaves == TypeOfLeaves.PATERNITY
//                && numberOfLeavesRequested(employee, request.startDate, request.endDate) > 10) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.PATERNITY_LEAVE_CANNOT_EXCEED_10;
//            return response;
//        }
//
//        if(isBlanketCoverage(request.typeOfLeaves) == false
//                && request.typeOfLeaves == TypeOfLeaves.PATERNITY && employee.gender == Gender.Female) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY;
//            return response;
//        }
//
//        if(isBlanketCoverage(request.typeOfLeaves) == false
//                && request.typeOfLeaves == TypeOfLeaves.PATERNITY
//                && employee.numberOfChildren >= 2) {
//
//            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//            response.reason = ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN;
//            return response;
//        }
//
//        if(request.typeOfLeaves == TypeOfLeaves.OPTIONAL_LEAVE) {
//            if(numberOfLeavesRequested(employee, request.startDate, request.endDate) > 1) {
//                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//                response.reason = ReasonsForLeaveResponse.OPTIONAL_LEAVES_GREATER_THAN_1;
//                return response;
//            }
//
//            if(!isAHoliday(request.startDate) && !this.calender.getHolidayInfo(request.startDate).isOptional) {
//                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//                response.reason = ReasonsForLeaveResponse.NOT_AN_OPTIONAL_LEAVE;
//                return  response;
//            }
//
//            if(isOtherOptionalLeaveAvailed(employee.optionalLeavesAvailed,
//                    this.calender.getHolidayInfo(request.startDate))) {
//                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//                response.reason = ReasonsForLeaveResponse.OPTIONAL_LEAVE_ALREADY_AVAILED;
//                return response;
//            }
//        }
//
//        if(isBlanketCoverage(request.typeOfLeaves)) {
//
//            if(employee.isOnBlanketCoverageLeave) {
//                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
//                response.reason = ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE;
//                return response;
//            }
//
//            // go to blanketCoverageManager
//            return this.blanketCoverageManager.requestValidatorForBlanketCoverage(request);
//        }
//
//        response.statusOfLeaveRequest = StatusOfLeaveRequest.APPROVED;
//        response.reason = ReasonsForLeaveResponse.APPROVED_LEAVE;
//
//        return response;
//    }
//
//    private boolean isBlanketCoverage(TypeOfLeaves typeOfLeaves) {
//        return typeOfLeaves == TypeOfLeaves.MATERNITY
//                || typeOfLeaves == TypeOfLeaves.SABBATICAL;
//    }
//
//    private boolean checkForCompOffAvailability(LeaveRequest request, Employee employee) {
//        return employee.getValidCompOffBalanceSize(request.startDate)
//                >= numberOfLeavesRequested(employee, request.startDate, request.endDate);
//    }
//
//    private boolean alreadyRequestedLeaveDuringPeriod(LeaveRequest request) {
//
//        boolean alreadyAppliedForLeave = false;
//
//        for(int i=0; i<listOfApprovedRequests.size(); i++) {
//            LeaveRequest approvedRequest = listOfApprovedRequests.get(i);
//            if(approvedRequest.employee == request.employee) {
//                if( isfirstDateBeforeSecondDateAndAfterThirdDate(request.endDate,
//                        approvedRequest.endDate,
//                        approvedRequest.startDate)
//
//                        || isfirstDateBeforeSecondDateAndAfterThirdDate(request.startDate,
//                        approvedRequest.endDate,
//                        approvedRequest.startDate )
//
//                        || doesTheRequestDurationContainExistingDuration(request.startDate,
//                        request.endDate,
//                        approvedRequest.startDate,
//                        approvedRequest.endDate)){
//
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean doesTheRequestDurationContainExistingDuration(LocalDate startDate,
//                                                                  LocalDate endDate,
//                                                                  LocalDate startDate1,
//                                                                  LocalDate endDate1) {
//        return startDate.isBefore(startDate1) && endDate.isAfter(endDate1);
//    }
//
//    private boolean isfirstDateBeforeSecondDateAndAfterThirdDate(LocalDate firstDate,
//                                                                 LocalDate secondDate,
//                                                                 LocalDate thirdDate) {
//        return firstDate.isBefore(secondDate) && firstDate.isAfter(thirdDate);
//    }
//
//    // this numberOfLeavesRequested handles all the case of weekends and public holidays
//    private long numberOfLeavesRequested(Employee employee, LocalDate startDate, LocalDate endDate) {
//        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
//        long requiredDays = totalDays;
//
//        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
//        {
//
//            if(isAHoliday(date))
//            {   Holiday currentHoliday = this.calender.getHolidayInfo(date);
//                if( !isHolidayOptionalAndAvailedByEmployee(employee, currentHoliday, date)) {
//                    requiredDays -= 1;
//                }
//            }
//        }
//
//        if(totalDays == 1 && isAHoliday(startDate)) {
//            Holiday currentHoliday = this.calender.getHolidayInfo(startDate);
//            if(!isHolidayOptionalAndAvailedByEmployee(employee, currentHoliday, startDate)) {
//                requiredDays -= 1;
//            }
//        }
//
//        return requiredDays;
//    }
//
//    private boolean isHolidayOptionalAndAvailedByEmployee(Employee employee,
//                                                          Holiday currentHoliday,
//                                                          LocalDate date) {
//        return currentHoliday != null && currentHoliday.isOptional
//                && !(employee.optionalLeavesAvailed.contains(date))
//                && !(employee.optionalLeavesAvailed.contains(currentHoliday.groupedWith));
//    }
//
//    private boolean isAHoliday(LocalDate date) {
//        return date.getDayOfWeek().name() == "SATURDAY"
//                || date.getDayOfWeek().name() == "SUNDAY"
//                || isPublicHoliday(date);
//    }
//
//    private boolean isPublicHoliday(LocalDate date) {
//
//        return this.calender.isAPublicHoliday(date);
//    }
//
//
//    private boolean startDateIsAfterEndDate(LocalDate startDate, LocalDate endDate) {
//        return startDate.isAfter(endDate);
//    }
//
//    private boolean givenDateIsNull(LocalDate date) {
//        return date == null;
//    }
//
//}
package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveManager {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    private ArrayList <LeaveRequest> listOfApprovedRequests;

    private CompOffManager compOffManager;


    private PublicHolidays calender;

    protected LeaveManager() {
        this.listOfApprovedRequests = new ArrayList<LeaveRequest>();

        this.compOffManager = new CompOffManager();
        this.calender = new PublicHolidays();
        this.employeeService = new EmployeeService();
    }

    @Autowired
    LeaveManager(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {

        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;


        this.listOfApprovedRequests = new ArrayList<LeaveRequest>();

        this.compOffManager = new CompOffManager();
        this.calender = new PublicHolidays();

    }

    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> allLeaveRequests = new ArrayList<>();
        leaveRequestRepository.findAll().forEach(allLeaveRequests::add);
        return allLeaveRequests;
    }

    // currently this only checks if there is a leave balance available or not and
    // if the leave balance is available it approves the leaves
    public LeaveResponse apply(LeaveRequest request){

        LeaveResponse response = new LeaveResponse();
        Employee employee = this.employeeRepository.findById(request.employee).get();

        // validate input Parameters
        if(!isBlanketCoverage(request.typeOfLeaves) || givenDateIsNull(request.startDate)) {
            if((givenDateIsNull(request.startDate) && givenDateIsNull(request.endDate))) {
                response.reason = ReasonsForLeaveResponse.DATE_NULL;
                throw new IllegalArgumentException("The start date and end date cannot be null");
            }

            if(startDateIsAfterEndDate(request.startDate, request.endDate)) {
                response.reason = ReasonsForLeaveResponse.START_AFTER_END;
                throw new IllegalArgumentException("The start date cannot be after end date");
            }
        }

        response = leaveRequestsApprovalManager(request, employee ,response);

        // add the approved request to the list
        if(response.statusOfLeaveRequest == StatusOfLeaveRequest.APPROVED) {

            request.statusOfLeaveRequest = StatusOfLeaveRequest.APPROVED;

            if(request.typeOfLeaves == TypeOfLeaves.OUT_OF_OFFICE) {
                employee.leavesBalance -= numberOfLeavesRequested(employee, request.startDate, request.endDate);
            }

            if(request.typeOfLeaves == TypeOfLeaves.COMP_OFF)
            {
                employee.updateCompOffBalance(request.startDate,
                        numberOfLeavesRequested(employee, request.startDate, request.endDate));
            }

            if(request.typeOfLeaves == TypeOfLeaves.MATERNITY) {
                request.endDate = LocalDate.of(request.startDate.getYear(),
                        request.startDate.getMonth().plus(6), request.startDate.getDayOfMonth());
            }

            if(request.typeOfLeaves == TypeOfLeaves.PATERNITY) {
                request.endDate = LocalDate.of(request.startDate.getYear(),
                        request.startDate.getMonth(), request.startDate.plusDays(10).getDayOfMonth());
            }

            if(isBlanketCoverage(request.typeOfLeaves) && !employee.isOnBlanketCoverageLeave) {
                employee.isOnBlanketCoverageLeave = true;
            }

            // check here for optional leaves logic

            if(request.typeOfLeaves == TypeOfLeaves.OPTIONAL_LEAVE) {
                employee.optionalLeavesAvailed.add(request.startDate);
            }

            this.listOfApprovedRequests.add(request);
            this.leaveRequestRepository.save(request);
        }

        return response;
    }

    private boolean isOtherOptionalLeaveAvailed(List<LocalDate> optionalLeavesAvailed, Holiday currentHoliday) {
        return optionalLeavesAvailed.contains(currentHoliday.groupedWith);
    }

    private LeaveResponse leaveRequestsApprovalManager(LeaveRequest request,
                                                       Employee employee,
                                                       LeaveResponse response) {

        // this numberOfLeavesRequested handles all the case of weekends and public holidays
        if( isBlanketCoverage(request.typeOfLeaves) == false
                && request.typeOfLeaves == TypeOfLeaves.OUT_OF_OFFICE
                && (employee.leavesBalance <= 0
                || employee.leavesBalance < numberOfLeavesRequested(employee,
                request.startDate,
                request.endDate)))
        {
            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE;
            return response;
        }
        if (isBlanketCoverage(request.typeOfLeaves) == false){
            if(numberOfLeavesRequested(employee, request.startDate, request.endDate) == 0
                    && request.typeOfLeaves != TypeOfLeaves.OPTIONAL_LEAVE){

                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
                response.reason = ReasonsForLeaveResponse.PUBLIC_HOLIDAY;
                return response;
            }
        }
        if(isBlanketCoverage(request.typeOfLeaves) == false
                && alreadyRequestedLeaveDuringPeriod(request)) {

            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE;
            return response;
        }

        if(isBlanketCoverage(request.typeOfLeaves) == false
                && request.typeOfLeaves == TypeOfLeaves.COMP_OFF
                && checkForCompOffAvailability(request, employee) == false) {

            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE;
            return response;
        }

        if(isBlanketCoverage(request.typeOfLeaves) == false
                && request.typeOfLeaves == TypeOfLeaves.PATERNITY
                && numberOfLeavesRequested(employee, request.startDate, request.endDate) > 10) {

            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.PATERNITY_LEAVE_CANNOT_EXCEED_10;
            return response;
        }

        if(isBlanketCoverage(request.typeOfLeaves) == false
                && request.typeOfLeaves == TypeOfLeaves.PATERNITY && employee.gender == Gender.Female) {

            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY;
            return response;
        }

        if(isBlanketCoverage(request.typeOfLeaves) == false
                && request.typeOfLeaves == TypeOfLeaves.PATERNITY
                && employee.numberOfChildren >= Constants.MAX_NUMBER_OF_CHILDREN) {

            response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
            response.reason = ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN;
            return response;
        }

        if(request.typeOfLeaves == TypeOfLeaves.OPTIONAL_LEAVE) {
            if(numberOfLeavesRequested(employee, request.startDate, request.endDate) > 1) {
                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
                response.reason = ReasonsForLeaveResponse.OPTIONAL_LEAVES_GREATER_THAN_1;
                return response;
            }

            if(!isAHoliday(request.startDate) && !this.calender.getHolidayInfo(request.startDate).isOptional) {
                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
                response.reason = ReasonsForLeaveResponse.NOT_AN_OPTIONAL_LEAVE;
                return  response;
            }

            if(isOtherOptionalLeaveAvailed(employee.optionalLeavesAvailed,
                    this.calender.getHolidayInfo(request.startDate))) {
                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
                response.reason = ReasonsForLeaveResponse.OPTIONAL_LEAVE_ALREADY_AVAILED;
                return response;
            }
        }

        if(isBlanketCoverage(request.typeOfLeaves)) {

            if(employee.isOnBlanketCoverageLeave) {
                response.statusOfLeaveRequest = StatusOfLeaveRequest.REJECTED;
                response.reason = ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE;
                return response;
            }

            // go to blanketLeavesCoverageManager
            return requestValidatorForBlanketCoverage(request);
        }

        response.statusOfLeaveRequest = StatusOfLeaveRequest.APPROVED;
        response.reason = ReasonsForLeaveResponse.APPROVED_LEAVE;

        return response;
    }

    private boolean isBlanketCoverage(TypeOfLeaves typeOfLeaves) {
        return typeOfLeaves == TypeOfLeaves.MATERNITY
                || typeOfLeaves == TypeOfLeaves.SABBATICAL;
    }

    private boolean checkForCompOffAvailability(LeaveRequest request, Employee employee) {
        return employee.getValidCompOffBalanceSize(request.startDate)
                >= numberOfLeavesRequested(employee, request.startDate, request.endDate);
    }

    private boolean alreadyRequestedLeaveDuringPeriod(LeaveRequest request) {

        boolean alreadyAppliedForLeave = false;

        for(int i=0; i<listOfApprovedRequests.size(); i++) {
            LeaveRequest approvedRequest = listOfApprovedRequests.get(i);
            if(approvedRequest.employee == request.employee) {
                if( isfirstDateBeforeSecondDateAndAfterThirdDate(request.endDate,
                        approvedRequest.endDate,
                        approvedRequest.startDate)

                        || isfirstDateBeforeSecondDateAndAfterThirdDate(request.startDate,
                        approvedRequest.endDate,
                        approvedRequest.startDate )

                        || doesTheRequestDurationContainExistingDuration(request.startDate,
                        request.endDate,
                        approvedRequest.startDate,
                        approvedRequest.endDate)){

                    return true;
                }
            }
        }
        return false;
    }

    private boolean doesTheRequestDurationContainExistingDuration(LocalDate startDate,
                                                                  LocalDate endDate,
                                                                  LocalDate startDate1,
                                                                  LocalDate endDate1) {
        return startDate.isBefore(startDate1) && endDate.isAfter(endDate1);
    }

    private boolean isfirstDateBeforeSecondDateAndAfterThirdDate(LocalDate firstDate,
                                                                 LocalDate secondDate,
                                                                 LocalDate thirdDate) {
        return firstDate.isBefore(secondDate) && firstDate.isAfter(thirdDate);
    }

    // this numberOfLeavesRequested handles all the case of weekends and public holidays
    private long numberOfLeavesRequested(Employee employee, LocalDate startDate, LocalDate endDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long requiredDays = totalDays;

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
        {

            if(isAHoliday(date))
            {   Holiday currentHoliday = this.calender.getHolidayInfo(date);
                if( !isHolidayOptionalAndAvailedByEmployee(employee, currentHoliday, date)) {
                    requiredDays -= 1;
                }
            }
        }

        if(totalDays == 1 && isAHoliday(startDate)) {
            Holiday currentHoliday = this.calender.getHolidayInfo(startDate);
            if(!isHolidayOptionalAndAvailedByEmployee(employee, currentHoliday, startDate)) {
                requiredDays -= 1;
            }
        }

        return requiredDays;
    }

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
                && employee.numberOfChildren >= Constants.MAX_NUMBER_OF_CHILDREN)
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

    private boolean isHolidayOptionalAndAvailedByEmployee(Employee employee,
                                                          Holiday currentHoliday,
                                                          LocalDate date) {
        return currentHoliday != null && currentHoliday.isOptional
                && !(employee.optionalLeavesAvailed.contains(date))
                && !(employee.optionalLeavesAvailed.contains(currentHoliday.groupedWith));
    }

    private boolean isAHoliday(LocalDate date) {
        return date.getDayOfWeek().name() == "SATURDAY"
                || date.getDayOfWeek().name() == "SUNDAY"
                || isPublicHoliday(date);
    }

    private boolean isPublicHoliday(LocalDate date) {

        return this.calender.isAPublicHoliday(date);
    }


    private boolean startDateIsAfterEndDate(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate);
    }

    private boolean givenDateIsNull(LocalDate date) {
        return date == null;
    }

}

