package com.example.LeaveManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.LeaveManagementSystem.entity.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication,Long> {

	
	Optional<LeaveApplication> findByIdAndEmployeeId(Long leaveId, Long employeeId);
}
