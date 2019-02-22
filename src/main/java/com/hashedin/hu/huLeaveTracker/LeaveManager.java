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
        Employee employee = this.employeeRepository.findById(request.getEmployee()).get();

        // validate input Parameters
        if(!isBlanketCoverage(request.getTypeOfLeaves()) || givenDateIsNull(request.getStartDate())) {
            if((givenDateIsNull(request.getStartDate()) && givenDateIsNull(request.getEndDate()))) {
                response.setReason(ReasonsForLeaveResponse.DATE_NULL);
                throw new IllegalArgumentException("The start date and end date cannot be null");
            }

            if(startDateIsAfterEndDate(request.getStartDate(), request.getEndDate())) {
                response.setReason(ReasonsForLeaveResponse.START_AFTER_END);
                throw new IllegalArgumentException("The start date cannot be after end date");
            }
        }

        response = leaveRequestsApprovalManager(request, employee ,response);

        // add the approved request to the list
        if(response.getStatusOfLeaveRequest() == StatusOfLeaveRequest.APPROVED) {

            request.setStatusOfLeaveRequest(StatusOfLeaveRequest.APPROVED);

            if(request.getTypeOfLeaves() == TypeOfLeaves.OUT_OF_OFFICE) {
                employee.setLeavesBalance(
                        (int) (employee.getLeavesBalance() - numberOfLeavesRequested(employee,
                                request.getStartDate(),
                                request.getEndDate())));
            }

            if(request.getTypeOfLeaves() == TypeOfLeaves.COMP_OFF)
            {
                employee.updateCompOffBalance(request.getStartDate(),
                        numberOfLeavesRequested(employee, request.getStartDate(), request.getEndDate()));
            }

            if(request.getTypeOfLeaves() == TypeOfLeaves.MATERNITY) {
                request.setEndDate(LocalDate.of(request.getStartDate().getYear(),
                        request.getStartDate().getMonth().plus(6), request.getStartDate().getDayOfMonth()));
            }

            if(request.getTypeOfLeaves() == TypeOfLeaves.PATERNITY) {
                request.setEndDate(LocalDate.of(
                        request.getStartDate().getYear(),
                        request.getStartDate().getMonth(),
                        request.getStartDate().plusDays(10).getDayOfMonth()));
            }

            if(isBlanketCoverage(request.getTypeOfLeaves()) && !employee.isOnBlanketCoverageLeave()) {
                employee.setOnBlanketCoverageLeave(true);
            }

            // check here for optional leaves logic

            if(request.getTypeOfLeaves() == TypeOfLeaves.OPTIONAL_LEAVE) {
                List<LocalDate> optionalLeaves = employee.getOptionalLeavesAvailed();
                optionalLeaves.add(request.getStartDate());
                employee.setOptionalLeavesAvailed(optionalLeaves);
            }

            this.listOfApprovedRequests.add(request);
            this.leaveRequestRepository.save(request);
        }

        return response;
    }

    private boolean isOtherOptionalLeaveAvailed(List<LocalDate> optionalLeavesAvailed, Holiday currentHoliday) {
        return optionalLeavesAvailed.contains(currentHoliday.getGroupedWith());
    }

    private LeaveResponse leaveRequestsApprovalManager(LeaveRequest request,
                                                       Employee employee,
                                                       LeaveResponse response) {

        // this numberOfLeavesRequested handles all the case of weekends and public holidays
        if( isBlanketCoverage(request.getTypeOfLeaves()) == false
                && request.getTypeOfLeaves() == TypeOfLeaves.OUT_OF_OFFICE
                && (employee.getLeavesBalance() <= 0
                || employee.getLeavesBalance() < numberOfLeavesRequested(employee,
                request.getStartDate(),
                request.getEndDate())))
        {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.INSUFFICIENT_LEAVE_BALANCE);
            return response;
        }
        if (isBlanketCoverage(request.getTypeOfLeaves()) == false){
            if(numberOfLeavesRequested(employee, request.getStartDate(), request.getEndDate()) == 0
                    && request.getTypeOfLeaves() != TypeOfLeaves.OPTIONAL_LEAVE){

                response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
                response.setReason(ReasonsForLeaveResponse.PUBLIC_HOLIDAY);
                return response;
            }
        }
        if(isBlanketCoverage(request.getTypeOfLeaves()) == false
                && alreadyRequestedLeaveDuringPeriod(request)) {

            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
            return response;
        }

        if(isBlanketCoverage(request.getTypeOfLeaves()) == false
                && request.getTypeOfLeaves() == TypeOfLeaves.COMP_OFF
                && checkForCompOffAvailability(request, employee) == false) {

            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.INSUFFICIENT_COMPOFF_BALANCE);
            return response;
        }

        if(isBlanketCoverage(request.getTypeOfLeaves()) == false
                && request.getTypeOfLeaves() == TypeOfLeaves.PATERNITY
                && numberOfLeavesRequested(employee, request.getStartDate(), request.getEndDate()) > 10) {

            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.PATERNITY_LEAVE_CANNOT_EXCEED_10);
            return response;
        }

        if(isBlanketCoverage(request.getTypeOfLeaves()) == false
                && request.getTypeOfLeaves() == TypeOfLeaves.PATERNITY && employee.getGender() == Gender.Female) {

            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY);
            return response;
        }

        if(isBlanketCoverage(request.getTypeOfLeaves()) == false
                && request.getTypeOfLeaves() == TypeOfLeaves.PATERNITY
                && employee.getNumberOfChildren() >= 8) {

            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
            return response;
        }

        if(request.getTypeOfLeaves() == TypeOfLeaves.OPTIONAL_LEAVE) {
            if(numberOfLeavesRequested(employee, request.getStartDate(), request.getEndDate()) > 1) {
                response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
                response.setReason(ReasonsForLeaveResponse.OPTIONAL_LEAVES_GREATER_THAN_1);
                return response;
            }

            if(!isAHoliday(request.getStartDate())
                    && !this.calender.getHolidayInfo(request.getStartDate()).isOptional()) {
                response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
                response.setReason(ReasonsForLeaveResponse.NOT_AN_OPTIONAL_LEAVE);
                return  response;
            }

            if(isOtherOptionalLeaveAvailed(employee.getOptionalLeavesAvailed(),
                    this.calender.getHolidayInfo(request.getStartDate()))) {
                response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
                response.setReason(ReasonsForLeaveResponse.OPTIONAL_LEAVE_ALREADY_AVAILED);
                return response;
            }
        }

        if(isBlanketCoverage(request.getTypeOfLeaves())) {

            if(employee.isOnBlanketCoverageLeave()) {
                response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
                response.setReason(ReasonsForLeaveResponse.ALREADY_APPLIED_FOR_LEAVE);
                return response;
            }

            // go to blanketLeavesCoverageManager
            return requestValidatorForBlanketCoverage(request);
        }

        response.setStatusOfLeaveRequest(StatusOfLeaveRequest.APPROVED);
        response.setReason(ReasonsForLeaveResponse.APPROVED_LEAVE);

        return response;
    }

    private boolean isBlanketCoverage(TypeOfLeaves typeOfLeaves) {
        return typeOfLeaves == TypeOfLeaves.MATERNITY
                || typeOfLeaves == TypeOfLeaves.SABBATICAL;
    }

    private boolean checkForCompOffAvailability(LeaveRequest request, Employee employee) {
        return employee.getValidCompOffBalanceSize(request.getStartDate())
                >= numberOfLeavesRequested(employee, request.getStartDate(), request.getEndDate());
    }

    private boolean alreadyRequestedLeaveDuringPeriod(LeaveRequest request) {

        boolean alreadyAppliedForLeave = false;

        for(int i=0; i<listOfApprovedRequests.size(); i++) {
            LeaveRequest approvedRequest = listOfApprovedRequests.get(i);
            if(approvedRequest.getEmployee() == request.getEmployee()) {
                if( isfirstDateBeforeSecondDateAndAfterThirdDate(request.getEndDate(),
                        approvedRequest.getEndDate(),
                        approvedRequest.getStartDate())

                        || isfirstDateBeforeSecondDateAndAfterThirdDate(request.getStartDate(),
                        approvedRequest.getEndDate(),
                        approvedRequest.getStartDate() )

                        || doesTheRequestDurationContainExistingDuration(request.getStartDate(),
                        request.getEndDate(),
                        approvedRequest.getStartDate(),
                        approvedRequest.getEndDate())){

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
        Employee employee = this.employeeRepository.findById(request.getEmployee()).get();

        if(request.getTypeOfLeaves() == TypeOfLeaves.MATERNITY && employee.getGender() == Gender.Male) {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE);
        }

        else if(request.getTypeOfLeaves() == TypeOfLeaves.MATERNITY
                && !employee.hasWorkedForGivenDays(request.getStartDate(), 180))
        {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        }

        else if(request.getTypeOfLeaves() == TypeOfLeaves.MATERNITY
                && employee.getNumberOfChildren() >= 8)
        {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.MATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN);
        }

        else if(request.getTypeOfLeaves() == TypeOfLeaves.SABBATICAL
                && !employee.hasWorkedForGivenDays(request.getStartDate(), 365 * 2)) {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS);
        }

        else if(request.getTypeOfLeaves() == TypeOfLeaves.SABBATICAL
                && ChronoUnit.DAYS.between(request.getRequestDate(), request.getStartDate()) < 45) {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.REJECTED);
            response.setReason(ReasonsForLeaveResponse.REQUESTED_LATE_FOR_LEAVE);
        }

        else {
            response.setStatusOfLeaveRequest(StatusOfLeaveRequest.APPROVED);
            response.setReason(ReasonsForLeaveResponse.BLANKET_COVERAGE_REQUEST_VALIDATED);
        }
        return response;
    }

    private boolean isHolidayOptionalAndAvailedByEmployee(Employee employee,
                                                          Holiday currentHoliday,
                                                          LocalDate date) {
        return currentHoliday != null && currentHoliday.isOptional()
                && !(employee.getOptionalLeavesAvailed().contains(date))
                && !(employee.getOptionalLeavesAvailed().contains(currentHoliday.getGroupedWith()));
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
