package com.insurai.backend.service;

import com.insurai.backend.dto.DashboardDto;

public interface DashboardService {
    DashboardDto getAdminDashboard();
    DashboardDto getStaffDashboard();
    DashboardDto getEmployeeDashboard(Long userId);
}
