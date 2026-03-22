package com.insurai.backend.controller;

import com.insurai.backend.entity.RiskAssessment;
import com.insurai.backend.repository.RiskAssessmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk-assessments")
public class RiskAssessmentController {

    private final RiskAssessmentRepository riskAssessmentRepository;

    public RiskAssessmentController(RiskAssessmentRepository riskAssessmentRepository) {
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RiskAssessment>> getAllRiskAssessments() {
        return ResponseEntity.ok(riskAssessmentRepository.findAll());
    }
}
