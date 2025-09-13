package com.legacykeep.interaction.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Interaction Event
 * 
 * Base event class for interaction-related events in the family legacy system.
 * Used for publishing events to Kafka for other services to consume.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionEvent {
    
    private String eventId;
    private String eventType;
    private String eventVersion;
    private LocalDateTime timestamp;
    private UUID userId;
    private UUID contentId;
    private UUID familyId;
    private String interactionType;
    private Map<String, Object> eventData;
    private Map<String, Object> metadata;
    
    /**
     * Create a comment created event
     */
    public static InteractionEvent commentCreated(UUID userId, UUID contentId, UUID familyId, Long commentId, String commentText) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_CREATED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("COMMENT")
                .eventData(Map.of(
                        "commentId", commentId,
                        "commentText", commentText,
                        "action", "created"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a comment updated event
     */
    public static InteractionEvent commentUpdated(UUID userId, UUID contentId, UUID familyId, Long commentId, String commentText) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_UPDATED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("COMMENT")
                .eventData(Map.of(
                        "commentId", commentId,
                        "commentText", commentText,
                        "action", "updated"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a comment deleted event
     */
    public static InteractionEvent commentDeleted(UUID userId, UUID contentId, UUID familyId, Long commentId) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_DELETED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("COMMENT")
                .eventData(Map.of(
                        "commentId", commentId,
                        "action", "deleted"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a reaction added event
     */
    public static InteractionEvent reactionAdded(UUID userId, UUID contentId, UUID familyId, Long reactionId, String reactionType, Integer intensity) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("REACTION_ADDED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("REACTION")
                .eventData(Map.of(
                        "reactionId", reactionId,
                        "reactionType", reactionType,
                        "intensity", intensity,
                        "action", "added"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a reaction updated event
     */
    public static InteractionEvent reactionUpdated(UUID userId, UUID contentId, UUID familyId, Long reactionId, String reactionType, Integer intensity) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("REACTION_UPDATED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("REACTION")
                .eventData(Map.of(
                        "reactionId", reactionId,
                        "reactionType", reactionType,
                        "intensity", intensity,
                        "action", "updated"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a reaction removed event
     */
    public static InteractionEvent reactionRemoved(UUID userId, UUID contentId, UUID familyId, Long reactionId, String reactionType) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("REACTION_REMOVED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("REACTION")
                .eventData(Map.of(
                        "reactionId", reactionId,
                        "reactionType", reactionType,
                        "action", "removed"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a comment liked event
     */
    public static InteractionEvent commentLiked(UUID userId, UUID contentId, UUID familyId, Long commentId) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_LIKED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("LIKE")
                .eventData(Map.of(
                        "commentId", commentId,
                        "action", "liked"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a comment unliked event
     */
    public static InteractionEvent commentUnliked(UUID userId, UUID contentId, UUID familyId, Long commentId) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_UNLIKED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("LIKE")
                .eventData(Map.of(
                        "commentId", commentId,
                        "action", "unliked"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
    
    /**
     * Create a comment flagged event
     */
    public static InteractionEvent commentFlagged(UUID userId, UUID contentId, UUID familyId, Long commentId, String reason) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_FLAGGED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(userId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("FLAG")
                .eventData(Map.of(
                        "commentId", commentId,
                        "reason", reason,
                        "action", "flagged"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "high"
                ))
                .build();
    }
    
    /**
     * Create a comment moderated event
     */
    public static InteractionEvent commentModerated(UUID moderatorId, UUID contentId, UUID familyId, Long commentId, boolean approved, String reason) {
        return InteractionEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("COMMENT_MODERATED")
                .eventVersion("1.0")
                .timestamp(LocalDateTime.now())
                .userId(moderatorId)
                .contentId(contentId)
                .familyId(familyId)
                .interactionType("MODERATION")
                .eventData(Map.of(
                        "commentId", commentId,
                        "approved", approved,
                        "reason", reason,
                        "action", "moderated"
                ))
                .metadata(Map.of(
                        "source", "interaction-service",
                        "priority", "normal"
                ))
                .build();
    }
}
