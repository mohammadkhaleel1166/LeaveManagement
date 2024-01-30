package com.example.LeaveManagementSystem.service;

import java.util.List;

import com.example.LeaveManagementSystem.payload.LeaveApplicationDto;
import com.example.LeaveManagementSystem.payload.LeaveApplicationStatusDto;

public interface EmployeeLeaveApplicationService {
	
	public LeaveApplicationStatusDto applyForLeave(Long employeeId,LeaveApplicationDto leaveApplicationDto);
	public LeaveApplicationStatusDto getLeaveApplicationById(Long employeeId,Long LeaveApplicationId);
	
	public long getUserIdByEmail(String loggedEmail);
}
