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
import com.example.LeaveManagementSystem.payload.BackendResponse;
import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.payload.LeaveStatusUpdate;
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
    public ResponseEntity<BackendResponse> getAllLeaves(
            @PathVariable(name="managerId") Long managerId,
            Authentication authentication){
        BackendResponse response = new BackendResponse();

        try {
            String loggedMail = authentication.getName();
            
            Long loggedId = managerLeaveApplicationService.getUserIdByEmail(loggedMail);
            
            if (!loggedId.equals(managerId)) {
                response.setMessage("Unauthorized access");
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            logger.info("Fetching all leave applications for managerId: {}", managerId);
            List<LeaveApplicationStatusDto> allLeaves = managerLeaveApplicationService.getAllLeaveApplications(managerId);
            logger.info("Fetched {} leave applications successfully", allLeaves.size());
            
            response.setMessage("Leave applications fetched successfully");
            response.setStatus("success");
            response.setData(allLeaves);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while fetching leave applications", e);
            response.setMessage("An error occurred while fetching leave applications");
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    // Api to get a specific leave application for an employee 
    
    
    @GetMapping("{managerId}/leaves/{employeeId}/leave/{leaveId}")
    public ResponseEntity<BackendResponse> getLeave(
            @PathVariable(name="managerId") Long managerId,
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            Authentication authentication){
        BackendResponse response = new BackendResponse();

        try {
            String loggedMail = authentication.getName();
            
            Long loggedId = managerLeaveApplicationService.getUserIdByEmail(loggedMail);
            
            if (!loggedId.equals(managerId)) {
                response.setMessage("Unauthorized access");
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
           
            logger.info("Fetching leave application with id {} for managerId: {} and employeeId: {}", leaveId, managerId, employeeId);
            
            LeaveApplicationStatusDto leave = managerLeaveApplicationService.getLeaveApplicationById(managerId, employeeId, leaveId);
            
           
            response.setMessage("Leave application fetched successfully");
            response.setStatus("success");
            response.setData(leave);
            
            logger.info("Fetched leave application successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while fetching leave application", e);
            response.setMessage("An error occurred while fetching leave application");
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    
 
    // Api to approve/reject leave for an employee
    
    @PutMapping("{managerId}/leaves/{employeeId}/checkLeave/{leaveId}")
    public ResponseEntity<BackendResponse> approveLeave(
            @PathVariable(name="managerId") Long managerId,
            @PathVariable(name="employeeId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            @RequestBody LeaveStatusUpdate leaveStatusUpdate,
            Authentication authentication
    ) {
        BackendResponse response = new BackendResponse();

        try {
            String loggedMail = authentication.getName();
            
            Long loggedId = managerLeaveApplicationService.getUserIdByEmail(loggedMail);
            
            if (!loggedId.equals(managerId)) {
                response.setMessage("Unauthorized access");
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
           
            logger.info("Approving leave application with id {} for managerId: {} and employeeId: {}", leaveId, managerId, employeeId);
            
            LeaveApplicationStatusDto approvedLeave = managerLeaveApplicationService.updateLeaveApplication(managerId, employeeId, leaveId, leaveStatusUpdate);
            
           
            response.setMessage("Leave application approved successfully");
            response.setStatus("success");
            response.setData(approvedLeave);
            
            logger.info("Leave application approved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while approving leave application", e);
            response.setMessage("An error occurred while approving leave application");
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    

    
}
