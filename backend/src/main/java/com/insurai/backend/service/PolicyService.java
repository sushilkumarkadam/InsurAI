package com.insurai.backend.service;

import com.insurai.backend.dto.PolicyDto;
import java.util.List;

public interface PolicyService {
    PolicyDto createPolicy(PolicyDto policyDto);
    List<PolicyDto> getAllPolicies();
    PolicyDto getPolicyById(Long id);
    PolicyDto updatePolicy(Long id, PolicyDto policyDto);
    void deletePolicy(Long id);
    List<PolicyDto> getPoliciesByStatus(String status);
    List<PolicyDto> getPoliciesByType(String type);
    List<PolicyDto> getPoliciesByAssignedUser(Long userId);
    PolicyDto assignPolicy(Long id, Long userId);
}
