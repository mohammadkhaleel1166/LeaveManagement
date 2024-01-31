package com.example.LeaveManagementSystem.serviceImpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.LeaveManagementSystem.entity.Users;
import com.example.LeaveManagementSystem.payload.Role;
import com.example.LeaveManagementSystem.payload.UserDto;
import com.example.LeaveManagementSystem.repository.UserRepository;
import com.example.LeaveManagementSystem.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDto createEmployee(UserDto userDto) {
        logger.info("Creating employee: {}", userDto);
        
        
        if (!userDto.getPassword().isEmpty()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
           
            logger.warn("Password field is empty. Please provide a valid password.");
             }
        
        Users employee = modelMapper.map(userDto, Users.class);
        employee.setRole(Role.EMPLOYEE);
        logger.debug("Saving employee: {}", employee);
        Users savedEmployee = userRepository.save(employee);
        logger.info("Employee created: {}", savedEmployee);
        return modelMapper.map(savedEmployee, UserDto.class);
    }
	@Override
	public boolean isUserWithEmailExists(String email) {
		 Optional<Users> user = userRepository.findByEmail(email);
	        return user.isPresent();
	}
}
