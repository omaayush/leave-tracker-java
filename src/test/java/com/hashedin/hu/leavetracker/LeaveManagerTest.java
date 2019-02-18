package com.hashedin.hu.leavetracker;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

public class LeaveManagerTest {

    @Test
    public void testDurationLessThan20days() {
        LeaveManager manager=new LeaveManager();
        LeaveRequest request=new LeaveRequest(today(),tomorrow(), 1);
        LeaveResponse response=manager.applyForLeave(request);
        assertEquals("Start date is after end date",response.getLeaveStatus(),LeaveStatus.ACCEPTED);
    }

    private LocalDate today() {
        return LocalDate.now();
    }
    private LocalDate tomorrow() {
    return LocalDate.now().plusDays(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionTest(){
        LeaveManager manager=new LeaveManager();
        LeaveRequest request=new LeaveRequest(tomorrow(),today(), 1);
        LeaveResponse response=manager.applyForLeave(request);
        //assertEquals("Exception Occurred",response);
    }
}