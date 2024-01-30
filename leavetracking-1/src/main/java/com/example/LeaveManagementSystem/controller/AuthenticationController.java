package com.example.LeaveManagementSystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeaveManagementSystem.payload.JWTAuthResponse;
import com.example.LeaveManagementSystem.payload.LoginDto;
import com.example.LeaveManagementSystem.payload.UserDto;
import com.example.LeaveManagementSystem.security.JwtTokenProvider;
import com.example.LeaveManagementSystem.service.ManagerService;
import com.example.LeaveManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ManagerService managerService;
    
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    
    // Api for creating an employee
    
    
    @PostMapping("/register/employee")
    public ResponseEntity<UserDto> createUserEmployee(@RequestBody UserDto userDto){
    	
    	
    	if(userDto.getName().trim().isEmpty() || userDto.getEmail().trim().isEmpty() || userDto.getPassword().trim().isEmpty())
    	{
    		logger.info("Incomplete request for registering an employee. Some fields are missing.");
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
      
        UserDto createdEmployee = userService.createEmployee(userDto);
        logger.info("Employee registration successful: {}", createdEmployee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    
    
    // Api for creating a manager
    
    
    @PostMapping("/register/manager")
    public ResponseEntity<UserDto> createManager(@RequestBody UserDto userDto){
    	


    	if(userDto.getName().trim().isEmpty() || userDto.getEmail().trim().isEmpty() || userDto.getPassword().trim().isEmpty())
    	{
    		logger.info("BAD_REQUEST while registering user some fields missed");
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        
        UserDto createdManager = managerService.createManager(userDto);
        logger.info("Manager created successfully");
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }
    
    // Api for user login
    //JWTAuthResponse consists token,tokentype
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> loginuser(@RequestBody LoginDto loginDto){
        
        try {
            // Authenticate the user using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            
            // Set the authentication in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate a JWT token using JwtTokenProvider
            String token = jwtTokenProvider.generateToken(authentication);
            
            // Return the JWT token in a JWTAuthResponse and HTTP status 200 (OK)
            return ResponseEntity.ok(new JWTAuthResponse(token));
            
        } catch (Exception e) {
            logger.error("Exception during login: {}", e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
