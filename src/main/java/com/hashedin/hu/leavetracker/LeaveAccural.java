package com.hashedin.hu.leavetracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

class LeaveAccrual {

    EmployeeStore employeeStore;
    LeavesStore leavesStore;
    LocalDate updateDate;

    public LeaveAccrual(EmployeeStore employeeDao, LeavesStore leaveDao, LocalDate updateDate) {
        this.employeeStore = employeeDao;
        this.leavesStore = leaveDao;
        this.updateDate = updateDate;
    }

    public void creditLeaves(LocalDate updateDate) {
        // assuming the update date is first of every month

        //HashSet<Employee> employees = employeeStore.getEmployees();
        ArrayList<Employee> employees = employeeStore.getEmployees();
        ArrayList<LeaveGrant> leaveGrants = leavesStore.getLeaves();
        LocalDate joiningDate;

        // month's start and end dates
        LocalDate previousMonthStart = updateDate.minusMonths(1).with(firstDayOfMonth());
        LocalDate previousMonthEnd = updateDate.minusMonths(1).with(lastDayOfMonth());

        Iterator<Employee> iter = this.employeeStore.getEmployees().iterator();

        while (iter.hasNext()) {
//        for (Employee employee: employees) {
            Employee employee = iter.next();
            joiningDate = employee.getDateOfJoining();
            int leaveIncrement = 2;
            // for employees who joined last month joining.month == update.getLastMonth
            boolean hasJoinedPreviousDecember = false;
            // for january of every year
            if (updateDate.getMonthValue() == 1 && joiningDate.getMonthValue() == 12 &&
                    joiningDate.getYear() == updateDate.getYear() - 1) {
                hasJoinedPreviousDecember = true;
            }
            if (hasJoinedPreviousDecember || (joiningDate.getMonth() == updateDate.getMonth().minus(1))
                    && (joiningDate.getYear() == updateDate.getYear())) {
                // employee has joined previous month only
                if ((joiningDate.getDayOfMonth() > 15)) {
                    leaveIncrement = 1;
                } else {
                    leaveIncrement = 2;
                }
            } else {
                // check for employees on maternity or sabbatical leaves
                for (LeaveGrant grant : leaveGrants) {
                    if (grant.getEmployeeId() == employee.getId() &&
                            grant.getLeaveType() == LeaveType.MATERNITY ||

                            grant.getLeaveType() == LeaveType.SABBATICAL) {

                        if (grant.getStartDate().isAfter(previousMonthStart) &&
                                grant.getStartDate().isBefore(previousMonthEnd) ||
                                grant.getEndDate().isBefore(previousMonthEnd) &&
                                        grant.getStartDate().isAfter(previousMonthStart)) {
                            leaveIncrement = 1;
                        } else if (grant.getStartDate().isBefore(previousMonthStart) &&
                                grant.getEndDate().isAfter(previousMonthEnd)) {
                            leaveIncrement = 0;
                        }
                    }
                }
            }
            employee.setGeneralLeaves(employee.getGeneralLeaves() + leaveIncrement);
            employees.add(employee);

        }
        employeeStore.setEmployees(employees);
    }
}
