package com.insurai.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "renewals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Renewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    // e.g. PENDING, COMPLETED, OVERDUE
    @Column(nullable = false)
    private String status;
}
