package com.example.LeaveManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.LeaveManagementSystem.entity.Users;

public interface UserRepository  extends JpaRepository<Users,Long>{

	Optional<Users> findByEmail(String email);

}
