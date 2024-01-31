package com.example.LeaveManagementSystem.service;

import java.util.List;

import com.example.LeaveManagementSystem.entity.LeaveApplication;
import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;
import com.example.LeaveManagementSystem.payload.LeaveStatusUpdate;

public interface ManagerLeaveApplicationService {
	
	public List<LeaveApplicationStatusDto> getAllLeaveApplications(Long managerId);
	
	public LeaveApplicationStatusDto getLeaveApplicationById(Long managerId,Long employeeId,Long LeaveApplicationId);
	
	public LeaveApplicationStatusDto updateLeaveApplication(Long managerId,Long employeeId,Long LeaveApplicationId,LeaveStatusUpdate leaveStatusUpdate);
	
	public long getUserIdByEmail(String loggedEmail);
}
