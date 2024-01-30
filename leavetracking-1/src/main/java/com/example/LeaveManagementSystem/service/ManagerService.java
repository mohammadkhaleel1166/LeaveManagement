package com.example.LeaveManagementSystem.service;

import com.example.LeaveManagementSystem.payload.ManagerDto;
import com.example.LeaveManagementSystem.payload.UserDto;

public interface ManagerService {
	public UserDto createManager(UserDto userDto);
}
