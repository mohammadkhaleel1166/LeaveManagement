package com.example.LeaveManagementSystem.payload;

import lombok.Data;

@Data

public class JWTAuthResponse {
	private String token;
	private String tokenType="Bearer";
	public JWTAuthResponse(String token) {
		this.token=token;
	}
}
