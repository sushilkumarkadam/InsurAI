package com.insurai.backend.service.impl;

import com.insurai.backend.dto.PolicyDto;
import com.insurai.backend.entity.Policy;
import com.insurai.backend.exception.ResourceNotFoundException;
import com.insurai.backend.repository.PolicyRepository;
import com.insurai.backend.repository.UserRepository;
import com.insurai.backend.entity.User;
import com.insurai.backend.service.PolicyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository, UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PolicyDto createPolicy(PolicyDto policyDto) {
        Policy policy = new Policy();
        // Auto-generate policy number if not provided
        policy.setPolicyNumber(policyDto.getPolicyNumber() != null
                ? policyDto.getPolicyNumber()
                : "POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        policy.setTitle(policyDto.getTitle());
        policy.setType(policyDto.getType());
        policy.setDescription(policyDto.getDescription());
        policy.setCoverageAmount(policyDto.getCoverageAmount());
        policy.setPremium(policyDto.getPremium());
        policy.setStatus(policyDto.getStatus() != null ? policyDto.getStatus() : "ACTIVE");
        policy.setExpiryDate(policyDto.getExpiryDate()); // @PrePersist will default to +1 year if null

        Policy savedPolicy = policyRepository.save(policy);
        return mapToDto(savedPolicy);
    }

    @Override
    public List<PolicyDto> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyDto getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        return mapToDto(policy);
    }

    @Override
    public PolicyDto updatePolicy(Long id, PolicyDto policyDto) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));

        policy.setTitle(policyDto.getTitle());
        policy.setType(policyDto.getType());
        policy.setDescription(policyDto.getDescription());
        policy.setCoverageAmount(policyDto.getCoverageAmount());
        policy.setPremium(policyDto.getPremium());
        if (policyDto.getStatus() != null) policy.setStatus(policyDto.getStatus());
        if (policyDto.getExpiryDate() != null) policy.setExpiryDate(policyDto.getExpiryDate());

        Policy updatedPolicy = policyRepository.save(policy);
        return mapToDto(updatedPolicy);
    }

    @Override
    public void deletePolicy(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        policyRepository.delete(policy);
    }

    @Override
    public List<PolicyDto> getPoliciesByStatus(String status) {
        return policyRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyDto> getPoliciesByType(String type) {
        return policyRepository.findByType(type).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyDto> getPoliciesByAssignedUser(Long userId) {
        return policyRepository.findByAssignedUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyDto assignPolicy(Long id, Long userId) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        }
        policy.setAssignedUser(user);
        Policy savedPolicy = policyRepository.save(policy);
        return mapToDto(savedPolicy);
    }

    private PolicyDto mapToDto(Policy policy) {
        PolicyDto dto = new PolicyDto();
        dto.setId(policy.getId());
        dto.setPolicyNumber(policy.getPolicyNumber());
        dto.setTitle(policy.getTitle());
        dto.setType(policy.getType());
        dto.setDescription(policy.getDescription());
        dto.setCoverageAmount(policy.getCoverageAmount());
        dto.setPremium(policy.getPremium());
        dto.setStatus(policy.getStatus());
        dto.setExpiryDate(policy.getExpiryDate());
        dto.setCreatedAt(policy.getCreatedAt());
        dto.setUpdatedAt(policy.getUpdatedAt());
        
        if (policy.getAssignedUser() != null) {
            dto.setAssignedUserId(policy.getAssignedUser().getId());
            dto.setAssignedUsername(policy.getAssignedUser().getUsername());
        }
        
        return dto;
    }
}
