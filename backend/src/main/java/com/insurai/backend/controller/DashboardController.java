package com.insurai.backend.controller;

import com.insurai.backend.dto.DashboardDto;
import com.insurai.backend.entity.User;
import com.insurai.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardDto> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DashboardDto> getStaffDashboard() {
        return ResponseEntity.ok(dashboardService.getStaffDashboard());
    }

    @GetMapping("/employee")
    public ResponseEntity<DashboardDto> getEmployeeDashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(dashboardService.getEmployeeDashboard(user.getId()));
    }
}
