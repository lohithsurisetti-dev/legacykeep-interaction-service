package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Comment Repository Interface
 * 
 * Repository interface for comment operations in the family legacy system.
 * Provides data access methods for comments with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find comments by content ID with pagination
     */
    @Query("SELECT c FROM Comment c WHERE c.contentId = :contentId AND c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByContentIdAndStatusActive(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find top-level comments by content ID (no parent comment)
     */
    @Query("SELECT c FROM Comment c WHERE c.contentId = :contentId AND c.parentComment IS NULL " +
           "AND c.status = 'ACTIVE' AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findTopLevelCommentsByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find replies for a specific comment
     */
    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId " +
           "AND c.status = 'ACTIVE' AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt ASC")
    Page<Comment> findRepliesByParentCommentId(@Param("parentCommentId") Long parentCommentId, Pageable pageable);
    
    /**
     * Find comment by ID with status check
     */
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.status = 'ACTIVE'")
    Comment findByIdAndStatusActive(@Param("commentId") Long commentId);
    
    // =============================================================================
    // User and Family Context Operations
    // =============================================================================
    
    /**
     * Find comments by user ID
     */
    @Query("SELECT c FROM Comment c WHERE c.userId = :userId AND c.status = 'ACTIVE' " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByUserIdAndStatusActive(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find comments by generation level
     */
    @Query("SELECT c FROM Comment c WHERE c.generationLevel = :generationLevel AND c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByGenerationLevelAndStatusActive(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    /**
     * Find comments by cultural tag
     */
    @Query("SELECT c FROM Comment c WHERE c.culturalTags LIKE %:culturalTag% AND c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByCulturalTagAndStatusActive(@Param("culturalTag") String culturalTag, Pageable pageable);
    
    // =============================================================================
    // Search and Discovery Operations
    // =============================================================================
    
    /**
     * Search comments by text content
     */
    @Query("SELECT c FROM Comment c WHERE LOWER(c.commentText) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
           "AND c.status = 'ACTIVE' AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> searchByCommentText(@Param("searchText") String searchText, Pageable pageable);
    
    /**
     * Find comments by hashtag
     */
    @Query("SELECT c FROM Comment c WHERE c.hashtags LIKE %:hashtag% AND c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByHashtagAndStatusActive(@Param("hashtag") String hashtag, Pageable pageable);
    
    /**
     * Find trending hashtags
     */
    @Query("SELECT c.hashtags FROM Comment c WHERE c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "AND c.createdAt >= :sevenDaysAgo")
    List<String> findTrendingHashtags(@Param("sevenDaysAgo") java.time.LocalDateTime sevenDaysAgo);
    
    // =============================================================================
    // Moderation Operations
    // =============================================================================
    
    /**
     * Find comments pending moderation
     */
    @Query("SELECT c FROM Comment c WHERE c.moderationStatus = 'PENDING' OR c.moderationStatus = 'FLAGGED' " +
           "ORDER BY c.createdAt ASC")
    Page<Comment> findCommentsPendingModeration(Pageable pageable);
    
    /**
     * Find flagged comments
     */
    @Query("SELECT c FROM Comment c WHERE c.moderationStatus = 'FLAGGED' ORDER BY c.createdAt ASC")
    Page<Comment> findFlaggedComments(Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count comments by content ID
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.contentId = :contentId AND c.status = 'ACTIVE'")
    Long countByContentIdAndStatusActive(@Param("contentId") UUID contentId);
    
    /**
     * Count replies by parent comment ID
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.id = :parentCommentId AND c.status = 'ACTIVE'")
    Long countRepliesByParentCommentId(@Param("parentCommentId") Long parentCommentId);
    
    /**
     * Count comments by user ID
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.userId = :userId AND c.status = 'ACTIVE'")
    Long countByUserIdAndStatusActive(@Param("userId") UUID userId);
    
    /**
     * Count comments by generation level
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.generationLevel = :generationLevel AND c.status = 'ACTIVE'")
    Long countByGenerationLevelAndStatusActive(@Param("generationLevel") Integer generationLevel);
    
    /**
     * Count comments by cultural tag
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.culturalTags LIKE %:culturalTag% AND c.status = 'ACTIVE'")
    Long countByCulturalTagAndStatusActive(@Param("culturalTag") String culturalTag);
    
    /**
     * Get average sentiment score by content ID
     */
    @Query("SELECT AVG(c.sentimentScore) FROM Comment c WHERE c.contentId = :contentId AND c.status = 'ACTIVE' " +
           "AND c.sentimentScore IS NOT NULL")
    Double getAverageSentimentScoreByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Get top hashtags by count
     */
    @Query("SELECT c.hashtags FROM Comment c WHERE c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "AND c.createdAt >= :thirtyDaysAgo")
    List<String> getTopHashtags(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get top mentions by count
     */
    @Query("SELECT c.mentions FROM Comment c WHERE c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "AND c.createdAt >= :thirtyDaysAgo")
    List<String> getTopMentions(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    // =============================================================================
    // Family Context Analytics
    // =============================================================================
    
    /**
     * Get family comment activity by generation
     */
    @Query("SELECT c.generationLevel, COUNT(c) FROM Comment c WHERE c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "AND c.createdAt >= :thirtyDaysAgo " +
           "GROUP BY c.generationLevel ORDER BY COUNT(c) DESC")
    List<Object[]> getFamilyCommentActivityByGeneration(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    /**
     * Get family comment activity by cultural context
     */
    @Query("SELECT c.culturalTags, COUNT(c) FROM Comment c WHERE c.status = 'ACTIVE' " +
           "AND (c.moderationStatus = 'APPROVED' OR c.moderationStatus = 'AUTO_APPROVED') " +
           "AND c.createdAt >= :thirtyDaysAgo " +
           "GROUP BY c.culturalTags ORDER BY COUNT(c) DESC")
    List<Object[]> getFamilyCommentActivityByCulturalContext(@Param("thirtyDaysAgo") java.time.LocalDateTime thirtyDaysAgo);
    
    // =============================================================================
    // Utility Operations
    // =============================================================================
    
    /**
     * Check if user has commented on content
     */
    @Query("SELECT COUNT(c) > 0 FROM Comment c WHERE c.contentId = :contentId AND c.userId = :userId " +
           "AND c.status = 'ACTIVE'")
    boolean hasUserCommentedOnContent(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Check if comment exists and is active
     */
    @Query("SELECT COUNT(c) > 0 FROM Comment c WHERE c.id = :commentId AND c.status = 'ACTIVE'")
    boolean existsByIdAndStatusActive(@Param("commentId") Long commentId);
    
    /**
     * Check if comment is a reply
     */
    @Query("SELECT c.parentComment IS NOT NULL FROM Comment c WHERE c.id = :commentId")
    boolean isReply(@Param("commentId") Long commentId);
    
    /**
     * Get comment depth (number of levels in thread)
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.id = :commentId AND c.status = 'ACTIVE'")
    Long getCommentDepth(@Param("commentId") Long commentId);
}
