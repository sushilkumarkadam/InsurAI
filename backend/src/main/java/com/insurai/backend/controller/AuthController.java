package com.insurai.backend.controller;

import com.insurai.backend.dto.AuthResponseDto;
import com.insurai.backend.dto.LoginDto;
import com.insurai.backend.dto.RegisterDto;
import com.insurai.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterDto registerDto) {
        AuthResponseDto response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        AuthResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody com.insurai.backend.dto.ChangePasswordDto changePasswordDto,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.insurai.backend.entity.User user) {
        authService.changePassword(user.getId(), changePasswordDto);
        return ResponseEntity.ok("Password changed successfully");
    }
}
