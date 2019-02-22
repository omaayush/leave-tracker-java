package com.hashedin.hu.huLeaveTracker;

public enum ReasonsForLeaveResponse {
    INSUFFICIENT_LEAVE_BALANCE("Insufficient Leave Balance"),
    APPROVED_LEAVE("Leave request meets all the criteria"),
    DATE_NULL("Date Cannot be null"),
    START_AFTER_END("Start date cannot be after end date"),
    PUBLIC_HOLIDAY("It is already a holiday"),
    ALREADY_APPLIED_FOR_LEAVE("Already applied for leave in that duration"),
    INVALID_COMPOFF_LOG_HOURS("Log Hours less than comp off hours"),
    INSUFFICIENT_COMPOFF_BALANCE("Insufficient Compoff balance"),
    COMPOFF_EXPIRED("COMPOFF WAS EXPIRED"),
    BLANKET_COVERAGE_REQUEST_VALIDATED("Blanket coverage request was validated"),
    BLANKET_COVERAGE_REQUEST_NOT_VALIDATED("Blanket coverage request was found to be invalid"),
    MALE_EMPLOYEE_CANNOT_APPLY_FOR_MATERNITY_LEAVE("A male employee cannot apply for maternity leave"),
    FEMALE_EMPLOYEE_CANT_APPLY_FOR_PATERNITY("A female employee can't apply for paternity leave"),
    EMPLOYEE_DIDNOT_WORK_FOR_REQUIRED_DAYS
            ("Employee did not work for required number of days before asking for blanket coverage leaves"),
    MATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN("Maternity leave can be applied for upto 2 children only"),
    PATERNITY_CAN_ONLY_BE_CLAIMED_FOR_UPTO_2_CHILDREN("Maternity leave can be applied for upto 2 children only"),
    PATERNITY_LEAVE_CANNOT_EXCEED_10("max number of paternity leaves cannot exceed 10"),
    REQUESTED_LATE_FOR_LEAVE("Request for leave was late"),
    NOT_AN_OPTIONAL_LEAVE("This date is not an optional leave"),
    AVAILED_OPTIONAL_LEAVE("Optional leave claimed"),
    OPTIONAL_LEAVE_ALREADY_AVAILED("The other optional leave for the same group was already availed."),
    OPTIONAL_LEAVES_GREATER_THAN_1("Optional leaves cannot be greater than 1");

    private String leaveResponseMessage;
    ReasonsForLeaveResponse(String message) {
        this.leaveResponseMessage = message;
    }
}
