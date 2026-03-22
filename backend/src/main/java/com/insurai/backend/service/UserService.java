package com.insurai.backend.service;

import com.insurai.backend.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUserRole(Long id, String role);
    UserDto createUser(com.insurai.backend.dto.RegisterDto dto);
    UserDto updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
}
