package com.insurai.backend.repository;

import com.insurai.backend.entity.Renewal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenewalRepository extends JpaRepository<Renewal, Long> {
    List<Renewal> findByUserId(Long userId);
}
