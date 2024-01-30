package com.example.LeaveManagementSystem.payload;

import lombok.Data;

@Data

public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String password;
}
