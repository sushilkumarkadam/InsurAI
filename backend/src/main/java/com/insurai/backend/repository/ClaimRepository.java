package com.insurai.backend.repository;

import com.insurai.backend.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserId(Long userId);
    List<Claim> findByStatus(String status);
    List<Claim> findByUserIdAndStatus(Long userId, String status);
    List<Claim> findByClaimType(String claimType);
}
