package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Rating Repository
 * 
 * Repository for managing rating entities with custom query methods.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find rating by content ID and user ID
     */
    @Query("SELECT r FROM Rating r WHERE r.contentId = :contentId AND r.userId = :userId")
    Optional<Rating> findByContentIdAndUserId(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Find all ratings for a specific content
     */
    @Query("SELECT r FROM Rating r WHERE r.contentId = :contentId ORDER BY r.createdAt DESC")
    Page<Rating> findByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find all ratings by a specific user
     */
    @Query("SELECT r FROM Rating r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    Page<Rating> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Find ratings by family member
     */
    @Query("SELECT r FROM Rating r WHERE r.userId = :familyMemberId ORDER BY r.createdAt DESC")
    Page<Rating> findByFamilyMemberId(@Param("familyMemberId") UUID familyMemberId, Pageable pageable);
    
    /**
     * Find ratings by generation level
     */
    @Query("SELECT r FROM Rating r WHERE r.generationLevel = :generationLevel ORDER BY r.createdAt DESC")
    Page<Rating> findByGenerationLevel(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    /**
     * Find ratings by cultural context
     */
    @Query("SELECT r FROM Rating r WHERE r.culturalContext LIKE %:culturalTag% ORDER BY r.createdAt DESC")
    Page<Rating> findByCulturalContext(@Param("culturalTag") String culturalTag, Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count ratings for content
     */
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.contentId = :contentId")
    Long countByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Calculate average rating for content
     */
    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.contentId = :contentId")
    Double getAverageRatingByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get rating distribution for content
     */
    @Query("SELECT r.ratingValue, COUNT(r) FROM Rating r WHERE r.contentId = :contentId GROUP BY r.ratingValue ORDER BY r.ratingValue")
    List<Object[]> getRatingDistributionByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Find high-rated content (4+ stars)
     */
    @Query("SELECT r FROM Rating r WHERE r.ratingValue >= 4 ORDER BY r.createdAt DESC")
    Page<Rating> findHighRatedContent(Pageable pageable);
    
    /**
     * Find ratings with text reviews
     */
    @Query("SELECT r FROM Rating r WHERE r.ratingText IS NOT NULL AND r.ratingText != '' ORDER BY r.createdAt DESC")
    Page<Rating> findRatingsWithText(Pageable pageable);
    
    // =============================================================================
    // Time-based Queries
    // =============================================================================
    
    /**
     * Find recent ratings
     */
    @Query("SELECT r FROM Rating r WHERE r.createdAt >= :since ORDER BY r.createdAt DESC")
    Page<Rating> findRecentRatings(@Param("since") LocalDateTime since, Pageable pageable);
    
    /**
     * Find ratings by date range
     */
    @Query("SELECT r FROM Rating r WHERE r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    Page<Rating> findRatingsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    // =============================================================================
    // Privacy and Visibility
    // =============================================================================
    
    /**
     * Find public ratings for content
     */
    @Query("SELECT r FROM Rating r WHERE r.contentId = :contentId AND r.isPrivate = false ORDER BY r.createdAt DESC")
    Page<Rating> findPublicRatingsByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find anonymous ratings
     */
    @Query("SELECT r FROM Rating r WHERE r.isAnonymous = true ORDER BY r.createdAt DESC")
    Page<Rating> findAnonymousRatings(Pageable pageable);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search ratings by text content
     */
    @Query("SELECT r FROM Rating r WHERE r.ratingText LIKE %:searchText% ORDER BY r.createdAt DESC")
    Page<Rating> searchRatingsByText(@Param("searchText") String searchText, Pageable pageable);
    
    /**
     * Find ratings by rating value
     */
    @Query("SELECT r FROM Rating r WHERE r.ratingValue = :ratingValue ORDER BY r.createdAt DESC")
    Page<Rating> findByRatingValue(@Param("ratingValue") Integer ratingValue, Pageable pageable);
}
