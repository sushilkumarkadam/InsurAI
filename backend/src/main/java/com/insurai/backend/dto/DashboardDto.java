package com.insurai.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDto {
    private long totalPolicies;
    private long totalClaims;
    private long pendingClaims;
    private long approvedClaims;
    private long rejectedClaims;
    private long totalUsers;
    private long fraudAlerts;
    private BigDecimal totalRevenue; // sum of approved claim amounts
}
