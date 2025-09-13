package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Share Repository
 * 
 * Repository for managing share entities with custom query methods.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find all shares for a specific content
     */
    @Query("SELECT s FROM Share s WHERE s.contentId = :contentId ORDER BY s.createdAt DESC")
    Page<Share> findByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find all shares by a specific user
     */
    @Query("SELECT s FROM Share s WHERE s.userId = :userId ORDER BY s.createdAt DESC")
    Page<Share> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find shares received by a specific user
     */
    @Query("SELECT s FROM Share s WHERE s.targetUserId = :targetUserId ORDER BY s.createdAt DESC")
    Page<Share> findByTargetUserId(@Param("targetUserId") UUID targetUserId, Pageable pageable);
    
    /**
     * Find shares for a specific family
     */
    @Query("SELECT s FROM Share s WHERE s.targetFamilyId = :targetFamilyId ORDER BY s.createdAt DESC")
    Page<Share> findByTargetFamilyId(@Param("targetFamilyId") UUID targetFamilyId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Find shares by family member
     */
    @Query("SELECT s FROM Share s WHERE s.userId = :familyMemberId ORDER BY s.createdAt DESC")
    Page<Share> findByFamilyMemberId(@Param("familyMemberId") UUID familyMemberId, Pageable pageable);
    
    /**
     * Find shares by generation level
     */
    @Query("SELECT s FROM Share s WHERE s.generationLevel = :generationLevel ORDER BY s.createdAt DESC")
    Page<Share> findByGenerationLevel(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    // =============================================================================
    // Share Type Operations
    // =============================================================================
    
    /**
     * Find shares by type
     */
    @Query("SELECT s FROM Share s WHERE s.shareType = :shareType ORDER BY s.createdAt DESC")
    Page<Share> findByShareType(@Param("shareType") String shareType, Pageable pageable);
    
    /**
     * Find shares by content and type
     */
    @Query("SELECT s FROM Share s WHERE s.contentId = :contentId AND s.shareType = :shareType ORDER BY s.createdAt DESC")
    Page<Share> findByContentIdAndShareType(@Param("contentId") UUID contentId, @Param("shareType") String shareType, Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count shares for content
     */
    @Query("SELECT COUNT(s) FROM Share s WHERE s.contentId = :contentId")
    Long countByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Count shares by type for content
     */
    @Query("SELECT s.shareType, COUNT(s) FROM Share s WHERE s.contentId = :contentId GROUP BY s.shareType")
    List<Object[]> getShareTypeDistributionByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Find most shared content
     */
    @Query("SELECT s.contentId, COUNT(s) as shareCount FROM Share s GROUP BY s.contentId ORDER BY shareCount DESC")
    List<Object[]> findMostSharedContent(Pageable pageable);
    
    // =============================================================================
    // Time-based Queries
    // =============================================================================
    
    /**
     * Find recent shares
     */
    @Query("SELECT s FROM Share s WHERE s.createdAt >= :since ORDER BY s.createdAt DESC")
    Page<Share> findRecentShares(@Param("since") LocalDateTime since, Pageable pageable);
    
    /**
     * Find shares by date range
     */
    @Query("SELECT s FROM Share s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    Page<Share> findSharesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    // =============================================================================
    // Privacy and Visibility
    // =============================================================================
    
    /**
     * Find public shares for content
     */
    @Query("SELECT s FROM Share s WHERE s.contentId = :contentId AND s.isPrivate = false ORDER BY s.createdAt DESC")
    Page<Share> findPublicSharesByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find anonymous shares
     */
    @Query("SELECT s FROM Share s WHERE s.isAnonymous = true ORDER BY s.createdAt DESC")
    Page<Share> findAnonymousShares(Pageable pageable);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search shares by message content
     */
    @Query("SELECT s FROM Share s WHERE s.shareMessage LIKE %:searchText% ORDER BY s.createdAt DESC")
    Page<Share> searchSharesByMessage(@Param("searchText") String searchText, Pageable pageable);
}
