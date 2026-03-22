package com.insurai.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PolicyDto {
    private Long id;
    private String policyNumber;
    private String title;
    private String type;
    private String description;
    private BigDecimal coverageAmount;
    private BigDecimal premium;
    private String status;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
    
    // New fields
    private LocalDateTime updatedAt;
    private Long assignedUserId;
    private String assignedUsername; // enriched
}
