package com.insurai.backend.service.impl;

import com.insurai.backend.dto.UserDto;
import com.insurai.backend.entity.Role;
import com.insurai.backend.entity.User;
import com.insurai.backend.exception.ResourceNotFoundException;
import com.insurai.backend.repository.UserRepository;
import com.insurai.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToDto(user);
    }

    @Override
    public UserDto updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided");
        }
        
        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Override
    public UserDto createUser(com.insurai.backend.dto.RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new com.insurai.backend.exception.BadRequestException("Username is already taken!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new com.insurai.backend.exception.BadRequestException("Email is already taken!");
        }

        Role role = Role.EMPLOYEE;
        if (dto.getRole() != null) {
            try {
                role = Role.valueOf(dto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                role = Role.EMPLOYEE;
            }
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .phone(dto.getPhone())
                .department(dto.getDepartment())
                .build();

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) user.setEmail(dto.getEmail());
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            try {
                user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {}
        }
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getDepartment() != null) user.setDepartment(dto.getDepartment());

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setPhone(user.getPhone());
        dto.setDepartment(user.getDepartment());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
