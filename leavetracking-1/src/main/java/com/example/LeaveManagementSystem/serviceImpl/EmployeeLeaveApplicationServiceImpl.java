package com.example.LeaveManagementSystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LeaveManagementSystem.entity.LeaveApplication;
import com.example.LeaveManagementSystem.entity.Users;
import com.example.LeaveManagementSystem.exception.LeaveNotFound;
import com.example.LeaveManagementSystem.exception.UserNotFound;
import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.payload.LeaveStatus;
import com.example.LeaveManagementSystem.repository.LeaveApplicationRepository;
import com.example.LeaveManagementSystem.repository.UserRepository;
import com.example.LeaveManagementSystem.service.EmployeeLeaveApplicationService;

@Service
public class EmployeeLeaveApplicationServiceImpl implements EmployeeLeaveApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeLeaveApplicationServiceImpl.class);

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepo;
    
    
    // Api method to apply for leave
    
    
    @Override
    public LeaveApplicationStatusDto applyForLeave(Long employeeId, LeaveApplicationDto leaveApplicationDto) {
        logger.info("Applying for leave - Employee ID: {}, Leave Application DTO: {}", employeeId, leaveApplicationDto);
   //checking for employee present or not in db
        Users employee = userRepo.findById(employeeId).orElseThrow(
                () -> new UserNotFound(String.format("Employee with ID %d not found", employeeId)));

        LeaveApplication leaveApplication = modelMapper.map(leaveApplicationDto, LeaveApplication.class);
        leaveApplication.setStatus(LeaveStatus.PENDING);
        leaveApplication.setEmployee(employee);
        LeaveApplication createdLeave = leaveApplicationRepo.save(leaveApplication);

        logger.info("Leave application submitted successfully - Employee ID: {}, Leave Application ID: {}",
                employeeId, createdLeave.getId());
        return modelMapper.map(createdLeave, LeaveApplicationStatusDto.class);
    }



    // Api method to track a  specific leave application by its ID
    
    
    
    @Override
    public LeaveApplicationStatusDto getLeaveApplicationById(Long employeeId, Long leaveApplicationId) {
        logger.info("Fetching leave application by ID - Employee ID: {}, Leave Application ID: {}", employeeId, leaveApplicationId);

        
        LeaveApplication leavesApplication = leaveApplicationRepo.findByIdAndEmployeeId(leaveApplicationId, employeeId)
                .orElseThrow(() -> new LeaveNotFound(String.format(
                        "Leave application with ID %d for employee ID %d not found", leaveApplicationId, employeeId)));

      LeaveApplicationStatusDto leaveApplicationStatusDto=convertToDto(leavesApplication);
     leaveApplicationStatusDto.setEmployeeId(employeeId);
        return leaveApplicationStatusDto ;
    }

    private LeaveApplicationStatusDto convertToDto(LeaveApplication leaveApplication) {
        return modelMapper.map(leaveApplication, LeaveApplicationStatusDto.class);
    }

	@Override
	public long getUserIdByEmail(String loggedEmail) {
		
		
		Users user=userRepo.findByEmail(loggedEmail).orElseThrow(
				()-> new UserNotFound(String.format("Employee with email %s not found", loggedEmail)));
				
		return user.getId();
	}
    
    
}
