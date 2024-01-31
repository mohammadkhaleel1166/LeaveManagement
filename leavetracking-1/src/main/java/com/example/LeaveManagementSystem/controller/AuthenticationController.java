package com.example.LeaveManagementSystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeaveManagementSystem.payload.BackendResponse;
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
    public ResponseEntity<BackendResponse> createUserEmployee(@RequestBody UserDto userDto){
    	BackendResponse response=new BackendResponse();  	
if (userDto == null || userDto.getName() == null || userDto.getName().isEmpty() || userDto.getEmail() == null ||  userDto.getEmail().isEmpty()|| userDto.getPassword() == null ||  userDto.getPassword().isEmpty())
{

 StringBuilder errorMessage = new StringBuilder("Fields ");
                if (userDto.getName() == null || userDto.getName().isEmpty()) {
                    errorMessage.append("'name', ");
                }
                if (userDto.getEmail() == null ||  userDto.getEmail().isEmpty()) {
                    errorMessage.append("'Email', ");
                }
                if (userDto.getPassword() == null ||  userDto.getPassword().isEmpty()) {
                    errorMessage.append("'password', ");
                }
                errorMessage.append("are mandatory");
                response.setMessage(errorMessage.toString());
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}

if (userService.isUserWithEmailExists(userDto.getEmail())) {
    response.setMessage("Email already exists");
    response.setStatus("fail");
    response.setData("empty");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
      
        UserDto createdEmployee = userService.createEmployee(userDto);
        logger.info("Employee registration successful: {}", createdEmployee);
        response.setMessage("registration successful");
		response.setStatus("success");
		response.setData(createdEmployee);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    // Api for creating a manager
    
    
    @PostMapping("/register/manager")
    public ResponseEntity<BackendResponse> createManager(@RequestBody UserDto userDto){
    	BackendResponse response=new BackendResponse();

if (userDto == null || userDto.getName() == null || userDto.getName().isEmpty() || userDto.getEmail() == null ||  userDto.getEmail().isEmpty()|| userDto.getPassword() == null ||  userDto.getPassword().isEmpty())
{

 StringBuilder errorMessage = new StringBuilder("Fields ");
                if (userDto.getName() == null || userDto.getName().isEmpty()) {
                    errorMessage.append("'name', ");
                }
                if (userDto.getEmail() == null ||  userDto.getEmail().isEmpty()) {
                    errorMessage.append("'Email', ");
                }
                if (userDto.getPassword() == null ||  userDto.getPassword().isEmpty()) {
                    errorMessage.append("'password', ");
                }
                errorMessage.append("are mandatory");
                response.setMessage(errorMessage.toString());
                response.setStatus("fail");
                response.setData("empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}

if (userService.isUserWithEmailExists(userDto.getEmail())) {
    response.setMessage("Email already exists");
    response.setStatus("fail");
    response.setData("empty");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
        UserDto createdManager = managerService.createManager(userDto);
        logger.info("Manager created successfully");
        response.setMessage("registration successful");
     		response.setStatus("success");
     		response.setData(createdManager);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Api for user login
    //JWTAuthResponse consists token,tokentype
    
    @PostMapping("/login")
    public ResponseEntity<BackendResponse> loginUser(@RequestBody LoginDto loginDto) {
        BackendResponse response = new BackendResponse();

        try {
           
            if (loginDto.getEmail() == null || loginDto.getEmail().isEmpty()) {
                if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
                    throw new BadCredentialsException("Email and password are missing");
                } else {
                    throw new BadCredentialsException("Email is missing");
                }
            } else if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
                throw new BadCredentialsException("Password is missing");
            }

            // Authenticate the user using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            
            // Set the authentication in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate a JWT token using JwtTokenProvider
            String token = jwtTokenProvider.generateToken(authentication);
            
            // Return the JWT token in a JWTAuthResponse and HTTP status 200 (OK)
            response.setMessage("Login successful");
            response.setStatus("success");
            response.setData(new JWTAuthResponse(token));
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            logger.error("Incorrect credentials during login: {}", e.getMessage());
            response.setMessage( e.getMessage());
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            logger.error("Exception during login: {}", e.getMessage());
            response.setMessage("An error occurred during login");
            response.setStatus("fail");
            response.setData("empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
