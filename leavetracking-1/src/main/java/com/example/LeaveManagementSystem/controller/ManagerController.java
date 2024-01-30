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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeaveManagementSystem.entity.LeaveApplication;
import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.service.EmployeeLeaveApplicationService;
import com.example.LeaveManagementSystem.service.ManagerLeaveApplicationService;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    
    @Autowired
    private EmployeeLeaveApplicationService employeeLeaveApplicationService;
    
    @Autowired
    private ManagerLeaveApplicationService managerLeaveApplicationService;
    
    
    // Api to get all leave applications
    
    
    @GetMapping("{managerId}/leaves")
    public ResponseEntity<List<LeaveApplicationStatusDto>> getAllLeaves(
            @PathVariable(name="managerId") Long managerId,
            Authentication authentication
    ){
    	try {
    		
    		String loggedMail=authentication.getName();
        	
        	Long loggedId=managerLeaveApplicationService.getUserIdByEmail(loggedMail);
        	
        	if(!loggedId.equals(managerId)) {
        		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        	}
        	
            logger.info("Fetching all leave applications for managerId: {}", managerId);
            List<LeaveApplicationStatusDto> allLeaves = managerLeaveApplicationService.getAllLeaveApplications(managerId);
            logger.info("Fetched {} leave applications successfully", allLeaves.size());
            
            return new ResponseEntity<>(allLeaves, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while fetching leave applications", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    // Api to get a specific leave application for an employee 
    
    
    @GetMapping("{managerId}/leaves/{employeeId}/leave/{leaveId}")
    public ResponseEntity<LeaveApplicationStatusDto> getLeave(
            @PathVariable(name="managerId") Long managerId,
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            Authentication authentication
    ){
    	try {
    		
    		String loggedMail=authentication.getName();
        	
        	Long loggedId=managerLeaveApplicationService.getUserIdByEmail(loggedMail);
        	
        	if(!loggedId.equals(managerId)) {
        		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        	}
           
    		logger.info("Fetching leave application with id {} for managerId: {} and employeeId: {}", leaveId, managerId, employeeId);
    		
    		
            LeaveApplicationStatusDto leave = managerLeaveApplicationService.getLeaveApplicationById(managerId, employeeId, leaveId);
            logger.info("Fetched leave application successfully");
            return new ResponseEntity<>(leave, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while fetching leave application", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
 
    // Api to approve/reject leave for an employee
    //status approve or reject decided by LeaveApplication fields if any one of them empty makes it rejected
    
    
    @PutMapping("{managerId}/leaves/{employeeId}/checkLeave/{leaveId}")
    public ResponseEntity<LeaveApplicationStatusDto> approveLeave(
            @PathVariable(name="managerId") Long managerId,
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            @RequestBody String managerComment,
            Authentication authentication
    ) {
    	try {
    		
    		String loggedMail=authentication.getName();
        	
        	Long loggedId=managerLeaveApplicationService.getUserIdByEmail(loggedMail);
        	
        	if(!loggedId.equals(managerId)) {
        		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        	}
           
    		logger.info("Approving leave application with id {} for managerId: {} and employeeId: {}", leaveId, managerId, employeeId);
           
            LeaveApplicationStatusDto approvedLeave = managerLeaveApplicationService.updateLeaveApplication(managerId, employeeId, leaveId,managerComment);

            logger.info("Leave application approved successfully");
           
            return new ResponseEntity<>(approvedLeave, HttpStatus.OK);
    	}catch (Exception e) {
            logger.error("Error while approving leave application", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
