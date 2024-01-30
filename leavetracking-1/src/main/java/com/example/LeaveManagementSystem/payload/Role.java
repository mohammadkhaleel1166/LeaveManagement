package com.example.LeaveManagementSystem.payload;

public enum Role {

	EMPLOYEE,
	MANAGER;
	
	public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
