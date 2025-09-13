package com.legacykeep.interaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Comment Entity
 * 
 * Represents comments and replies in the family legacy system.
 * Supports nested comment threads, mentions, hashtags, and rich family discussions.
 * Designed with family context and cultural sensitivity in mind.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content_id", nullable = false)
    private UUID contentId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "mentions", columnDefinition = "jsonb")
    private String mentions; // JSON array of mentioned user IDs
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "hashtags", columnDefinition = "jsonb")
    private String hashtags; // JSON array of hashtags
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "media_urls", columnDefinition = "jsonb")
    private String mediaUrls; // JSON array of media URLs in comment
    
    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = false;
    
    @Column(name = "edit_count", nullable = false)
    @Builder.Default
    private Integer editCount = 0;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "edit_history", columnDefinition = "jsonb")
    private String editHistory; // JSON array storing edit history
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private CommentStatus status = CommentStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false, length = 20)
    @Builder.Default
    private ModerationStatus moderationStatus = ModerationStatus.PENDING;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_context", columnDefinition = "jsonb")
    private String familyContext; // JSON object with family-specific context
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cultural_tags", columnDefinition = "jsonb")
    private String culturalTags; // JSON array of cultural tags
    
    @Column(name = "generation_level")
    private Integer generationLevel; // Generation level of the commenter
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relationship_context", columnDefinition = "jsonb")
    private String relationshipContext; // JSON object with relationship context
    
    @Column(name = "sentiment_score")
    private Double sentimentScore; // AI-calculated sentiment score
    
    @Column(name = "language_code", length = 5)
    private String languageCode; // Language of the comment
    
    @Column(name = "is_anonymous", nullable = false)
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Column(name = "is_private", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;
    
    @Column(name = "reply_count", nullable = false)
    @Builder.Default
    private Integer replyCount = 0;
    
    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private Integer likeCount = 0;
    
    @Column(name = "reaction_count", nullable = false)
    @Builder.Default
    private Integer reactionCount = 0;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Comment Status Enumeration
     */
    public enum CommentStatus {
        ACTIVE("ACTIVE", "Active", "Comment is active and visible"),
        DELETED("DELETED", "Deleted", "Comment has been deleted"),
        MODERATED("MODERATED", "Moderated", "Comment has been moderated"),
        HIDDEN("HIDDEN", "Hidden", "Comment is hidden from family view"),
        ARCHIVED("ARCHIVED", "Archived", "Comment has been archived");
        
        private final String statusName;
        private final String displayName;
        private final String description;
        
        CommentStatus(String statusName, String displayName, String description) {
            this.statusName = statusName;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getStatusName() {
            return statusName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Moderation Status Enumeration
     */
    public enum ModerationStatus {
        PENDING("PENDING", "Pending", "Comment is pending moderation"),
        APPROVED("APPROVED", "Approved", "Comment has been approved"),
        REJECTED("REJECTED", "Rejected", "Comment has been rejected"),
        FLAGGED("FLAGGED", "Flagged", "Comment has been flagged for review"),
        AUTO_APPROVED("AUTO_APPROVED", "Auto Approved", "Comment was auto-approved");
        
        private final String statusName;
        private final String displayName;
        private final String description;
        
        ModerationStatus(String statusName, String displayName, String description) {
            this.statusName = statusName;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getStatusName() {
            return statusName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Check if this comment is a reply
     */
    public boolean isReply() {
        return parentComment != null;
    }
    
    /**
     * Check if this comment is a top-level comment
     */
    public boolean isTopLevel() {
        return parentComment == null;
    }
    
    /**
     * Check if this comment is visible to family
     */
    public boolean isVisible() {
        return status == CommentStatus.ACTIVE && 
               (moderationStatus == ModerationStatus.APPROVED || 
                moderationStatus == ModerationStatus.AUTO_APPROVED);
    }
    
    /**
     * Check if this comment requires moderation
     */
    public boolean requiresModeration() {
        return moderationStatus == ModerationStatus.PENDING || 
               moderationStatus == ModerationStatus.FLAGGED;
    }
    
    /**
     * Check if this comment has been edited
     */
    public boolean hasBeenEdited() {
        return isEdited && editCount > 0;
    }
    
    /**
     * Check if this comment has replies
     */
    public boolean hasReplies() {
        return replyCount > 0;
    }
    
    /**
     * Check if this comment has reactions
     */
    public boolean hasReactions() {
        return reactionCount > 0;
    }
    
    /**
     * Check if this comment has likes
     */
    public boolean hasLikes() {
        return likeCount > 0;
    }
}
