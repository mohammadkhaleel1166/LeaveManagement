package com.example.LeaveManagementSystem.payload;

import java.time.LocalDate;

import com.example.LeaveManagementSystem.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LeaveApplicationStatusDto {
	private Long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
	private LeaveStatus status;
	private  String comment;
	private Long employeeId;
	
}
