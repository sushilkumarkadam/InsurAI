package com.insurai.backend.service.impl;

import com.insurai.backend.entity.Claim;
import com.insurai.backend.entity.FraudAlert;
import com.insurai.backend.entity.RiskAssessment;
import com.insurai.backend.repository.ClaimRepository;
import com.insurai.backend.repository.FraudAlertRepository;
import com.insurai.backend.repository.RiskAssessmentRepository;
import com.insurai.backend.service.FraudRiskService;
import com.insurai.backend.service.NotificationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FraudRiskServiceImpl implements FraudRiskService {

    private final ClaimRepository claimRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final NotificationService notificationService;

    public FraudRiskServiceImpl(ClaimRepository claimRepository, FraudAlertRepository fraudAlertRepository,
                                RiskAssessmentRepository riskAssessmentRepository, NotificationService notificationService) {
        this.claimRepository = claimRepository;
        this.fraudAlertRepository = fraudAlertRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void analyzeClaim(Long claimId) {
        Claim claim = claimRepository.findById(claimId).orElseThrow();

        // 1. Simple Rule-based Fraud Detection
        boolean isFraudulent = false;
        StringBuilder fraudReason = new StringBuilder();

        // Rule A: Unusually high claim amount (> 50,000)
        if (claim.getAmount().compareTo(new BigDecimal("50000")) > 0) {
            isFraudulent = true;
            fraudReason.append("Unusually high claim amount. ");
        }

        // Rule B: Duplicate Claims check: Does the user have multiple claims for the same policy this month?
        List<Claim> userClaims = claimRepository.findByUserId(claim.getUser().getId());
        long recentClaimsCount = userClaims.stream()
                .filter(c -> c.getPolicy().getId().equals(claim.getPolicy().getId()))
                .filter(c -> c.getDateSubmitted() != null && c.getDateSubmitted().isAfter(java.time.LocalDateTime.now().minusDays(30)))
                .count();
        if (recentClaimsCount > 2) {
            isFraudulent = true;
            fraudReason.append("Frequent claims in short time. ");
        }

        if (isFraudulent) {
            FraudAlert alert = new FraudAlert();
            alert.setClaim(claim);
            alert.setReason(fraudReason.toString().trim());
            fraudAlertRepository.save(alert);
            
            // Notify ADMIN or STAFF (We notify the claimant too for the demo that it's under review)
            notificationService.createNotification(claim.getUser().getId(), "Your claim #" + claim.getId() + " has been flagged for manual review due to standard risk policies.");
        }

        // 2. Simple Risk Scoring Logic (low / medium / high)
        RiskAssessment assessment = new RiskAssessment();
        assessment.setClaim(claim);
        assessment.setPolicy(claim.getPolicy());

        int score = 0;
        if (claim.getAmount().compareTo(new BigDecimal("1000")) <= 0) {
            score += 10;
        } else if (claim.getAmount().compareTo(new BigDecimal("10000")) <= 0) {
            score += 50;
        } else {
            score += 90;
        }

        if (recentClaimsCount > 1) {
            score += 40;
        }

        String riskLevel;
        if (score < 30) riskLevel = "LOW";
        else if (score < 70) riskLevel = "MEDIUM";
        else riskLevel = "HIGH";

        assessment.setScore(score);
        assessment.setRiskLevel(riskLevel);
        riskAssessmentRepository.save(assessment);

        // Update overall claim risk score
        claim.setRiskScore(score);
        claimRepository.save(claim);
    }
}
