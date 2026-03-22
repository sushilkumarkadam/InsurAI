package com.insurai.backend.repository;

import com.insurai.backend.entity.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByIsResolvedFalse();
    List<FraudAlert> findByIsResolved(Boolean isResolved);
}
