package com.legacykeep.interaction.enums;

/**
 * Interaction Type Enumeration
 * 
 * Defines all available interaction types for family legacy content.
 * Each type is designed with family context and cultural sensitivity in mind.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public enum InteractionType {
    
    // =============================================================================
    // Core Engagement Types
    // =============================================================================
    
    /**
     * Like interaction - Simple appreciation for content
     */
    LIKE("LIKE", "Like", "👍", "#4CAF50", "ENGAGEMENT"),
    
    /**
     * Love interaction - Deep emotional connection to content
     */
    LOVE("LOVE", "Love", "❤️", "#E91E63", "ENGAGEMENT"),
    
    /**
     * Heart interaction - Family warmth and affection
     */
    HEART("HEART", "Heart", "💖", "#F06292", "ENGAGEMENT"),
    
    /**
     * Laugh interaction - Joy and humor in family stories
     */
    LAUGH("LAUGH", "Laugh", "😂", "#FF9800", "ENGAGEMENT"),
    
    /**
     * Wow interaction - Amazement and wonder
     */
    WOW("WOW", "Wow", "😮", "#9C27B0", "ENGAGEMENT"),
    
    /**
     * Sad interaction - Empathy and emotional support
     */
    SAD("SAD", "Sad", "😢", "#607D8B", "ENGAGEMENT"),
    
    /**
     * Angry interaction - Strong emotional response (family context)
     */
    ANGRY("ANGRY", "Angry", "😠", "#F44336", "ENGAGEMENT"),
    
    // =============================================================================
    // Family-Specific Interaction Types
    // =============================================================================
    
    /**
     * Blessing interaction - Traditional family blessing
     */
    BLESSING("BLESSING", "Blessing", "🙏", "#8BC34A", "FAMILY"),
    
    /**
     * Pride interaction - Family pride and honor
     */
    PRIDE("PRIDE", "Pride", "🏆", "#FFC107", "FAMILY"),
    
    /**
     * Gratitude interaction - Thankfulness and appreciation
     */
    GRATITUDE("GRATITUDE", "Gratitude", "🙏", "#4CAF50", "FAMILY"),
    
    /**
     * Memory interaction - Nostalgic memory trigger
     */
    MEMORY("MEMORY", "Memory", "🧠", "#9E9E9E", "FAMILY"),
    
    /**
     * Wisdom interaction - Appreciation for family wisdom
     */
    WISDOM("WISDOM", "Wisdom", "🧙‍♂️", "#795548", "FAMILY"),
    
    /**
     * Tradition interaction - Cultural and family tradition
     */
    TRADITION("TRADITION", "Tradition", "🏛️", "#3F51B5", "FAMILY"),
    
    // =============================================================================
    // Content Interaction Types
    // =============================================================================
    
    /**
     * Comment interaction - Text-based family discussion
     */
    COMMENT("COMMENT", "Comment", "💬", "#2196F3", "CONTENT"),
    
    /**
     * Reply interaction - Response to family comments
     */
    REPLY("REPLY", "Reply", "↩️", "#2196F3", "CONTENT"),
    
    /**
     * Share interaction - Sharing with family members
     */
    SHARE("SHARE", "Share", "📤", "#00BCD4", "CONTENT"),
    
    /**
     * Bookmark interaction - Saving for family reference
     */
    BOOKMARK("BOOKMARK", "Bookmark", "🔖", "#FF5722", "CONTENT"),
    
    /**
     * Rating interaction - Quality assessment for family content
     */
    RATING("RATING", "Rating", "⭐", "#FF9800", "CONTENT"),
    
    /**
     * View interaction - Content consumption tracking
     */
    VIEW("VIEW", "View", "👁️", "#607D8B", "CONTENT"),
    
    // =============================================================================
    // Social Interaction Types
    // =============================================================================
    
    /**
     * Mention interaction - Tagging family members
     */
    MENTION("MENTION", "Mention", "@", "#9C27B0", "SOCIAL"),
    
    /**
     * Tag interaction - Content categorization
     */
    TAG("TAG", "Tag", "#", "#607D8B", "SOCIAL"),
    
    /**
     * Poll interaction - Family decision making
     */
    POLL("POLL", "Poll", "📊", "#4CAF50", "SOCIAL"),
    
    /**
     * Story interaction - Temporary family updates
     */
    STORY("STORY", "Story", "📱", "#E91E63", "SOCIAL"),
    
    /**
     * Highlight interaction - Permanent story preservation
     */
    HIGHLIGHT("HIGHLIGHT", "Highlight", "⭐", "#FFC107", "SOCIAL");
    
    private final String typeName;
    private final String displayName;
    private final String icon;
    private final String colorCode;
    private final String category;
    
    InteractionType(String typeName, String displayName, String icon, String colorCode, String category) {
        this.typeName = typeName;
        this.displayName = displayName;
        this.icon = icon;
        this.colorCode = colorCode;
        this.category = category;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public String getCategory() {
        return category;
    }
    
    /**
     * Check if this interaction type is family-specific
     */
    public boolean isFamilySpecific() {
        return "FAMILY".equals(category);
    }
    
    /**
     * Check if this interaction type requires content
     */
    public boolean requiresContent() {
        return !"STORY".equals(typeName) && !"HIGHLIGHT".equals(typeName);
    }
    
    /**
     * Check if this interaction type requires user interaction
     */
    public boolean requiresUser() {
        return true; // All interactions require a user
    }
    
    /**
     * Get interaction type by name
     */
    public static InteractionType fromTypeName(String typeName) {
        for (InteractionType type : values()) {
            if (type.typeName.equals(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown interaction type: " + typeName);
    }
    
    /**
     * Get all interaction types by category
     */
    public static InteractionType[] getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.category.equals(category))
                .toArray(InteractionType[]::new);
    }
}
