package com.example.LeaveManagementSystem.payload;

import java.util.Objects;

import lombok.Data;

@Data

public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String password;
	
	
}
