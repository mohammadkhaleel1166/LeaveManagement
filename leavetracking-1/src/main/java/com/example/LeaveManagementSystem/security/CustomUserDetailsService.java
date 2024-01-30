package com.example.LeaveManagementSystem.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.LeaveManagementSystem.entity.Users;
import com.example.LeaveManagementSystem.exception.UserNotFound;
import com.example.LeaveManagementSystem.payload.Role;
import com.example.LeaveManagementSystem.repository.UserRepository;

@Configuration
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
    @Autowired
    private UserRepository userRepo;

    // This method is called to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
        	
        	// Retrieve user details from the database
            Users employee = userRepo.findByEmail(email).orElseThrow(
                    () -> new UserNotFound(String.format("User with email %s not found", email))
            );
            
            logger.info("User details retrieved for email: {}", email);

            // Create a UserDetails object with the user's email, password, and authorities (roles)
            return new User(employee.getEmail(), employee.getPassword(), getAuthorities(employee.getRole()));
        }catch(Exception e) {
        	
        	logger.error("Error loading user details for email: {}", email, e);
            throw e;
        }
    }

    // This method provides authorities (roles) for the user
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        // For simplicity, returning a single authority (role) based on the user's role
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }
}
