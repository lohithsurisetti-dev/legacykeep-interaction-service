package com.legacykeep.interaction.enums;

/**
 * Interaction Status Enumeration
 * 
 * Defines the lifecycle status of interactions in the family legacy system.
 * Each status represents a different stage in the interaction lifecycle,
 * with family context and moderation considerations.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public enum InteractionStatus {
    
    /**
     * Active status - Interaction is active and visible to family members
     */
    ACTIVE("ACTIVE", "Active", "Interaction is active and visible"),
    
    /**
     * Pending status - Interaction is pending moderation or approval
     */
    PENDING("PENDING", "Pending", "Interaction is pending moderation"),
    
    /**
     * Approved status - Interaction has been approved by family moderators
     */
    APPROVED("APPROVED", "Approved", "Interaction has been approved"),
    
    /**
     * Rejected status - Interaction has been rejected by family moderators
     */
    REJECTED("REJECTED", "Rejected", "Interaction has been rejected"),
    
    /**
     * Flagged status - Interaction has been flagged for review
     */
    FLAGGED("FLAGGED", "Flagged", "Interaction has been flagged for review"),
    
    /**
     * Hidden status - Interaction is hidden from family view
     */
    HIDDEN("HIDDEN", "Hidden", "Interaction is hidden from family view"),
    
    /**
     * Deleted status - Interaction has been deleted by user
     */
    DELETED("DELETED", "Deleted", "Interaction has been deleted"),
    
    /**
     * Moderated status - Interaction has been moderated by family
     */
    MODERATED("MODERATED", "Moderated", "Interaction has been moderated"),
    
    /**
     * Expired status - Interaction has expired (for temporary interactions)
     */
    EXPIRED("EXPIRED", "Expired", "Interaction has expired"),
    
    /**
     * Archived status - Interaction has been archived for historical purposes
     */
    ARCHIVED("ARCHIVED", "Archived", "Interaction has been archived");
    
    private final String statusName;
    private final String displayName;
    private final String description;
    
    InteractionStatus(String statusName, String displayName, String description) {
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
    
    /**
     * Check if this status is active (visible to family)
     */
    public boolean isActive() {
        return this == ACTIVE || this == APPROVED;
    }
    
    /**
     * Check if this status is pending moderation
     */
    public boolean isPending() {
        return this == PENDING || this == FLAGGED;
    }
    
    /**
     * Check if this status is deleted or hidden
     */
    public boolean isDeleted() {
        return this == DELETED || this == HIDDEN || this == EXPIRED;
    }
    
    /**
     * Check if this status requires moderation
     */
    public boolean requiresModeration() {
        return this == PENDING || this == FLAGGED;
    }
    
    /**
     * Check if this status is visible to family
     */
    public boolean isVisible() {
        return this == ACTIVE || this == APPROVED;
    }
    
    /**
     * Get status by name
     */
    public static InteractionStatus fromStatusName(String statusName) {
        for (InteractionStatus status : values()) {
            if (status.statusName.equals(statusName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown interaction status: " + statusName);
    }
    
    /**
     * Get all active statuses
     */
    public static InteractionStatus[] getActiveStatuses() {
        return java.util.Arrays.stream(values())
                .filter(InteractionStatus::isActive)
                .toArray(InteractionStatus[]::new);
    }
    
    /**
     * Get all pending statuses
     */
    public static InteractionStatus[] getPendingStatuses() {
        return java.util.Arrays.stream(values())
                .filter(InteractionStatus::isPending)
                .toArray(InteractionStatus[]::new);
    }
    
    /**
     * Get all visible statuses
     */
    public static InteractionStatus[] getVisibleStatuses() {
        return java.util.Arrays.stream(values())
                .filter(InteractionStatus::isVisible)
                .toArray(InteractionStatus[]::new);
    }
}
