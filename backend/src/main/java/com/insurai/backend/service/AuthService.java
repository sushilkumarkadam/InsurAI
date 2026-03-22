package com.insurai.backend.service;

import com.insurai.backend.dto.AuthResponseDto;
import com.insurai.backend.dto.LoginDto;
import com.insurai.backend.dto.RegisterDto;

public interface AuthService {
    AuthResponseDto register(RegisterDto registerDto);
    AuthResponseDto login(LoginDto loginDto);
    void changePassword(Long userId, com.insurai.backend.dto.ChangePasswordDto changePasswordDto);
}
