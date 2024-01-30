package com.example.LeaveManagementSystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeaveManagementSystem.entity.LeaveApplication;
import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.service.EmployeeLeaveApplicationService;

@RestController
@RequestMapping("/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeLeaveApplicationService employeeLeaveApplicationService;
    
    
    // Api for an employee to apply for leave
    
    
    @PostMapping("/{userId}/apply")
    public ResponseEntity<LeaveApplicationStatusDto> applyForLeave(@PathVariable(name="userId") Long employeeId,@RequestBody LeaveApplicationDto leaveApplicationDto,Authentication authentication){
    	
    	try {
    		String loggedMail=authentication.getName();
        	
        	Long loggedId=employeeLeaveApplicationService.getUserIdByEmail(loggedMail);
        	
        	if(!loggedId.equals(employeeId)) {
        		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        	}
            logger.info("Received request to apply for leave - userId: {}, leaveApplicationDto: {}", employeeId, leaveApplicationDto);

            LeaveApplicationStatusDto createdLeaveDto = employeeLeaveApplicationService.applyForLeave(employeeId, leaveApplicationDto);

            logger.info("Leave application submitted successfully - leaveApplicationDto: {}", createdLeaveDto);

            return new ResponseEntity<>(createdLeaveDto, HttpStatus.CREATED);
    	}catch (Exception e) {
            logger.error("Error while accessing  leave application", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    
    
    
    
    
    //API to TRACK a specific leave application for a user
    
    
    @GetMapping("{userId}/leaves/{leaveId}")
    public ResponseEntity<LeaveApplicationStatusDto> getLeave(
            @PathVariable(name="userId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            Authentication authentication
            ){
    	try {
    		String loggedMail=authentication.getName();
        	
        	Long loggedId=employeeLeaveApplicationService.getUserIdByEmail(loggedMail);
        	
        	if(!loggedId.equals(employeeId)) {
        		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        	}
            logger.info("Received request to get leave - userId: {}, leaveId: {}", employeeId, leaveId);
            LeaveApplicationStatusDto leave = employeeLeaveApplicationService.getLeaveApplicationById(employeeId, leaveId);
           
            return new ResponseEntity<>(leave, HttpStatus.OK);
    	}catch (Exception e) {
            logger.error("Error while approving leave applications", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
