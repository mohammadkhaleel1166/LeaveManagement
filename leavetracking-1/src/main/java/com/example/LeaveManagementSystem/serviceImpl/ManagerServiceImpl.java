package com.example.LeaveManagementSystem.serviceImpl;

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
import com.example.LeaveManagementSystem.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

    public static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createManager(UserDto userDto) {
        logger.info("Creating manager: {}", userDto);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Users manager = modelMapper.map(userDto, Users.class);
        manager.setRole(Role.MANAGER);
        logger.debug("Saving manager: {}", manager);
        Users savedManager = userRepo.save(manager);
        logger.info("Manager created: {}", savedManager);
        return modelMapper.map(savedManager, UserDto.class);
    }
}
