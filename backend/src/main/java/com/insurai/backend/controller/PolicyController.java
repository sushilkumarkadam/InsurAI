package com.insurai.backend.controller;

import com.insurai.backend.dto.PolicyDto;
import com.insurai.backend.service.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public ResponseEntity<List<PolicyDto>> getAllPolicies(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
        }
        if (type != null && !type.isEmpty()) {
            return ResponseEntity.ok(policyService.getPoliciesByType(type));
        }
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/my")
    public ResponseEntity<List<PolicyDto>> getMyPolicies(@org.springframework.security.core.annotation.AuthenticationPrincipal com.insurai.backend.entity.User user) {
        return ResponseEntity.ok(policyService.getPoliciesByAssignedUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyDto> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyDto> createPolicy(@RequestBody PolicyDto policyDto) {
        return new ResponseEntity<>(policyService.createPolicy(policyDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyDto> updatePolicy(@PathVariable Long id, @RequestBody PolicyDto policyDto) {
        return ResponseEntity.ok(policyService.updatePolicy(id, policyDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok("Policy deleted successfully");
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyDto> assignPolicy(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(policyService.assignPolicy(id, userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyDto> updatePolicyStatus(@PathVariable Long id, @RequestParam String status) {
        PolicyDto existing = policyService.getPolicyById(id);
        existing.setStatus(status);
        return ResponseEntity.ok(policyService.updatePolicy(id, existing));
    }
}
