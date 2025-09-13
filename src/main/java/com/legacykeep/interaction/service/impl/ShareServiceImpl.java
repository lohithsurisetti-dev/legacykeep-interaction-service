package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateShareRequest;
import com.legacykeep.interaction.dto.response.ShareResponse;
import com.legacykeep.interaction.entity.Share;
import com.legacykeep.interaction.exception.InteractionServiceException;
import com.legacykeep.interaction.repository.ShareRepository;
import com.legacykeep.interaction.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Share Service Implementation
 * 
 * Implementation of share service with family context and targeting.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShareServiceImpl implements ShareService {
    
    private final ShareRepository shareRepository;
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    @Override
    public ShareResponse createShare(CreateShareRequest request, UUID userId) {
        log.info("Creating share for content: {} by user: {} - type: {}", 
                request.getContentId(), userId, request.getShareType());
        
        // Create new share
        Share share = Share.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .shareType(request.getShareType())
                .targetUserId(request.getTargetUserId())
                .targetFamilyId(request.getTargetFamilyId())
                .shareMessage(request.getShareMessage())
                .familyContext(request.getFamilyContext())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .isAnonymous(request.getIsAnonymous())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .build();
        
        Share savedShare = shareRepository.save(share);
        log.info("Share created successfully with ID: {}", savedShare.getId());
        
        return convertToShareResponse(savedShare, userId);
    }
    
    @Override
    public void deleteShare(Long shareId, UUID userId) {
        log.info("Deleting share: {} by user: {}", shareId, userId);
        
        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new InteractionServiceException("Share not found"));
        
        // Check if user owns this share
        if (!share.getUserId().equals(userId)) {
            throw new InteractionServiceException("User can only delete their own shares");
        }
        
        shareRepository.delete(share);
        log.info("Share deleted successfully: {}", shareId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ShareResponse getShareById(Long shareId, UUID userId) {
        log.info("Getting share by ID: {} for user: {}", shareId, userId);
        
        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new InteractionServiceException("Share not found"));
        
        return convertToShareResponse(share, userId);
    }
    
    // =============================================================================
    // Content Share Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting shares for content: {} by user: {}", contentId, userId);
        
        Page<Share> shares = shareRepository.findByContentId(contentId, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getUserShares(UUID userId, Pageable pageable) {
        log.info("Getting shares by user: {}", userId);
        
        Page<Share> shares = shareRepository.findByUserId(userId, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesReceivedByUser(UUID userId, Pageable pageable) {
        log.info("Getting shares received by user: {}", userId);
        
        Page<Share> shares = shareRepository.findByTargetUserId(userId, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesForFamily(UUID familyId, Pageable pageable, UUID userId) {
        log.info("Getting shares for family: {} by user: {}", familyId, userId);
        
        Page<Share> shares = shareRepository.findByTargetFamilyId(familyId, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting shares by family member: {} for user: {}", familyMemberId, userId);
        
        Page<Share> shares = shareRepository.findByFamilyMemberId(familyMemberId, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting shares by generation: {} for user: {}", generationLevel, userId);
        
        Page<Share> shares = shareRepository.findByGenerationLevel(generationLevel, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    // =============================================================================
    // Share Type Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesByType(String shareType, Pageable pageable, UUID userId) {
        log.info("Getting shares by type: {} for user: {}", shareType, userId);
        
        Page<Share> shares = shareRepository.findByShareType(shareType, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getSharesByContentAndType(UUID contentId, String shareType, Pageable pageable, UUID userId) {
        log.info("Getting shares by content: {} and type: {} for user: {}", contentId, shareType, userId);
        
        Page<Share> shares = shareRepository.findByContentIdAndShareType(contentId, shareType, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public ShareStatistics getShareStatistics(UUID contentId, UUID userId) {
        log.info("Getting share statistics for content: {} by user: {}", contentId, userId);
        
        Long totalShares = shareRepository.countByContentId(contentId);
        List<Object[]> distribution = shareRepository.getShareTypeDistributionByContentId(contentId);
        
        // Parse distribution data
        Long shareCount = 0L, repostCount = 0L, forwardCount = 0L;
        for (Object[] row : distribution) {
            String shareType = (String) row[0];
            Long count = (Long) row[1];
            switch (shareType) {
                case "SHARE": shareCount = count; break;
                case "REPOST": repostCount = count; break;
                case "FORWARD": forwardCount = count; break;
            }
        }
        
        // Calculate family vs public shares (simplified)
        Long familyShares = totalShares / 2; // TODO: Implement proper family share calculation
        Long publicShares = totalShares - familyShares;
        Double familySharePercentage = totalShares > 0 ? (familyShares.doubleValue() / totalShares) * 100 : 0.0;
        
        return new ShareStatistics(
                totalShares, shareCount, repostCount, forwardCount, 
                familyShares, publicShares, familySharePercentage
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> getMostSharedContent(Pageable pageable, UUID userId) {
        log.info("Getting most shared content for user: {}", userId);
        
        // This would need a custom implementation to get content details
        // For now, return empty page
        return Page.empty();
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ShareResponse> searchShares(String searchText, Pageable pageable, UUID userId) {
        log.info("Searching shares with text: {} for user: {}", searchText, userId);
        
        Page<Share> shares = shareRepository.searchSharesByMessage(searchText, pageable);
        
        return shares.map(share -> convertToShareResponse(share, userId));
    }
    
    // =============================================================================
    // Helper Methods
    // =============================================================================
    
    /**
     * Convert Share entity to ShareResponse DTO
     */
    private ShareResponse convertToShareResponse(Share share, UUID userId) {
        return ShareResponse.builder()
                .id(share.getId())
                .contentId(share.getContentId())
                .userId(share.getUserId())
                .userName("User Name") // TODO: Get from User Service
                .userAvatar("User Avatar") // TODO: Get from User Service
                .shareType(share.getShareType())
                .targetUserId(share.getTargetUserId())
                .targetUserName("Target User") // TODO: Get from User Service
                .targetFamilyId(share.getTargetFamilyId())
                .targetFamilyName("Target Family") // TODO: Get from Family Service
                .shareMessage(share.getShareMessage())
                .familyContext(share.getFamilyContext())
                .generationLevel(share.getGenerationLevel())
                .generationName("Generation " + share.getGenerationLevel())
                .relationshipContext(share.getRelationshipContext())
                .isAnonymous(share.getIsAnonymous())
                .isPrivate(share.getIsPrivate())
                .metadata(share.getMetadata())
                .createdAt(share.getCreatedAt())
                .familyMemberName("Family Member") // TODO: Get from Relationship Service
                .relationshipToUser("Relationship") // TODO: Get from Relationship Service
                .isFromSameGeneration(false) // TODO: Calculate from generation levels
                .isFromSameFamily(false) // TODO: Calculate from family context
                .build();
    }
}
