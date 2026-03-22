package com.insurai.backend.service.impl;

import com.insurai.backend.dto.ClaimDto;
import com.insurai.backend.entity.Claim;
import com.insurai.backend.entity.ClaimDocument;
import com.insurai.backend.entity.Policy;
import com.insurai.backend.entity.User;
import com.insurai.backend.exception.ResourceNotFoundException;
import com.insurai.backend.repository.ClaimDocumentRepository;
import com.insurai.backend.repository.ClaimRepository;
import com.insurai.backend.repository.PolicyRepository;
import com.insurai.backend.repository.UserRepository;
import com.insurai.backend.service.ClaimService;
import com.insurai.backend.service.FileService;
import com.insurai.backend.service.FraudRiskService;
import com.insurai.backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final ClaimDocumentRepository claimDocumentRepository;
    private final FileService fileService;
    private final FraudRiskService fraudRiskService;
    private final NotificationService notificationService;

    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository,
                            UserRepository userRepository, ClaimDocumentRepository claimDocumentRepository,
                            FileService fileService, FraudRiskService fraudRiskService,
                            NotificationService notificationService) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
        this.claimDocumentRepository = claimDocumentRepository;
        this.fileService = fileService;
        this.fraudRiskService = fraudRiskService;
        this.notificationService = notificationService;
    }

    @Override
    public ClaimDto submitClaim(ClaimDto claimDto, MultipartFile document, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Policy policy = policyRepository.findById(claimDto.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        Claim claim = new Claim();
        claim.setUser(user);
        claim.setPolicy(policy);
        claim.setAmount(claimDto.getAmount());
        claim.setReason(claimDto.getReason());
        claim.setClaimType(claimDto.getClaimType());
        claim.setNotes(claimDto.getNotes());

        Claim savedClaim = claimRepository.save(claim);

        if (document != null && !document.isEmpty()) {
            String fileName = fileService.storeFile(document);
            ClaimDocument claimDocument = new ClaimDocument();
            claimDocument.setClaim(savedClaim);
            claimDocument.setFileName(document.getOriginalFilename());
            claimDocument.setFilePath(fileName);
            claimDocumentRepository.save(claimDocument);
        }

        // Trigger fraud and risk analysis
        fraudRiskService.analyzeClaim(savedClaim.getId());

        // Notify user
        notificationService.createNotification(userId,
                "Your claim #CLM-" + savedClaim.getId() + " for policy " + policy.getPolicyNumber() + " has been submitted successfully.");

        return mapToDto(savedClaim);
    }

    @Override
    public List<ClaimDto> getAllClaims() {
        return claimRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimDto> getClaimsByUserId(Long userId) {
        return claimRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClaimDto getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        return mapToDto(claim);
    }

    @Override
    public ClaimDto updateClaimStatus(Long id, String status) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        claim.setStatus(status.toUpperCase());
        if ("APPROVED".equalsIgnoreCase(status)) {
            claim.setDateApproved(java.time.LocalDateTime.now());
        }
        Claim updatedClaim = claimRepository.save(claim);

        // Notify user about status change
        notificationService.createNotification(claim.getUser().getId(),
                "The status of your claim #CLM-" + claim.getId() + " has been updated to: " + status.toUpperCase());

        return mapToDto(updatedClaim);
    }

    @Override
    public void deleteClaim(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        claimRepository.delete(claim);
    }

    @Override
    public ClaimDto assignClaim(Long id, Long staffId) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        claim.setAssignedStaff(staff);
        Claim updatedClaim = claimRepository.save(claim);
        return mapToDto(updatedClaim);
    }

    @Override
    public List<ClaimDto> filterClaims(String status, String type) {
        List<Claim> claims = claimRepository.findAll();
        if (status != null && !status.isEmpty()) {
            claims = claims.stream().filter(c -> status.equalsIgnoreCase(c.getStatus())).collect(Collectors.toList());
        }
        if (type != null && !type.isEmpty()) {
            claims = claims.stream().filter(c -> type.equalsIgnoreCase(c.getClaimType())).collect(Collectors.toList());
        }
        return claims.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ClaimDto mapToDto(Claim claim) {
        ClaimDto dto = new ClaimDto();
        dto.setId(claim.getId());
        // User info
        if (claim.getUser() != null) {
            dto.setUserId(claim.getUser().getId());
            dto.setUsername(claim.getUser().getUsername());
        }
        // Policy info
        if (claim.getPolicy() != null) {
            dto.setPolicyId(claim.getPolicy().getId());
            dto.setPolicyNumber(claim.getPolicy().getPolicyNumber());
            dto.setPolicyType(claim.getPolicy().getType());
            dto.setPolicyTitle(claim.getPolicy().getTitle());
        }
        dto.setAmount(claim.getAmount());
        dto.setReason(claim.getReason());
        dto.setClaimType(claim.getClaimType());
        dto.setNotes(claim.getNotes());
        dto.setStatus(claim.getStatus());
        dto.setDateSubmitted(claim.getDateSubmitted());
        dto.setDateApproved(claim.getDateApproved());
        dto.setRiskScore(claim.getRiskScore());

        if (claim.getAssignedStaff() != null) {
            dto.setAssignedStaffId(claim.getAssignedStaff().getId());
            dto.setAssignedStaffName(claim.getAssignedStaff().getUsername());
        }

        return dto;
    }
}
