package com.insurai.backend.service.impl;

import com.insurai.backend.dto.AuthResponseDto;
import com.insurai.backend.dto.LoginDto;
import com.insurai.backend.dto.RegisterDto;
import com.insurai.backend.dto.UserDto;
import com.insurai.backend.entity.Role;
import com.insurai.backend.entity.User;
import com.insurai.backend.exception.BadRequestException;
import com.insurai.backend.repository.UserRepository;
import com.insurai.backend.security.JwtTokenProvider;
import com.insurai.backend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponseDto register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Email is already taken!");
        }

        Role role = Role.EMPLOYEE;
        if (registerDto.getRole() != null) {
            try {
                role = Role.valueOf(registerDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Default to EMPLOYEE if invalid
                role = Role.EMPLOYEE;
            }
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(role)
                .phone(registerDto.getPhone())
                .department(registerDto.getDepartment())
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponseDto(null, mapToDto(savedUser));
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return new AuthResponseDto(token, mapToDto(user));
    }

    @Override
    public void changePassword(Long userId, com.insurai.backend.dto.ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
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
