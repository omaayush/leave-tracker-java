package com.hashedin.hu.huLeaveTracker;

enum CompOffProcessStatus {
    INSUFFICIENT_COMPOFF_HOURS("CompOff hours less than minimum compoff hours"),
    SUFFICIENT_COMPOFF_HOURS("CompOff hours greater than or equal to minimum compoff hours"),
    LOG_DOES_NOT_EXIST_FOR_THIS_DATE("Log does not exist for this date"),
    LOG_DOES_NOT_EXIST_FOR_THIS_EMPLOYEE("Log does not exist for this employee"),
    COMPOFF_EXPIRED("Comp off cannot be claimed after one month"),
    COMPOFF_VALID("Comp off is valid"),
    COMPOFF_ALREADY_CLAIMED("Compoff already claimed"),
    COMPOFF_ADDED_TO_EMPLOYEE("compoff added to employee's compoff balance"),
    COMPOFF_IS_ONLY_VALID_FOR_A_HOLIDAY("compoff is only valid for a holiday");


    private String status;

    CompOffProcessStatus(String status) {
        this.status = status;
    }
}