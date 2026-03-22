package com.insurai.backend.controller;

import com.insurai.backend.entity.Renewal;
import com.insurai.backend.repository.RenewalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/renewals")
public class RenewalController {

    private final RenewalRepository renewalRepository;

    public RenewalController(RenewalRepository renewalRepository) {
        this.renewalRepository = renewalRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Renewal>> getAllRenewals() {
        return ResponseEntity.ok(renewalRepository.findAll());
    }
}
