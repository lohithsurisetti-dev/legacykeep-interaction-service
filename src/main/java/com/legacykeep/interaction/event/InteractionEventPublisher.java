package com.legacykeep.interaction.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Interaction Event Publisher
 * 
 * Publishes interaction events to Kafka for other services to consume.
 * Handles event publishing for comments, reactions, and other interactions.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InteractionEventPublisher {
    
    private final KafkaTemplate<String, InteractionEvent> kafkaTemplate;
    
    private static final String INTERACTION_EVENTS_TOPIC = "interaction-events";
    private static final String COMMENT_EVENTS_TOPIC = "comment-events";
    private static final String REACTION_EVENTS_TOPIC = "reaction-events";
    private static final String NOTIFICATION_EVENTS_TOPIC = "notification-events";
    
    /**
     * Publish interaction event to Kafka
     */
    public void publishInteractionEvent(InteractionEvent event) {
        try {
            log.info("Publishing interaction event: {} for content: {}", event.getEventType(), event.getContentId());
            
            // Publish to general interaction events topic
            kafkaTemplate.send(INTERACTION_EVENTS_TOPIC, event.getEventId(), event);
            
            // Publish to specific topic based on event type
            if (event.getEventType().startsWith("COMMENT")) {
                kafkaTemplate.send(COMMENT_EVENTS_TOPIC, event.getEventId(), event);
            } else if (event.getEventType().startsWith("REACTION")) {
                kafkaTemplate.send(REACTION_EVENTS_TOPIC, event.getEventId(), event);
            }
            
            // Publish to notification events topic for notification service
            if (shouldNotify(event)) {
                kafkaTemplate.send(NOTIFICATION_EVENTS_TOPIC, event.getEventId(), event);
            }
            
            log.info("Successfully published interaction event: {}", event.getEventId());
            
        } catch (Exception e) {
            log.error("Failed to publish interaction event: {}", event.getEventId(), e);
            // In production, you might want to implement retry logic or dead letter queue
        }
    }
    
    /**
     * Publish comment created event
     */
    public void publishCommentCreated(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId, String commentText) {
        InteractionEvent event = InteractionEvent.commentCreated(userId, contentId, familyId, commentId, commentText);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment updated event
     */
    public void publishCommentUpdated(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId, String commentText) {
        InteractionEvent event = InteractionEvent.commentUpdated(userId, contentId, familyId, commentId, commentText);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment deleted event
     */
    public void publishCommentDeleted(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId) {
        InteractionEvent event = InteractionEvent.commentDeleted(userId, contentId, familyId, commentId);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish reaction added event
     */
    public void publishReactionAdded(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long reactionId, String reactionType, Integer intensity) {
        InteractionEvent event = InteractionEvent.reactionAdded(userId, contentId, familyId, reactionId, reactionType, intensity);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish reaction updated event
     */
    public void publishReactionUpdated(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long reactionId, String reactionType, Integer intensity) {
        InteractionEvent event = InteractionEvent.reactionUpdated(userId, contentId, familyId, reactionId, reactionType, intensity);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish reaction removed event
     */
    public void publishReactionRemoved(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long reactionId, String reactionType) {
        InteractionEvent event = InteractionEvent.reactionRemoved(userId, contentId, familyId, reactionId, reactionType);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment liked event
     */
    public void publishCommentLiked(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId) {
        InteractionEvent event = InteractionEvent.commentLiked(userId, contentId, familyId, commentId);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment unliked event
     */
    public void publishCommentUnliked(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId) {
        InteractionEvent event = InteractionEvent.commentUnliked(userId, contentId, familyId, commentId);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment flagged event
     */
    public void publishCommentFlagged(java.util.UUID userId, java.util.UUID contentId, java.util.UUID familyId, Long commentId, String reason) {
        InteractionEvent event = InteractionEvent.commentFlagged(userId, contentId, familyId, commentId, reason);
        publishInteractionEvent(event);
    }
    
    /**
     * Publish comment moderated event
     */
    public void publishCommentModerated(java.util.UUID moderatorId, java.util.UUID contentId, java.util.UUID familyId, Long commentId, boolean approved, String reason) {
        InteractionEvent event = InteractionEvent.commentModerated(moderatorId, contentId, familyId, commentId, approved, reason);
        publishInteractionEvent(event);
    }
    
    /**
     * Check if event should trigger notifications
     */
    private boolean shouldNotify(InteractionEvent event) {
        // Define which events should trigger notifications
        return event.getEventType().equals("COMMENT_CREATED") ||
               event.getEventType().equals("REACTION_ADDED") ||
               event.getEventType().equals("COMMENT_LIKED") ||
               event.getEventType().equals("COMMENT_FLAGGED") ||
               event.getEventType().equals("COMMENT_MODERATED");
    }
}
