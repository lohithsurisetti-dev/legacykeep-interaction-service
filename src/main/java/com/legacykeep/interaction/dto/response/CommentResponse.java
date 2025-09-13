package com.legacykeep.interaction.dto.response;

import com.legacykeep.interaction.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Comment Response DTO
 * 
 * Response object for comment operations in the family legacy system.
 * Includes all comment information with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    
    private Long id;
    
    private UUID contentId;
    
    private UUID userId;
    
    private String userName; // User display name
    
    private String userAvatar; // User avatar URL
    
    private Long parentCommentId;
    
    private String commentText;
    
    private List<String> mentions;
    
    private List<String> hashtags;
    
    private List<String> mediaUrls;
    
    private Boolean isEdited;
    
    private Integer editCount;
    
    private String editHistory;
    
    private Comment.CommentStatus status;
    
    private Comment.ModerationStatus moderationStatus;
    
    private String familyContext;
    
    private List<String> culturalTags;
    
    private Integer generationLevel;
    
    private String generationName; // Display name for generation level
    
    private String relationshipContext;
    
    private Double sentimentScore;
    
    private String languageCode;
    
    private Boolean isAnonymous;
    
    private Boolean isPrivate;
    
    private Integer replyCount;
    
    private Integer likeCount;
    
    private Integer reactionCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Nested replies for comment threads
    private List<CommentResponse> replies;
    
    // User interaction flags
    private Boolean hasUserLiked;
    
    private Boolean hasUserReacted;
    
    private String userReactionType;
    
    // Family context information
    private String familyMemberName; // Family member display name
    
    private String relationshipToUser; // Relationship to the requesting user
    
    private Boolean isFromSameGeneration;
    
    private Boolean isFromSameFamily;
    
    // Cultural context
    private String culturalContext; // Cultural context information
    
    private String emotionalContext; // Emotional context information
    
    // Moderation information
    private String moderationReason;
    
    private UUID moderatorId;
    
    private String moderatorName;
    
    private LocalDateTime moderatedAt;
}
