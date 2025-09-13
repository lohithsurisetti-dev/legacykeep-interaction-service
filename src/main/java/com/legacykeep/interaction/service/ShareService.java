package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateShareRequest;
import com.legacykeep.interaction.dto.response.ShareResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Share Service Interface
 * 
 * Service interface for managing shares with family context and targeting.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface ShareService {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Create a new share
     */
    ShareResponse createShare(CreateShareRequest request, UUID userId);
    
    /**
     * Delete a share
     */
    void deleteShare(Long shareId, UUID userId);
    
    /**
     * Get share by ID
     */
    ShareResponse getShareById(Long shareId, UUID userId);
    
    // =============================================================================
    // Content Share Operations
    // =============================================================================
    
    /**
     * Get all shares for content
     */
    Page<ShareResponse> getSharesForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get user's shares
     */
    Page<ShareResponse> getUserShares(UUID userId, Pageable pageable);
    
    /**
     * Get shares received by user
     */
    Page<ShareResponse> getSharesReceivedByUser(UUID userId, Pageable pageable);
    
    /**
     * Get shares for family
     */
    Page<ShareResponse> getSharesForFamily(UUID familyId, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get shares by family member
     */
    Page<ShareResponse> getSharesByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get shares by generation level
     */
    Page<ShareResponse> getSharesByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Share Type Operations
    // =============================================================================
    
    /**
     * Get shares by type
     */
    Page<ShareResponse> getSharesByType(String shareType, Pageable pageable, UUID userId);
    
    /**
     * Get shares by content and type
     */
    Page<ShareResponse> getSharesByContentAndType(UUID contentId, String shareType, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Get share statistics for content
     */
    ShareStatistics getShareStatistics(UUID contentId, UUID userId);
    
    /**
     * Get most shared content
     */
    Page<ShareResponse> getMostSharedContent(Pageable pageable, UUID userId);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search shares by message
     */
    Page<ShareResponse> searchShares(String searchText, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Inner Classes for Response Objects
    // =============================================================================
    
    /**
     * Share Statistics
     */
    class ShareStatistics {
        private Long totalShares;
        private Long shareCount;
        private Long repostCount;
        private Long forwardCount;
        private Long familyShares;
        private Long publicShares;
        private Double familySharePercentage;
        
        // Constructors, getters, and setters
        public ShareStatistics() {}
        
        public ShareStatistics(Long totalShares, Long shareCount, Long repostCount, Long forwardCount, 
                              Long familyShares, Long publicShares, Double familySharePercentage) {
            this.totalShares = totalShares;
            this.shareCount = shareCount;
            this.repostCount = repostCount;
            this.forwardCount = forwardCount;
            this.familyShares = familyShares;
            this.publicShares = publicShares;
            this.familySharePercentage = familySharePercentage;
        }
        
        // Getters and setters
        public Long getTotalShares() { return totalShares; }
        public void setTotalShares(Long totalShares) { this.totalShares = totalShares; }
        
        public Long getShareCount() { return shareCount; }
        public void setShareCount(Long shareCount) { this.shareCount = shareCount; }
        
        public Long getRepostCount() { return repostCount; }
        public void setRepostCount(Long repostCount) { this.repostCount = repostCount; }
        
        public Long getForwardCount() { return forwardCount; }
        public void setForwardCount(Long forwardCount) { this.forwardCount = forwardCount; }
        
        public Long getFamilyShares() { return familyShares; }
        public void setFamilyShares(Long familyShares) { this.familyShares = familyShares; }
        
        public Long getPublicShares() { return publicShares; }
        public void setPublicShares(Long publicShares) { this.publicShares = publicShares; }
        
        public Double getFamilySharePercentage() { return familySharePercentage; }
        public void setFamilySharePercentage(Double familySharePercentage) { this.familySharePercentage = familySharePercentage; }
    }
}
