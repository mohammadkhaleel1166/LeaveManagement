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
import com.example.LeaveManagementSystem.payload.BackendResponse;
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
    public ResponseEntity<BackendResponse> applyForLeave(@PathVariable(name="userId") Long employeeId,
            @RequestBody LeaveApplicationDto leaveApplicationDto, Authentication authentication) {
        BackendResponse response = new BackendResponse();

        try {
            String loggedMail = authentication.getName();
            Long loggedId = employeeLeaveApplicationService.getUserIdByEmail(loggedMail);

            if (!loggedId.equals(employeeId)) {
                response.setMessage("Unauthorized access");
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            logger.info("Received request to apply for leave - userId: {}, leaveApplicationDto: {}", employeeId, leaveApplicationDto);

           
            if (leaveApplicationDto.getStartDate() == null || leaveApplicationDto.getEndDate() == null || leaveApplicationDto.getReason() == null || leaveApplicationDto.getReason().isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Fields ");
                if (leaveApplicationDto.getStartDate() == null) {
                    errorMessage.append("'startDate', ");
                }
                if (leaveApplicationDto.getEndDate() == null) {
                    errorMessage.append("'endDate', ");
                }
                if (leaveApplicationDto.getReason() == null || leaveApplicationDto.getReason().isEmpty()) {
                    errorMessage.append("'reason', ");
                }
                errorMessage.append("are mandatory");
                response.setMessage(errorMessage.toString());
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            LeaveApplicationStatusDto createdLeaveDto = employeeLeaveApplicationService.applyForLeave(employeeId, leaveApplicationDto);

            logger.info("Leave application submitted successfully - leaveApplicationDto: {}", createdLeaveDto);
            response.setMessage("Leave application submitted successfully");
            response.setStatus("success");
//            response.setData("");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error while accessing leave application", e);
            response.setMessage("An error occurred while processing the leave application");
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    // Api to track leave application using leaveId of an employee
    
    
    @GetMapping("{userId}/leaves/{leaveId}")
    public ResponseEntity<BackendResponse> getLeave(
            @PathVariable(name="userId") Long employeeId,
            @PathVariable(name="leaveId") Long leaveId,
            Authentication authentication
            ){
        BackendResponse response = new BackendResponse();
        
       

        try {
            String loggedMail = authentication.getName();
            
            Long loggedId = employeeLeaveApplicationService.getUserIdByEmail(loggedMail);
            
            if (!loggedId.equals(employeeId)) {
                response.setMessage("Unauthorized access");
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            logger.info("Received request to get leave - userId: {}, leaveId: {}", employeeId, leaveId);
            LeaveApplicationStatusDto leave = employeeLeaveApplicationService.getLeaveApplicationById(employeeId, leaveId);
           
            
            response.setMessage("Leave details retrieved successfully");
            response.setStatus("success");
            response.setData(leave);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting leave application details", e);
            response.setMessage(e.getMessage());
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
