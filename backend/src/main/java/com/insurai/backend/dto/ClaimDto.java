package com.insurai.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimDto {
    private Long id;
    private Long userId;
    private String username;     // enriched from User entity for display
    private Long policyId;
    private String policyNumber; // enriched from Policy entity for display
    private String policyType;   // enriched from Policy entity for display
    private BigDecimal amount;
    private String reason;
    private String status;
    private LocalDateTime dateSubmitted;
    private Integer riskScore;
    
    // New fields
    private String claimType;
    private String notes;
    private String policyTitle;  // enriched
    private LocalDateTime dateApproved;
    private Long assignedStaffId;
    private String assignedStaffName; // enriched
}
