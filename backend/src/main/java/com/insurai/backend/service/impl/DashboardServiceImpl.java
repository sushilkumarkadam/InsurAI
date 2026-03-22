package com.insurai.backend.service.impl;

import com.insurai.backend.dto.DashboardDto;
import com.insurai.backend.entity.Claim;
import com.insurai.backend.repository.ClaimRepository;
import com.insurai.backend.repository.FraudAlertRepository;
import com.insurai.backend.repository.PolicyRepository;
import com.insurai.backend.repository.UserRepository;
import com.insurai.backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final FraudAlertRepository fraudAlertRepository;

    public DashboardServiceImpl(PolicyRepository policyRepository, ClaimRepository claimRepository,
                                UserRepository userRepository, FraudAlertRepository fraudAlertRepository) {
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
        this.fraudAlertRepository = fraudAlertRepository;
    }

    @Override
    public DashboardDto getAdminDashboard() {
        DashboardDto dto = new DashboardDto();
        List<Claim> allClaims = claimRepository.findAll();

        dto.setTotalPolicies(policyRepository.count());
        dto.setTotalClaims(allClaims.size());
        dto.setPendingClaims(allClaims.stream().filter(c -> "PENDING".equals(c.getStatus())).count());
        dto.setApprovedClaims(allClaims.stream().filter(c -> "APPROVED".equals(c.getStatus())).count());
        dto.setRejectedClaims(allClaims.stream().filter(c -> "REJECTED".equals(c.getStatus())).count());
        dto.setTotalUsers(userRepository.count());
        dto.setFraudAlerts(fraudAlertRepository.findByIsResolvedFalse().size());

        // Total revenue = sum of approved claim amounts
        BigDecimal totalRevenue = allClaims.stream()
                .filter(c -> "APPROVED".equals(c.getStatus()) && c.getAmount() != null)
                .map(Claim::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalRevenue(totalRevenue);

        return dto;
    }

    @Override
    public DashboardDto getStaffDashboard() {
        DashboardDto dto = new DashboardDto();
        List<Claim> allClaims = claimRepository.findAll();

        dto.setTotalPolicies(policyRepository.count());
        dto.setTotalClaims(allClaims.size());
        dto.setPendingClaims(allClaims.stream().filter(c -> "PENDING".equals(c.getStatus())).count());
        dto.setApprovedClaims(allClaims.stream().filter(c -> "APPROVED".equals(c.getStatus())).count());
        dto.setRejectedClaims(allClaims.stream().filter(c -> "REJECTED".equals(c.getStatus())).count());
        dto.setTotalUsers(userRepository.count());
        dto.setFraudAlerts(fraudAlertRepository.findByIsResolvedFalse().size());

        return dto;
    }

    @Override
    public DashboardDto getEmployeeDashboard(Long userId) {
        DashboardDto dto = new DashboardDto();
        List<Claim> userClaims = claimRepository.findByUserId(userId);

        dto.setTotalPolicies(policyRepository.count()); // all available policies
        dto.setTotalClaims(userClaims.size());
        dto.setPendingClaims(userClaims.stream().filter(c -> "PENDING".equals(c.getStatus())).count());
        dto.setApprovedClaims(userClaims.stream().filter(c -> "APPROVED".equals(c.getStatus())).count());
        dto.setRejectedClaims(userClaims.stream().filter(c -> "REJECTED".equals(c.getStatus())).count());
        dto.setTotalUsers(1L);

        return dto;
    }
}
