package com.hashedin.hu.huLeaveTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaveRequestController {



    @Autowired
    private LeaveManager leaveManager;

    @Autowired
    public LeaveRequestController(LeaveManager leaveManager){

        this.leaveManager = leaveManager;


    }

    @RequestMapping("/leaves")
    public List<LeaveRequest> getAllLeaveRequests() {
        return this.leaveManager.getAllLeaveRequests();
    }

    @RequestMapping(value = "/leaves/apply", method = RequestMethod.POST)
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveRequest request) {
        return new ResponseEntity<LeaveResponse>(this.leaveManager.apply(request), HttpStatus.OK);
    }
}
