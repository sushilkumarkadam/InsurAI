package com.insurai.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(nullable = false)
    private String reason;

    @Column(name = "date_flagged")
    private LocalDateTime dateFlagged;

    @Column(name = "is_resolved")
    private Boolean isResolved = false;

    @PrePersist
    protected void onCreate() {
        dateFlagged = LocalDateTime.now();
        if (isResolved == null) isResolved = false;
    }
}
