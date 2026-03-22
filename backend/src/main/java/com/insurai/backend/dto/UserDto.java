package com.insurai.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String phone;
    private String department;
    private LocalDateTime createdAt;
}
