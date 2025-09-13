package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateCommentRequest;
import com.legacykeep.interaction.dto.request.UpdateCommentRequest;
import com.legacykeep.interaction.dto.response.CommentResponse;
import com.legacykeep.interaction.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Comment Service Interface
 * 
 * Defines the contract for comment operations in the family legacy system.
 * Supports nested comment threads, mentions, hashtags, and rich family discussions.
 * Designed with family context and cultural sensitivity in mind.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface CommentService {
    
    // =============================================================================
    // Comment CRUD Operations
    // =============================================================================
    
    /**
     * Create a new comment on legacy content.
     * 
     * @param request Comment creation request
     * @param userId User ID creating the comment
     * @return Created comment response
     */
    CommentResponse createComment(CreateCommentRequest request, UUID userId);
    
    /**
     * Get comments for specific content with pagination and filtering.
     * 
     * @param contentId Content ID to get comments for
     * @param pageable Pagination information
     * @param userId User ID requesting comments (for permissions)
     * @return Page of comment responses
     */
    Page<CommentResponse> getCommentsForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get a specific comment by ID.
     * 
     * @param commentId Comment ID
     * @param userId User ID requesting the comment
     * @return Comment response
     */
    CommentResponse getCommentById(Long commentId, UUID userId);
    
    /**
     * Update an existing comment.
     * 
     * @param commentId Comment ID to update
     * @param request Update request
     * @param userId User ID updating the comment
     * @return Updated comment response
     */
    CommentResponse updateComment(Long commentId, UpdateCommentRequest request, UUID userId);
    
    /**
     * Delete a comment (soft delete).
     * 
     * @param commentId Comment ID to delete
     * @param userId User ID deleting the comment
     */
    void deleteComment(Long commentId, UUID userId);
    
    // =============================================================================
    // Reply Operations
    // =============================================================================
    
    /**
     * Create a reply to an existing comment.
     * 
     * @param parentCommentId Parent comment ID
     * @param request Reply creation request
     * @param userId User ID creating the reply
     * @return Created reply response
     */
    CommentResponse createReply(Long parentCommentId, CreateCommentRequest request, UUID userId);
    
    /**
     * Get replies for a specific comment.
     * 
     * @param parentCommentId Parent comment ID
     * @param pageable Pagination information
     * @param userId User ID requesting replies
     * @return Page of reply responses
     */
    Page<CommentResponse> getRepliesForComment(Long parentCommentId, Pageable pageable, UUID userId);
    
    /**
     * Get comment thread (comment + all replies) for a specific comment.
     * 
     * @param commentId Comment ID
     * @param userId User ID requesting the thread
     * @return Comment thread response
     */
    CommentResponse getCommentThread(Long commentId, UUID userId);
    
    // =============================================================================
    // Comment Interactions
    // =============================================================================
    
    /**
     * Like a comment.
     * 
     * @param commentId Comment ID to like
     * @param userId User ID liking the comment
     */
    void likeComment(Long commentId, UUID userId);
    
    /**
     * Unlike a comment.
     * 
     * @param commentId Comment ID to unlike
     * @param userId User ID unliking the comment
     */
    void unlikeComment(Long commentId, UUID userId);
    
    /**
     * Check if user has liked a comment.
     * 
     * @param commentId Comment ID
     * @param userId User ID
     * @return True if user has liked the comment
     */
    boolean hasUserLikedComment(Long commentId, UUID userId);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get comments by family member.
     * 
     * @param familyMemberId Family member ID
     * @param pageable Pagination information
     * @param userId User ID requesting comments
     * @return Page of comment responses
     */
    Page<CommentResponse> getCommentsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get comments by generation level.
     * 
     * @param generationLevel Generation level
     * @param pageable Pagination information
     * @param userId User ID requesting comments
     * @return Page of comment responses
     */
    Page<CommentResponse> getCommentsByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    /**
     * Get comments by cultural context.
     * 
     * @param culturalTag Cultural tag
     * @param pageable Pagination information
     * @param userId User ID requesting comments
     * @return Page of comment responses
     */
    Page<CommentResponse> getCommentsByCulturalContext(String culturalTag, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Search and Discovery
    // =============================================================================
    
    /**
     * Search comments by text content.
     * 
     * @param searchText Text to search for
     * @param pageable Pagination information
     * @param userId User ID performing search
     * @return Page of matching comment responses
     */
    Page<CommentResponse> searchComments(String searchText, Pageable pageable, UUID userId);
    
    /**
     * Get comments by hashtag.
     * 
     * @param hashtag Hashtag to search for
     * @param pageable Pagination information
     * @param userId User ID requesting comments
     * @return Page of comment responses
     */
    Page<CommentResponse> getCommentsByHashtag(String hashtag, Pageable pageable, UUID userId);
    
    /**
     * Get trending hashtags in comments.
     * 
     * @param limit Number of trending hashtags to return
     * @param userId User ID requesting trending hashtags
     * @return List of trending hashtags
     */
    List<String> getTrendingHashtags(int limit, UUID userId);
    
    // =============================================================================
    // Moderation Operations
    // =============================================================================
    
    /**
     * Moderate a comment (approve/reject).
     * 
     * @param commentId Comment ID to moderate
     * @param approved True to approve, false to reject
     * @param moderatorId Moderator user ID
     * @param reason Moderation reason
     */
    void moderateComment(Long commentId, boolean approved, UUID moderatorId, String reason);
    
    /**
     * Flag a comment for moderation.
     * 
     * @param commentId Comment ID to flag
     * @param userId User ID flagging the comment
     * @param reason Flagging reason
     */
    void flagComment(Long commentId, UUID userId, String reason);
    
    /**
     * Get comments pending moderation.
     * 
     * @param pageable Pagination information
     * @param moderatorId Moderator user ID
     * @return Page of comments pending moderation
     */
    Page<CommentResponse> getCommentsPendingModeration(Pageable pageable, UUID moderatorId);
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    /**
     * Get comment statistics for content.
     * 
     * @param contentId Content ID
     * @param userId User ID requesting statistics
     * @return Comment statistics
     */
    CommentStatistics getCommentStatistics(UUID contentId, UUID userId);
    
    /**
     * Get family comment activity summary.
     * 
     * @param familyId Family ID
     * @param userId User ID requesting summary
     * @return Family comment activity summary
     */
    FamilyCommentActivity getFamilyCommentActivity(UUID familyId, UUID userId);
    
    /**
     * Comment Statistics DTO
     */
    class CommentStatistics {
        private final Long totalComments;
        private final Long totalReplies;
        private final Long totalLikes;
        private final Long totalReactions;
        private final Double averageSentiment;
        private final List<String> topHashtags;
        private final List<String> topMentions;
        
        public CommentStatistics(Long totalComments, Long totalReplies, Long totalLikes, 
                               Long totalReactions, Double averageSentiment, 
                               List<String> topHashtags, List<String> topMentions) {
            this.totalComments = totalComments;
            this.totalReplies = totalReplies;
            this.totalLikes = totalLikes;
            this.totalReactions = totalReactions;
            this.averageSentiment = averageSentiment;
            this.topHashtags = topHashtags;
            this.topMentions = topMentions;
        }
        
        // Getters
        public Long getTotalComments() { return totalComments; }
        public Long getTotalReplies() { return totalReplies; }
        public Long getTotalLikes() { return totalLikes; }
        public Long getTotalReactions() { return totalReactions; }
        public Double getAverageSentiment() { return averageSentiment; }
        public List<String> getTopHashtags() { return topHashtags; }
        public List<String> getTopMentions() { return topMentions; }
    }
    
    /**
     * Family Comment Activity DTO
     */
    class FamilyCommentActivity {
        private final Long totalComments;
        private final Long activeCommenters;
        private final List<GenerationActivity> generationActivity;
        private final List<CulturalActivity> culturalActivity;
        
        public FamilyCommentActivity(Long totalComments, Long activeCommenters, 
                                   List<GenerationActivity> generationActivity, 
                                   List<CulturalActivity> culturalActivity) {
            this.totalComments = totalComments;
            this.activeCommenters = activeCommenters;
            this.generationActivity = generationActivity;
            this.culturalActivity = culturalActivity;
        }
        
        // Getters
        public Long getTotalComments() { return totalComments; }
        public Long getActiveCommenters() { return activeCommenters; }
        public List<GenerationActivity> getGenerationActivity() { return generationActivity; }
        public List<CulturalActivity> getCulturalActivity() { return culturalActivity; }
    }
    
    /**
     * Generation Activity DTO
     */
    class GenerationActivity {
        private final Integer generationLevel;
        private final String generationName;
        private final Long commentCount;
        private final Long activeMembers;
        
        public GenerationActivity(Integer generationLevel, String generationName, 
                                Long commentCount, Long activeMembers) {
            this.generationLevel = generationLevel;
            this.generationName = generationName;
            this.commentCount = commentCount;
            this.activeMembers = activeMembers;
        }
        
        // Getters
        public Integer getGenerationLevel() { return generationLevel; }
        public String getGenerationName() { return generationName; }
        public Long getCommentCount() { return commentCount; }
        public Long getActiveMembers() { return activeMembers; }
    }
    
    /**
     * Cultural Activity DTO
     */
    class CulturalActivity {
        private final String culturalTag;
        private final String displayName;
        private final Long commentCount;
        private final Long uniqueCommenters;
        
        public CulturalActivity(String culturalTag, String displayName, 
                              Long commentCount, Long uniqueCommenters) {
            this.culturalTag = culturalTag;
            this.displayName = displayName;
            this.commentCount = commentCount;
            this.uniqueCommenters = uniqueCommenters;
        }
        
        // Getters
        public String getCulturalTag() { return culturalTag; }
        public String getDisplayName() { return displayName; }
        public Long getCommentCount() { return commentCount; }
        public Long getUniqueCommenters() { return uniqueCommenters; }
    }
}
