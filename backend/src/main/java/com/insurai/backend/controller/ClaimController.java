package com.insurai.backend.controller;

import com.insurai.backend.dto.ClaimDto;
import com.insurai.backend.entity.User;
import com.insurai.backend.service.ClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ClaimDto> submitClaim(
            @RequestParam("policyId") Long policyId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("reason") String reason,
            @RequestParam(value = "claimType", required = false) String claimType,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "document", required = false) MultipartFile document,
            @AuthenticationPrincipal User user) {
        
        ClaimDto dto = new ClaimDto();
        dto.setPolicyId(policyId);
        dto.setAmount(amount);
        dto.setReason(reason);
        dto.setClaimType(claimType);
        dto.setNotes(notes);

        ClaimDto savedClaim = claimService.submitClaim(dto, document, user.getId());
        return new ResponseEntity<>(savedClaim, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<ClaimDto>> getAllClaims(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        if (status != null || type != null) {
            return ResponseEntity.ok(claimService.filterClaims(status, type));
        }
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @GetMapping("/my")
    public ResponseEntity<List<ClaimDto>> getMyClaims(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(claimService.getClaimsByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDto> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ClaimDto> updateClaimStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClaimDto> assignClaim(@PathVariable Long id, @RequestParam Long staffId) {
        return ResponseEntity.ok(claimService.assignClaim(id, staffId));
    }
}
