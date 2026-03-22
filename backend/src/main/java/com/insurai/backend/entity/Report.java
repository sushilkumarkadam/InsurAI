package com.insurai.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    // e.g. CLAIM_SUMMARY, POLICY_RENEWALS, FRAUD_ANALYSIS
    @Column(nullable = false)
    private String type;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "date_generated")
    private LocalDateTime dateGenerated;

    @PrePersist
    protected void onCreate() {
        dateGenerated = LocalDateTime.now();
    }
}
