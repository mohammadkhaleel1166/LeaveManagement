package com.example.LeaveManagementSystem.payload;

import java.time.LocalDate;

import lombok.Data;

@Data

public class LeaveApplicationDto {
	private Long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
}
