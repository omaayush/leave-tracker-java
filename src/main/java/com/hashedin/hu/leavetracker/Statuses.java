package com.hashedin.hu.leavetracker;

enum Gender {
    MALE, FEMALE
}

enum LeaveStatus {

    ACCEPTED, PENDING, REJECTED
}

enum LeaveType {
    GENERAL, SABBATICAL, MATERNITY, PATERNITY, COMPOFF
}

//enum CurrentLeaves{
//    GENERAL,
//    SABBATICAL,
//    MATERNITY,
//    PATERNITY,
//    COMPOFF,
//    OPTIONAL
//}
enum LeaveResponses {
    LEAVE_BALANCE_INSUFFICIENT("Insufficient Leave Balance"),
    GENERAL_LEAVE_BALANCE_INSUFFICIENT("General Leave Balance Insufficient"),
    SABBATICAL_LEAVE_BALANCE_INSUFFICIENT("Sabbatical Leaves not allowed"),
    MATERNITY_LEAVE_BALANCE_INSUFFICIENT("Maternity Leave not allowed"),
    PATERNITY_LEAVE_BALANCE_INSUFFICIENT("Paternity Leave not allowed"),
//    COMPOFF_LEAVE_BALANCE_INSUFFICIENT("CompOff Leave Balance Insufficient"),
    LEAVE_APPROVED("Leave request meets all the criteria"),
//    NULL_DATE("Date Cannot be null"),
//    START_DATE_END_DATE_CONFLICT("Start date cannot be after end date"),
//    PUBLIC_HOLIDAY("It is already a holiday"),
//    ALREADY_APPLIED("Already applied for leave in that duration"),
    VALID_BLANKET_COVERAGE("Blanket coverage request IS VALID"),
//    INVALID_BLANKET_COVERAGE("Blanket coverage request IS INVALID"),
    MATERNITY_LEAVE_FOR_MALE_NOTALLOWED("A male employee cannot apply for maternity leave"),
//    PATERNITY_LEAEVE_FOR_FEMALE_NOT_ALLOWED("A female employee can't apply for paternity leave"),
    EMPLOYEE_WORK_DAYS_LESS("Employee did not work " +
            "for required number of days before asking for blanket " +
            "coverage leaves"),
    MATERNITY_LEAVE_UPTO_2_CHILDREN("Maternity leave can be applied for upto 2 children only"),
//    PATERNITY_LEAVE_UPTO_2_CHILDREN("Maternity leave can be applied for upto 2 children only"),
//    PATERNITY_LEAVE_10_ALLOWED("max number of paternity leaves cannot exceed 10"),
    LATE_REQUEST("Request for leave was late");


    private String responseMessages;
    LeaveResponses(String message) {
        this.responseMessages = message;
    }
}

//enum CompOffStatus {
//    INSUFFICIENT_HOURS("CompOff hours less than minimum compOff hours"),
//    SUFFICIENT_HOURS("CompOff hours greater than or equal to minimum compOff hours"),
//    NO_COMPOFF_ON_THIS_DATE("CompOff not approved within this week"),
//    APPROVED_FOR_THIS_DATE("CompOff for this date permissible");
//    private String status;
//
//    CompOffStatus(String status) {
//        this.status = status;
//    }
//}

enum LogDatabaseStatus {
    LOG_ADDED("Log Work successfully added to database");

    private String logWorkStatus;
    LogDatabaseStatus(String status) {
        this.logWorkStatus = status;
    }
}



