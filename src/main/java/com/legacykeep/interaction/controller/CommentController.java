package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateCommentRequest;
import com.legacykeep.interaction.dto.request.UpdateCommentRequest;
import com.legacykeep.interaction.dto.response.CommentResponse;
import com.legacykeep.interaction.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Comment Controller
 * 
 * REST controller for comment operations in the family legacy system.
 * Provides comprehensive comment management with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comment Management", description = "APIs for managing comments on family legacy content")
public class CommentController {
    
    private final CommentService commentService;
    
    // =============================================================================
    // Comment CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Create a new comment", description = "Create a new comment on family legacy content")
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CreateCommentRequest request,
            @Parameter(description = "User ID creating the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating comment for content: {} by user: {}", request.getContentId(), userId);
        
        CommentResponse response = commentService.createComment(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get comments for content", description = "Get paginated comments for specific content")
    public ResponseEntity<Page<CommentResponse>> getCommentsForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting comments") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments for content: {} by user: {}", contentId, userId);
        
        Page<CommentResponse> response = commentService.getCommentsForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{commentId}")
    @Operation(summary = "Get comment by ID", description = "Get a specific comment by its ID")
    public ResponseEntity<CommentResponse> getCommentById(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID requesting the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting comment by ID: {} for user: {}", commentId, userId);
        
        CommentResponse response = commentService.getCommentById(commentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{commentId}")
    @Operation(summary = "Update comment", description = "Update an existing comment")
    public ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @Parameter(description = "User ID updating the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Updating comment: {} by user: {}", commentId, userId);
        
        CommentResponse response = commentService.updateComment(commentId, request, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment", description = "Delete a comment (soft delete)")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID deleting the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Deleting comment: {} by user: {}", commentId, userId);
        
        commentService.deleteComment(commentId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    // =============================================================================
    // Reply Operations
    // =============================================================================
    
    @PostMapping("/{parentCommentId}/replies")
    @Operation(summary = "Create a reply", description = "Create a reply to an existing comment")
    public ResponseEntity<CommentResponse> createReply(
            @Parameter(description = "Parent comment ID") @PathVariable Long parentCommentId,
            @Valid @RequestBody CreateCommentRequest request,
            @Parameter(description = "User ID creating the reply") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating reply to comment: {} by user: {}", parentCommentId, userId);
        
        CommentResponse response = commentService.createReply(parentCommentId, request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{parentCommentId}/replies")
    @Operation(summary = "Get replies for comment", description = "Get paginated replies for a specific comment")
    public ResponseEntity<Page<CommentResponse>> getRepliesForComment(
            @Parameter(description = "Parent comment ID") @PathVariable Long parentCommentId,
            @Parameter(description = "User ID requesting replies") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting replies for comment: {} by user: {}", parentCommentId, userId);
        
        Page<CommentResponse> response = commentService.getRepliesForComment(parentCommentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{commentId}/thread")
    @Operation(summary = "Get comment thread", description = "Get comment thread with all replies")
    public ResponseEntity<CommentResponse> getCommentThread(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID requesting the thread") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting comment thread for comment: {} by user: {}", commentId, userId);
        
        CommentResponse response = commentService.getCommentThread(commentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Comment Interactions
    // =============================================================================
    
    @PostMapping("/{commentId}/like")
    @Operation(summary = "Like a comment", description = "Like a comment")
    public ResponseEntity<Void> likeComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID liking the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Liking comment: {} by user: {}", commentId, userId);
        
        commentService.likeComment(commentId, userId);
        
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{commentId}/like")
    @Operation(summary = "Unlike a comment", description = "Unlike a comment")
    public ResponseEntity<Void> unlikeComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID unliking the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Unliking comment: {} by user: {}", commentId, userId);
        
        commentService.unlikeComment(commentId, userId);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{commentId}/liked")
    @Operation(summary = "Check if user liked comment", description = "Check if user has liked a comment")
    public ResponseEntity<Boolean> hasUserLikedComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Checking if user: {} liked comment: {}", userId, commentId);
        
        boolean hasLiked = commentService.hasUserLikedComment(commentId, userId);
        
        return ResponseEntity.ok(hasLiked);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get comments by family member", description = "Get comments by a specific family member")
    public ResponseEntity<Page<CommentResponse>> getCommentsByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting comments") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments by family member: {} for user: {}", familyMemberId, userId);
        
        Page<CommentResponse> response = commentService.getCommentsByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get comments by generation", description = "Get comments by generation level")
    public ResponseEntity<Page<CommentResponse>> getCommentsByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting comments") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments by generation: {} for user: {}", generationLevel, userId);
        
        Page<CommentResponse> response = commentService.getCommentsByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cultural/{culturalTag}")
    @Operation(summary = "Get comments by cultural context", description = "Get comments by cultural tag")
    public ResponseEntity<Page<CommentResponse>> getCommentsByCulturalContext(
            @Parameter(description = "Cultural tag") @PathVariable String culturalTag,
            @Parameter(description = "User ID requesting comments") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<CommentResponse> response = commentService.getCommentsByCulturalContext(culturalTag, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Search and Discovery
    // =============================================================================
    
    @GetMapping("/search")
    @Operation(summary = "Search comments", description = "Search comments by text content")
    public ResponseEntity<Page<CommentResponse>> searchComments(
            @Parameter(description = "Search text") @RequestParam String searchText,
            @Parameter(description = "User ID performing search") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Searching comments with text: {} for user: {}", searchText, userId);
        
        Page<CommentResponse> response = commentService.searchComments(searchText, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/hashtag/{hashtag}")
    @Operation(summary = "Get comments by hashtag", description = "Get comments by hashtag")
    public ResponseEntity<Page<CommentResponse>> getCommentsByHashtag(
            @Parameter(description = "Hashtag") @PathVariable String hashtag,
            @Parameter(description = "User ID requesting comments") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments by hashtag: {} for user: {}", hashtag, userId);
        
        Page<CommentResponse> response = commentService.getCommentsByHashtag(hashtag, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/trending/hashtags")
    @Operation(summary = "Get trending hashtags", description = "Get trending hashtags in comments")
    public ResponseEntity<List<String>> getTrendingHashtags(
            @Parameter(description = "Number of trending hashtags to return") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "User ID requesting trending hashtags") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting trending hashtags for user: {}", userId);
        
        List<String> response = commentService.getTrendingHashtags(limit, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Moderation Operations
    // =============================================================================
    
    @PostMapping("/{commentId}/moderate")
    @Operation(summary = "Moderate comment", description = "Moderate a comment (approve/reject)")
    public ResponseEntity<Void> moderateComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "Approval status") @RequestParam boolean approved,
            @Parameter(description = "Moderation reason") @RequestParam(required = false) String reason,
            @Parameter(description = "Moderator user ID") @RequestHeader("X-User-ID") UUID moderatorId) {
        
        log.info("Moderating comment: {} by moderator: {} - approved: {}", commentId, moderatorId, approved);
        
        commentService.moderateComment(commentId, approved, moderatorId, reason);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{commentId}/flag")
    @Operation(summary = "Flag comment", description = "Flag a comment for moderation")
    public ResponseEntity<Void> flagComment(
            @Parameter(description = "Comment ID") @PathVariable Long commentId,
            @Parameter(description = "Flagging reason") @RequestParam String reason,
            @Parameter(description = "User ID flagging the comment") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Flagging comment: {} by user: {} - reason: {}", commentId, userId, reason);
        
        commentService.flagComment(commentId, userId, reason);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/moderation/pending")
    @Operation(summary = "Get comments pending moderation", description = "Get comments pending moderation")
    public ResponseEntity<Page<CommentResponse>> getCommentsPendingModeration(
            @Parameter(description = "Moderator user ID") @RequestHeader("X-User-ID") UUID moderatorId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting comments pending moderation for moderator: {}", moderatorId);
        
        Page<CommentResponse> response = commentService.getCommentsPendingModeration(pageable, moderatorId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get comment statistics", description = "Get comment statistics for content")
    public ResponseEntity<CommentService.CommentStatistics> getCommentStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting comment statistics for content: {} by user: {}", contentId, userId);
        
        CommentService.CommentStatistics response = commentService.getCommentStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/family/{familyId}/activity")
    @Operation(summary = "Get family comment activity", description = "Get family comment activity summary")
    public ResponseEntity<CommentService.FamilyCommentActivity> getFamilyCommentActivity(
            @Parameter(description = "Family ID") @PathVariable UUID familyId,
            @Parameter(description = "User ID requesting summary") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting family comment activity for family: {} by user: {}", familyId, userId);
        
        CommentService.FamilyCommentActivity response = commentService.getFamilyCommentActivity(familyId, userId);
        
        return ResponseEntity.ok(response);
    }
}
