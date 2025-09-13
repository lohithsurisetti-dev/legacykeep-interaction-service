package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateCommentRequest;
import com.legacykeep.interaction.dto.request.UpdateCommentRequest;
import com.legacykeep.interaction.dto.response.CommentResponse;
import com.legacykeep.interaction.entity.Comment;
import com.legacykeep.interaction.repository.CommentRepository;
import com.legacykeep.interaction.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Comment Service Implementation
 * 
 * Implementation of comment operations in the family legacy system.
 * Provides comprehensive comment management with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    
    // =============================================================================
    // Comment CRUD Operations
    // =============================================================================
    
    @Override
    public CommentResponse createComment(CreateCommentRequest request, UUID userId) {
        log.info("Creating comment for content: {} by user: {}", request.getContentId(), userId);
        
        // Validate content exists (would integrate with Legacy Service)
        // validateContentExists(request.getContentId());
        
        // Create comment entity
        Comment comment = Comment.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .commentText(request.getCommentText())
                .mentions(convertListToJson(request.getMentions()))
                .hashtags(convertListToJson(request.getHashtags()))
                .mediaUrls(convertListToJson(request.getMediaUrls()))
                .familyContext(request.getFamilyContext())
                .culturalTags(convertListToJson(request.getCulturalTags()))
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .languageCode(request.getLanguageCode())
                .isAnonymous(request.getIsAnonymous())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .status(Comment.CommentStatus.ACTIVE)
                .moderationStatus(Comment.ModerationStatus.AUTO_APPROVED)
                .build();
        
        // Set parent comment if this is a reply
        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
            comment.setParentComment(parentComment);
        }
        
        // Save comment
        Comment savedComment = commentRepository.save(comment);
        
        // Update parent comment reply count if this is a reply
        if (request.getParentCommentId() != null) {
            updateParentCommentReplyCount(request.getParentCommentId());
        }
        
        log.info("Comment created successfully with ID: {}", savedComment.getId());
        
        // Convert to response
        return convertToCommentResponse(savedComment, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting comments for content: {} by user: {}", contentId, userId);
        
        Page<Comment> comments = commentRepository.findByContentIdAndStatusActive(contentId, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentId, UUID userId) {
        log.info("Getting comment by ID: {} for user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        return convertToCommentResponse(comment, userId);
    }
    
    @Override
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, UUID userId) {
        log.info("Updating comment: {} by user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Check if user owns the comment
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to update this comment");
        }
        
        // Update comment fields
        comment.setCommentText(request.getCommentText());
        comment.setMentions(convertListToJson(request.getMentions()));
        comment.setHashtags(convertListToJson(request.getHashtags()));
        comment.setMediaUrls(convertListToJson(request.getMediaUrls()));
        comment.setFamilyContext(request.getFamilyContext());
        comment.setCulturalTags(convertListToJson(request.getCulturalTags()));
        comment.setRelationshipContext(request.getRelationshipContext());
        comment.setLanguageCode(request.getLanguageCode());
        comment.setIsAnonymous(request.getIsAnonymous());
        comment.setIsPrivate(request.getIsPrivate());
        comment.setMetadata(request.getMetadata());
        
        // Update edit information
        comment.setIsEdited(true);
        comment.setEditCount(comment.getEditCount() + 1);
        comment.setEditHistory(updateEditHistory(comment.getEditHistory(), request.getEditReason()));
        
        Comment updatedComment = commentRepository.save(comment);
        
        log.info("Comment updated successfully: {}", commentId);
        
        return convertToCommentResponse(updatedComment, userId);
    }
    
    @Override
    public void deleteComment(Long commentId, UUID userId) {
        log.info("Deleting comment: {} by user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Check if user owns the comment
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to delete this comment");
        }
        
        // Soft delete
        comment.setStatus(Comment.CommentStatus.DELETED);
        commentRepository.save(comment);
        
        // Update parent comment reply count if this is a reply
        if (comment.getParentComment() != null) {
            updateParentCommentReplyCount(comment.getParentComment().getId());
        }
        
        log.info("Comment deleted successfully: {}", commentId);
    }
    
    // =============================================================================
    // Reply Operations
    // =============================================================================
    
    @Override
    public CommentResponse createReply(Long parentCommentId, CreateCommentRequest request, UUID userId) {
        log.info("Creating reply to comment: {} by user: {}", parentCommentId, userId);
        
        // Validate parent comment exists
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        
        // Create reply
        CreateCommentRequest replyRequest = CreateCommentRequest.builder()
                .contentId(request.getContentId())
                .commentText(request.getCommentText())
                .parentCommentId(parentCommentId)
                .mentions(request.getMentions())
                .hashtags(request.getHashtags())
                .mediaUrls(request.getMediaUrls())
                .familyContext(request.getFamilyContext())
                .culturalTags(request.getCulturalTags())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .languageCode(request.getLanguageCode())
                .isAnonymous(request.getIsAnonymous())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .build();
        
        return createComment(replyRequest, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getRepliesForComment(Long parentCommentId, Pageable pageable, UUID userId) {
        log.info("Getting replies for comment: {} by user: {}", parentCommentId, userId);
        
        Page<Comment> replies = commentRepository.findRepliesByParentCommentId(parentCommentId, pageable);
        
        return replies.map(reply -> convertToCommentResponse(reply, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentThread(Long commentId, UUID userId) {
        log.info("Getting comment thread for comment: {} by user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        CommentResponse commentResponse = convertToCommentResponse(comment, userId);
        
        // Get replies for this comment
        Page<Comment> replies = commentRepository.findRepliesByParentCommentId(commentId, Pageable.unpaged());
        List<CommentResponse> replyResponses = replies.getContent().stream()
                .map(reply -> convertToCommentResponse(reply, userId))
                .collect(Collectors.toList());
        
        commentResponse.setReplies(replyResponses);
        
        return commentResponse;
    }
    
    // =============================================================================
    // Comment Interactions
    // =============================================================================
    
    @Override
    public void likeComment(Long commentId, UUID userId) {
        log.info("Liking comment: {} by user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Check if user has already liked this comment
        if (hasUserLikedComment(commentId, userId)) {
            throw new IllegalArgumentException("User has already liked this comment");
        }
        
        // Increment like count
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
        
        log.info("Comment liked successfully: {}", commentId);
    }
    
    @Override
    public void unlikeComment(Long commentId, UUID userId) {
        log.info("Unliking comment: {} by user: {}", commentId, userId);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        // Check if user has liked this comment
        if (!hasUserLikedComment(commentId, userId)) {
            throw new IllegalArgumentException("User has not liked this comment");
        }
        
        // Decrement like count
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
        
        log.info("Comment unliked successfully: {}", commentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedComment(Long commentId, UUID userId) {
        // This would integrate with a separate like system or use a join table
        // For now, return false as placeholder
        return false;
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting comments by family member: {} for user: {}", familyMemberId, userId);
        
        Page<Comment> comments = commentRepository.findByUserIdAndStatusActive(familyMemberId, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting comments by generation: {} for user: {}", generationLevel, userId);
        
        Page<Comment> comments = commentRepository.findByGenerationLevelAndStatusActive(generationLevel, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByCulturalContext(String culturalTag, Pageable pageable, UUID userId) {
        log.info("Getting comments by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<Comment> comments = commentRepository.findByCulturalTagAndStatusActive(culturalTag, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    // =============================================================================
    // Search and Discovery
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> searchComments(String searchText, Pageable pageable, UUID userId) {
        log.info("Searching comments with text: {} for user: {}", searchText, userId);
        
        Page<Comment> comments = commentRepository.searchByCommentText(searchText, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByHashtag(String hashtag, Pageable pageable, UUID userId) {
        log.info("Getting comments by hashtag: {} for user: {}", hashtag, userId);
        
        Page<Comment> comments = commentRepository.findByHashtagAndStatusActive(hashtag, pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<String> getTrendingHashtags(int limit, UUID userId) {
        log.info("Getting trending hashtags for user: {}", userId);
        
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<String> hashtags = commentRepository.findTrendingHashtags(sevenDaysAgo);
        
        // Process and return top hashtags
        return hashtags.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // =============================================================================
    // Moderation Operations
    // =============================================================================
    
    @Override
    public void moderateComment(Long commentId, boolean approved, UUID moderatorId, String reason) {
        log.info("Moderating comment: {} by moderator: {} - approved: {}", commentId, moderatorId, approved);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        comment.setModerationStatus(approved ? Comment.ModerationStatus.APPROVED : Comment.ModerationStatus.REJECTED);
        comment.setMetadata(updateModerationMetadata(comment.getMetadata(), moderatorId, reason, approved));
        
        commentRepository.save(comment);
        
        log.info("Comment moderated successfully: {} - approved: {}", commentId, approved);
    }
    
    @Override
    public void flagComment(Long commentId, UUID userId, String reason) {
        log.info("Flagging comment: {} by user: {} - reason: {}", commentId, userId, reason);
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        
        comment.setModerationStatus(Comment.ModerationStatus.FLAGGED);
        comment.setMetadata(updateFlagMetadata(comment.getMetadata(), userId, reason));
        
        commentRepository.save(comment);
        
        log.info("Comment flagged successfully: {}", commentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsPendingModeration(Pageable pageable, UUID moderatorId) {
        log.info("Getting comments pending moderation for moderator: {}", moderatorId);
        
        Page<Comment> comments = commentRepository.findCommentsPendingModeration(pageable);
        
        return comments.map(comment -> convertToCommentResponse(comment, moderatorId));
    }
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public CommentStatistics getCommentStatistics(UUID contentId, UUID userId) {
        log.info("Getting comment statistics for content: {} by user: {}", contentId, userId);
        
        Long totalComments = commentRepository.countByContentIdAndStatusActive(contentId);
        Long totalReplies = commentRepository.countRepliesByParentCommentId(0L);
        Double averageSentiment = commentRepository.getAverageSentimentScoreByContentId(contentId);
        
        // Get top hashtags and mentions
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<String> topHashtags = commentRepository.getTopHashtags(thirtyDaysAgo);
        List<String> topMentions = commentRepository.getTopMentions(thirtyDaysAgo);
        
        return new CommentStatistics(
                totalComments,
                totalReplies,
                0L, // totalLikes - would integrate with like system
                0L, // totalReactions - would integrate with reaction system
                averageSentiment,
                topHashtags,
                topMentions
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public FamilyCommentActivity getFamilyCommentActivity(UUID familyId, UUID userId) {
        log.info("Getting family comment activity for family: {} by user: {}", familyId, userId);
        
        // Get generation activity
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Object[]> generationData = commentRepository.getFamilyCommentActivityByGeneration(thirtyDaysAgo);
        List<GenerationActivity> generationActivity = generationData.stream()
                .map(data -> new GenerationActivity(
                        (Integer) data[0],
                        "Generation " + data[0],
                        (Long) data[1],
                        0L // activeMembers - would integrate with user service
                ))
                .collect(Collectors.toList());
        
        // Get cultural activity
        List<Object[]> culturalData = commentRepository.getFamilyCommentActivityByCulturalContext(thirtyDaysAgo);
        List<CulturalActivity> culturalActivity = culturalData.stream()
                .map(data -> new CulturalActivity(
                        (String) data[0],
                        (String) data[0],
                        (Long) data[1],
                        0L // uniqueCommenters - would integrate with user service
                ))
                .collect(Collectors.toList());
        
        return new FamilyCommentActivity(
                0L, // totalComments - would calculate from data
                0L, // activeCommenters - would integrate with user service
                generationActivity,
                culturalActivity
        );
    }
    
    // =============================================================================
    // Private Helper Methods
    // =============================================================================
    
    private CommentResponse convertToCommentResponse(Comment comment, UUID userId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .contentId(comment.getContentId())
                .userId(comment.getUserId())
                .userName("User Name") // Would integrate with User Service
                .userAvatar("User Avatar") // Would integrate with User Service
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .commentText(comment.getCommentText())
                .mentions(convertJsonToList(comment.getMentions()))
                .hashtags(convertJsonToList(comment.getHashtags()))
                .mediaUrls(convertJsonToList(comment.getMediaUrls()))
                .isEdited(comment.getIsEdited())
                .editCount(comment.getEditCount())
                .editHistory(comment.getEditHistory())
                .status(comment.getStatus())
                .moderationStatus(comment.getModerationStatus())
                .familyContext(comment.getFamilyContext())
                .culturalTags(convertJsonToList(comment.getCulturalTags()))
                .generationLevel(comment.getGenerationLevel())
                .generationName("Generation " + comment.getGenerationLevel())
                .relationshipContext(comment.getRelationshipContext())
                .sentimentScore(comment.getSentimentScore())
                .languageCode(comment.getLanguageCode())
                .isAnonymous(comment.getIsAnonymous())
                .isPrivate(comment.getIsPrivate())
                .replyCount(comment.getReplyCount())
                .likeCount(comment.getLikeCount())
                .reactionCount(comment.getReactionCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .hasUserLiked(hasUserLikedComment(comment.getId(), userId))
                .hasUserReacted(false) // Would integrate with reaction system
                .userReactionType(null) // Would integrate with reaction system
                .familyMemberName("Family Member") // Would integrate with User Service
                .relationshipToUser("Relationship") // Would integrate with Relationship Service
                .isFromSameGeneration(false) // Would calculate based on user data
                .isFromSameFamily(false) // Would calculate based on family data
                .culturalContext("Cultural Context") // Would process cultural tags
                .emotionalContext("Emotional Context") // Would process sentiment
                .moderationReason(null) // Would extract from metadata
                .moderatorId(null) // Would extract from metadata
                .moderatorName(null) // Would integrate with User Service
                .moderatedAt(null) // Would extract from metadata
                .build();
    }
    
    private void updateParentCommentReplyCount(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElse(null);
        if (parentComment != null) {
            Long replyCount = commentRepository.countRepliesByParentCommentId(parentCommentId);
            parentComment.setReplyCount(replyCount.intValue());
            commentRepository.save(parentComment);
        }
    }
    
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        // Simple JSON array conversion - in production, use proper JSON library
        return "[" + list.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(",")) + "]";
    }
    
    private List<String> convertJsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        // Simple JSON array parsing - in production, use proper JSON library
        return List.of(json.replaceAll("[\\[\\]\"]", "").split(","));
    }
    
    private String updateEditHistory(String currentHistory, String editReason) {
        // Simple edit history update - in production, use proper JSON library
        String newEntry = "{\"timestamp\":\"" + LocalDateTime.now() + "\",\"reason\":\"" + editReason + "\"}";
        if (currentHistory == null || currentHistory.isEmpty()) {
            return "[" + newEntry + "]";
        }
        return currentHistory.substring(0, currentHistory.length() - 1) + "," + newEntry + "]";
    }
    
    private String updateModerationMetadata(String currentMetadata, UUID moderatorId, String reason, boolean approved) {
        // Simple metadata update - in production, use proper JSON library
        String newEntry = "{\"moderatorId\":\"" + moderatorId + "\",\"reason\":\"" + reason + "\",\"approved\":" + approved + ",\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        if (currentMetadata == null || currentMetadata.isEmpty()) {
            return "{\"moderation\":" + newEntry + "}";
        }
        return currentMetadata.substring(0, currentMetadata.length() - 1) + ",\"moderation\":" + newEntry + "}";
    }
    
    private String updateFlagMetadata(String currentMetadata, UUID userId, String reason) {
        // Simple metadata update - in production, use proper JSON library
        String newEntry = "{\"userId\":\"" + userId + "\",\"reason\":\"" + reason + "\",\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        if (currentMetadata == null || currentMetadata.isEmpty()) {
            return "{\"flags\":[" + newEntry + "]}";
        }
        return currentMetadata.substring(0, currentMetadata.length() - 1) + ",\"flags\":[" + newEntry + "]}";
    }
}
