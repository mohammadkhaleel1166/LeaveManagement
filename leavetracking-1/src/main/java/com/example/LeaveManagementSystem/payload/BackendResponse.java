package com.example.LeaveManagementSystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BackendResponse {
	
	private String message;
	private String status;
	private Object data;


}
