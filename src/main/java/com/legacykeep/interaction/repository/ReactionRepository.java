package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Reaction Repository Interface
 * 
 * Repository interface for reaction operations in the family legacy system.
 * Provides data access methods for reactions with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find reactions by content ID with pagination
     */
    @Query("SELECT r FROM Reaction r WHERE r.contentId = :contentId ORDER BY r.createdAt DESC")
    Page<Reaction> findByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find reaction by content ID and user ID
     */
    @Query("SELECT r FROM Reaction r WHERE r.contentId = :contentId AND r.userId = :userId")
    Reaction findByContentIdAndUserId(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Find reactions by user ID
     */
    @Query("SELECT r FROM Reaction r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    Page<Reaction> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find reactions by reaction type
     */
    @Query("SELECT r FROM Reaction r WHERE r.reactionType = :reactionType ORDER BY r.createdAt DESC")
    Page<Reaction> findByReactionType(@Param("reactionType") Reaction.ReactionType reactionType, Pageable pageable);
    
    // =============================================================================
    // User and Family Context Operations
    // =============================================================================
    
    /**
     * Find reactions by generation level
     */
    @Query("SELECT r FROM Reaction r WHERE r.generationLevel = :generationLevel ORDER BY r.createdAt DESC")
    Page<Reaction> findByGenerationLevel(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    /**
     * Find reactions by cultural context
     */
    @Query("SELECT r FROM Reaction r WHERE r.culturalContext LIKE %:culturalTag% ORDER BY r.createdAt DESC")
    Page<Reaction> findByCulturalContext(@Param("culturalTag") String culturalTag, Pageable pageable);
    
    /**
     * Find reactions by family context
     */
    @Query("SELECT r FROM Reaction r WHERE r.familyContext LIKE %:familyContext% ORDER BY r.createdAt DESC")
    Page<Reaction> findByFamilyContext(@Param("familyContext") String familyContext, Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count reactions by content ID
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.contentId = :contentId")
    Long countByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Count unique reactors by content ID
     */
    @Query("SELECT COUNT(DISTINCT r.userId) FROM Reaction r WHERE r.contentId = :contentId")
    Long countUniqueReactorsByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Count reactions by reaction type and content ID
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.contentId = :contentId AND r.reactionType = :reactionType")
    Long countByContentIdAndReactionType(@Param("contentId") UUID contentId, @Param("reactionType") Reaction.ReactionType reactionType);
    
    /**
     * Count reactions by user ID
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.userId = :userId")
    Long countByUserId(@Param("userId") UUID userId);
    
    /**
     * Count reactions by generation level
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.generationLevel = :generationLevel")
    Long countByGenerationLevel(@Param("generationLevel") Integer generationLevel);
    
    /**
     * Count reactions by cultural context
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.culturalContext LIKE %:culturalTag%")
    Long countByCulturalContext(@Param("culturalTag") String culturalTag);
    
    /**
     * Get average intensity by content ID
     */
    @Query("SELECT AVG(r.intensity) FROM Reaction r WHERE r.contentId = :contentId")
    Double getAverageIntensityByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get average intensity by reaction type and content ID
     */
    @Query("SELECT AVG(r.intensity) FROM Reaction r WHERE r.contentId = :contentId AND r.reactionType = :reactionType")
    Double getAverageIntensityByContentIdAndReactionType(@Param("contentId") UUID contentId, @Param("reactionType") Reaction.ReactionType reactionType);
    
    /**
     * Get reaction breakdown by type for content
     */
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.contentId = :contentId GROUP BY r.reactionType ORDER BY COUNT(r) DESC")
    List<Object[]> getReactionBreakdownByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get intensity distribution for content
     */
    @Query("SELECT r.intensity, COUNT(r) FROM Reaction r WHERE r.contentId = :contentId GROUP BY r.intensity ORDER BY r.intensity")
    List<Object[]> getIntensityDistributionByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get generation reaction breakdown for content
     */
    @Query("SELECT r.generationLevel, COUNT(r) FROM Reaction r WHERE r.contentId = :contentId GROUP BY r.generationLevel ORDER BY COUNT(r) DESC")
    List<Object[]> getGenerationReactionBreakdownByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get cultural reaction breakdown for content
     */
    @Query("SELECT r.culturalContext, COUNT(r) FROM Reaction r WHERE r.contentId = :contentId GROUP BY r.culturalContext ORDER BY COUNT(r) DESC")
    List<Object[]> getCulturalReactionBreakdownByContentId(@Param("contentId") UUID contentId);
    
    // =============================================================================
    // Family Context Analytics
    // =============================================================================
    
    /**
     * Get family reaction activity by generation
     */
    @Query("SELECT r.generationLevel, COUNT(r) FROM Reaction r WHERE r.createdAt >= :thirtyDaysAgo " +
           "GROUP BY r.generationLevel ORDER BY COUNT(r) DESC")
    List<Object[]> getFamilyReactionActivityByGeneration(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get family reaction activity by cultural context
     */
    @Query("SELECT r.culturalContext, COUNT(r) FROM Reaction r WHERE r.createdAt >= :thirtyDaysAgo " +
           "GROUP BY r.culturalContext ORDER BY COUNT(r) DESC")
    List<Object[]> getFamilyReactionActivityByCulturalContext(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get top reaction types by count
     */
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.createdAt >= :thirtyDaysAgo " +
           "GROUP BY r.reactionType ORDER BY COUNT(r) DESC")
    List<Object[]> getTopReactionTypes(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get trending reactions
     */
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.createdAt >= :sevenDaysAgo " +
           "GROUP BY r.reactionType ORDER BY COUNT(r) DESC")
    List<Object[]> getTrendingReactions(@Param("sevenDaysAgo") java.time.LocalDateTime sevenDaysAgo);
    
    // =============================================================================
    // Reaction Type Analytics
    // =============================================================================
    
    /**
     * Get family-specific reactions count
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.reactionType IN ('BLESSING', 'PRIDE', 'GRATITUDE', 'MEMORY', 'WISDOM', 'TRADITION', 'RESPECT', 'HONOR', 'LEGACY', 'HERITAGE')")
    Long countFamilySpecificReactions();
    
    /**
     * Get generational reactions count
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.reactionType IN ('GRANDPARENT', 'PARENT', 'CHILD', 'SIBLING')")
    Long countGenerationalReactions();
    
    /**
     * Get cultural reactions count
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.reactionType IN ('NAMASTE', 'OM', 'FESTIVAL', 'PRAYER', 'RITUAL')")
    Long countCulturalReactions();
    
    /**
     * Get core reactions count
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.reactionType IN ('LIKE', 'LOVE', 'HEART', 'LAUGH', 'WOW', 'SAD', 'ANGRY')")
    Long countCoreReactions();
    
    // =============================================================================
    // Utility Operations
    // =============================================================================
    
    /**
     * Check if user has reacted to content
     */
    @Query("SELECT COUNT(r) > 0 FROM Reaction r WHERE r.contentId = :contentId AND r.userId = :userId")
    boolean hasUserReactedToContent(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Check if user has specific reaction to content
     */
    @Query("SELECT COUNT(r) > 0 FROM Reaction r WHERE r.contentId = :contentId AND r.userId = :userId AND r.reactionType = :reactionType")
    boolean hasUserReactedWithType(@Param("contentId") UUID contentId, @Param("userId") UUID userId, @Param("reactionType") Reaction.ReactionType reactionType);
    
    /**
     * Get user's reaction to content
     */
    @Query("SELECT r FROM Reaction r WHERE r.contentId = :contentId AND r.userId = :userId")
    Reaction getUserReactionToContent(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Check if reaction exists
     */
    @Query("SELECT COUNT(r) > 0 FROM Reaction r WHERE r.id = :reactionId")
    boolean existsById(@Param("reactionId") Long reactionId);
    
    /**
     * Get reaction intensity statistics
     */
    @Query("SELECT MIN(r.intensity), MAX(r.intensity), AVG(r.intensity) FROM Reaction r WHERE r.contentId = :contentId")
    Object[] getReactionIntensityStatistics(@Param("contentId") UUID contentId);
    
    /**
     * Get reaction intensity statistics by type
     */
    @Query("SELECT MIN(r.intensity), MAX(r.intensity), AVG(r.intensity) FROM Reaction r WHERE r.contentId = :contentId AND r.reactionType = :reactionType")
    Object[] getReactionIntensityStatisticsByType(@Param("contentId") UUID contentId, @Param("reactionType") Reaction.ReactionType reactionType);
    
    // =============================================================================
    // Advanced Analytics
    // =============================================================================
    
    /**
     * Get reaction sentiment analysis
     */
    @Query("SELECT r.reactionType, AVG(r.intensity) FROM Reaction r WHERE r.contentId = :contentId " +
           "GROUP BY r.reactionType ORDER BY AVG(r.intensity) DESC")
    List<Object[]> getReactionSentimentAnalysis(@Param("contentId") UUID contentId);
    
    /**
     * Get family engagement metrics
     */
    @Query("SELECT COUNT(DISTINCT r.userId), COUNT(r), AVG(r.intensity) FROM Reaction r WHERE r.createdAt >= :thirtyDaysAgo")
    Object[] getFamilyEngagementMetrics(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get cultural engagement metrics
     */
    @Query("SELECT r.culturalContext, COUNT(DISTINCT r.userId), COUNT(r), AVG(r.intensity) FROM Reaction r " +
           "WHERE r.createdAt >= :thirtyDaysAgo AND r.culturalContext IS NOT NULL " +
           "GROUP BY r.culturalContext ORDER BY COUNT(r) DESC")
    List<Object[]> getCulturalEngagementMetrics(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
}
