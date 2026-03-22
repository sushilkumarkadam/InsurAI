package com.insurai.backend.repository;

import com.insurai.backend.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByStatus(String status);
    List<Policy> findByType(String type);
    List<Policy> findByAssignedUserId(Long userId);
}
