package com.insurai.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    // LOW, MEDIUM, HIGH
    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    private Integer score;

    @Column(name = "assessment_date")
    private LocalDateTime assessmentDate;

    @PrePersist
    protected void onCreate() {
        assessmentDate = LocalDateTime.now();
    }
}
