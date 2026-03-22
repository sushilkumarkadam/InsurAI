package com.insurai.backend.service;

import com.insurai.backend.dto.ClaimDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ClaimService {
    ClaimDto submitClaim(ClaimDto claimDto, MultipartFile document, Long userId);
    List<ClaimDto> getAllClaims();
    List<ClaimDto> getClaimsByUserId(Long userId);
    ClaimDto getClaimById(Long id);
    ClaimDto updateClaimStatus(Long id, String status);
    void deleteClaim(Long id);
    ClaimDto assignClaim(Long id, Long staffId);
    List<ClaimDto> filterClaims(String status, String type);
}
