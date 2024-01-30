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
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.payload.LeaveStatus;
import com.example.LeaveManagementSystem.repository.LeaveApplicationRepository;
import com.example.LeaveManagementSystem.repository.UserRepository;
import com.example.LeaveManagementSystem.service.ManagerLeaveApplicationService;

@Service
public class ManagerLeaveApplicationServiceImpl implements ManagerLeaveApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerLeaveApplicationServiceImpl.class);

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepo;
    

    // Api to get all leave applications for a manager and employee
    
    
    @Override
    public List<LeaveApplicationStatusDto> getAllLeaveApplications(Long managerId) {
        
        List<LeaveApplication> leaveApplications = leaveApplicationRepo.findAll();

       
        List<LeaveApplicationStatusDto> leaveApplicationStatusDtos = leaveApplications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        logger.info("Retrieved {} leave applications for managerId: {}",
                leaveApplicationStatusDtos.size(), managerId);

        return leaveApplicationStatusDtos;
    }
    
    

    // Api method to get a specific leave application by its ID
    
    
    @Override
    public LeaveApplicationStatusDto getLeaveApplicationById(Long managerId, Long employeeId, Long leaveApplicationId) {
       
        LeaveApplication leaveApplication = leaveApplicationRepo.findByIdAndEmployeeId(leaveApplicationId, employeeId)
                .orElseThrow(() -> new LeaveNotFound(String.format(
                        "Leave application with id %d for employee id %d not found", leaveApplicationId, employeeId)));

    
        LeaveApplicationStatusDto leaveApplicationStatusDto = convertToDto(leaveApplication);

        logger.info("Retrieved leave application by ID: {} for managerId: {} and employeeId: {}",
                leaveApplicationId, managerId, employeeId);

        return leaveApplicationStatusDto;
    }

    
    
    // Api method to update the status of a leave application
    
    
    @Override
    public LeaveApplicationStatusDto updateLeaveApplication(Long managerId, Long employeeId, Long leaveApplicationId,String managerComment) {
       
        LeaveApplication leaveApplication = leaveApplicationRepo.findByIdAndEmployeeId(leaveApplicationId, employeeId)
                .orElseThrow(() -> new LeaveNotFound(String.format(
                        "Leave application with id %d for employee id %d not found", leaveApplicationId, employeeId)));
     
        if (leaveApplication.getStatus() == LeaveStatus.PENDING) {
            // Check the required fields (startDate, endDate, reason) are filled
            if (leaveApplication.getStartDate() == null || leaveApplication.getEndDate() == null
                    || leaveApplication.getReason().trim().isEmpty()) {
                leaveApplication.setStatus(LeaveStatus.REJECTED);
            } else {
                leaveApplication.setStatus(LeaveStatus.APPROVED);
            }
            leaveApplication.setManagerComment(managerComment);
          
            LeaveApplication savedLeaveApplication = leaveApplicationRepo.save(leaveApplication);

            LeaveApplicationStatusDto updatedLeaveApplicationStatusDto = convertToDto(savedLeaveApplication);

            logger.info("Updated leave application status. Leave application ID: {}, Manager ID: {}, Employee ID: {}",
                    leaveApplicationId, managerId, employeeId,managerComment);

            return updatedLeaveApplicationStatusDto;
        }

        return convertToDto(leaveApplication);
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
