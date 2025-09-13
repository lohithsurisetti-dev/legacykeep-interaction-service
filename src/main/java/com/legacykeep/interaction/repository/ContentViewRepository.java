package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.ContentView;
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
 * Content View Repository
 * 
 * Repository for managing content view entities with custom query methods.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find all views for a specific content
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.contentId = :contentId ORDER BY cv.createdAt DESC")
    Page<ContentView> findByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find all views by a specific user
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.userId = :userId ORDER BY cv.createdAt DESC")
    Page<ContentView> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find views by content and user
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.contentId = :contentId AND cv.userId = :userId ORDER BY cv.createdAt DESC")
    Page<ContentView> findByContentIdAndUserId(@Param("contentId") UUID contentId, @Param("userId") UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Find views by family member
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.userId = :familyMemberId ORDER BY cv.createdAt DESC")
    Page<ContentView> findByFamilyMemberId(@Param("familyMemberId") UUID familyMemberId, Pageable pageable);
    
    /**
     * Find views by generation level
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.generationLevel = :generationLevel ORDER BY cv.createdAt DESC")
    Page<ContentView> findByGenerationLevel(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count views for content
     */
    @Query("SELECT COUNT(cv) FROM ContentView cv WHERE cv.contentId = :contentId")
    Long countByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Count views by user
     */
    @Query("SELECT COUNT(cv) FROM ContentView cv WHERE cv.userId = :userId")
    Long countByUserId(@Param("userId") UUID userId);
    
    /**
     * Count unique viewers for content
     */
    @Query("SELECT COUNT(DISTINCT cv.userId) FROM ContentView cv WHERE cv.contentId = :contentId")
    Long countUniqueViewersByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get average view duration for content
     */
    @Query("SELECT AVG(cv.viewDuration) FROM ContentView cv WHERE cv.contentId = :contentId AND cv.viewDuration IS NOT NULL")
    Double findAverageViewDurationByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get average view completion percentage for content
     */
    @Query("SELECT AVG(cv.viewCompletionPercentage) FROM ContentView cv WHERE cv.contentId = :contentId AND cv.viewCompletionPercentage IS NOT NULL")
    Double findAverageViewCompletionByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Find most viewed content
     */
    @Query("SELECT cv.contentId, COUNT(cv) as viewCount FROM ContentView cv GROUP BY cv.contentId ORDER BY viewCount DESC")
    List<Object[]> findMostViewedContent(Pageable pageable);
    
    /**
     * Find content with highest completion rates
     */
    @Query("SELECT cv.contentId, AVG(cv.viewCompletionPercentage) as avgCompletion FROM ContentView cv WHERE cv.viewCompletionPercentage IS NOT NULL GROUP BY cv.contentId ORDER BY avgCompletion DESC")
    List<Object[]> findContentWithHighestCompletionRates(Pageable pageable);
    
    // =============================================================================
    // Time-based Queries
    // =============================================================================
    
    /**
     * Find recent views
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.createdAt >= :since ORDER BY cv.createdAt DESC")
    Page<ContentView> findRecentViews(@Param("since") LocalDateTime since, Pageable pageable);
    
    /**
     * Find views by date range
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.createdAt BETWEEN :startDate AND :endDate ORDER BY cv.createdAt DESC")
    Page<ContentView> findViewsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find views today
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.createdAt >= :today ORDER BY cv.createdAt DESC")
    Page<ContentView> findViewsToday(@Param("today") LocalDateTime today, Pageable pageable);
    
    // =============================================================================
    // View Duration and Completion Analysis
    // =============================================================================
    
    /**
     * Find views with high completion rates
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.viewCompletionPercentage >= :minCompletion ORDER BY cv.viewCompletionPercentage DESC")
    Page<ContentView> findViewsWithHighCompletion(@Param("minCompletion") Double minCompletion, Pageable pageable);
    
    /**
     * Find views with long duration
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.viewDuration >= :minDuration ORDER BY cv.viewDuration DESC")
    Page<ContentView> findViewsWithLongDuration(@Param("minDuration") Integer minDuration, Pageable pageable);
    
    /**
     * Find views by completion range
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.viewCompletionPercentage BETWEEN :minCompletion AND :maxCompletion ORDER BY cv.viewCompletionPercentage DESC")
    Page<ContentView> findViewsByCompletionRange(@Param("minCompletion") Double minCompletion, @Param("maxCompletion") Double maxCompletion, Pageable pageable);
    
    // =============================================================================
    // Privacy and Visibility
    // =============================================================================
    
    /**
     * Find anonymous views
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.isAnonymous = true ORDER BY cv.createdAt DESC")
    Page<ContentView> findAnonymousViews(Pageable pageable);
    
    /**
     * Find views by content and generation
     */
    @Query("SELECT cv FROM ContentView cv WHERE cv.contentId = :contentId AND cv.generationLevel = :generationLevel ORDER BY cv.createdAt DESC")
    Page<ContentView> findByContentIdAndGenerationLevel(@Param("contentId") UUID contentId, @Param("generationLevel") Integer generationLevel, Pageable pageable);
}
