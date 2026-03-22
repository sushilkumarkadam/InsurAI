package com.insurai.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String reason;

    // PENDING, APPROVED, REJECTED, PROCESSING
    @Column(nullable = false)
    private String status;

    @Column(name = "date_submitted")
    private LocalDateTime dateSubmitted;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "claim_type")
    private String claimType;

    @Column(length = 1000)
    private String notes;

    @Column(name = "date_approved")
    private LocalDateTime dateApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;

    @PrePersist
    protected void onCreate() {
        dateSubmitted = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }
}
