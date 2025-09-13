package com.legacykeep.interaction.enums;

/**
 * Reaction Type Enumeration
 * 
 * Defines all available reaction types for family legacy content.
 * Each reaction is designed to capture the emotional response of family members
 * to legacy content, with cultural sensitivity and family context in mind.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public enum ReactionType {
    
    // =============================================================================
    // Core Emotional Reactions
    // =============================================================================
    
    /**
     * Like reaction - Simple appreciation
     */
    LIKE("LIKE", "Like", "👍", "#4CAF50", "CORE"),
    
    /**
     * Love reaction - Deep emotional connection
     */
    LOVE("LOVE", "Love", "❤️", "#E91E63", "CORE"),
    
    /**
     * Heart reaction - Family warmth and affection
     */
    HEART("HEART", "Heart", "💖", "#F06292", "CORE"),
    
    /**
     * Laugh reaction - Joy and humor
     */
    LAUGH("LAUGH", "Laugh", "😂", "#FF9800", "CORE"),
    
    /**
     * Wow reaction - Amazement and wonder
     */
    WOW("WOW", "Wow", "😮", "#9C27B0", "CORE"),
    
    /**
     * Sad reaction - Empathy and emotional support
     */
    SAD("SAD", "Sad", "😢", "#607D8B", "CORE"),
    
    /**
     * Angry reaction - Strong emotional response
     */
    ANGRY("ANGRY", "Angry", "😠", "#F44336", "CORE"),
    
    // =============================================================================
    // Family-Specific Reactions
    // =============================================================================
    
    /**
     * Blessing reaction - Traditional family blessing
     */
    BLESSING("BLESSING", "Blessing", "🙏", "#8BC34A", "FAMILY"),
    
    /**
     * Pride reaction - Family pride and honor
     */
    PRIDE("PRIDE", "Pride", "🏆", "#FFC107", "FAMILY"),
    
    /**
     * Gratitude reaction - Thankfulness and appreciation
     */
    GRATITUDE("GRATITUDE", "Gratitude", "🙏", "#4CAF50", "FAMILY"),
    
    /**
     * Memory reaction - Nostalgic memory trigger
     */
    MEMORY("MEMORY", "Memory", "🧠", "#9E9E9E", "FAMILY"),
    
    /**
     * Wisdom reaction - Appreciation for family wisdom
     */
    WISDOM("WISDOM", "Wisdom", "🧙‍♂️", "#795548", "FAMILY"),
    
    /**
     * Tradition reaction - Cultural and family tradition
     */
    TRADITION("TRADITION", "Tradition", "🏛️", "#3F51B5", "FAMILY"),
    
    /**
     * Respect reaction - Deep respect for elders and family
     */
    RESPECT("RESPECT", "Respect", "🙇‍♂️", "#795548", "FAMILY"),
    
    /**
     * Honor reaction - Honoring family heritage
     */
    HONOR("HONOR", "Honor", "👑", "#FFD700", "FAMILY"),
    
    /**
     * Legacy reaction - Appreciation for family legacy
     */
    LEGACY("LEGACY", "Legacy", "📜", "#8D6E63", "FAMILY"),
    
    /**
     * Heritage reaction - Cultural heritage appreciation
     */
    HERITAGE("HERITAGE", "Heritage", "🏛️", "#5D4037", "FAMILY"),
    
    // =============================================================================
    // Generational Reactions
    // =============================================================================
    
    /**
     * Grandparent reaction - Special reaction for grandparent content
     */
    GRANDPARENT("GRANDPARENT", "Grandparent", "👴", "#9E9E9E", "GENERATIONAL"),
    
    /**
     * Parent reaction - Special reaction for parent content
     */
    PARENT("PARENT", "Parent", "👨", "#607D8B", "GENERATIONAL"),
    
    /**
     * Child reaction - Special reaction for child content
     */
    CHILD("CHILD", "Child", "👶", "#FF9800", "GENERATIONAL"),
    
    /**
     * Sibling reaction - Special reaction for sibling content
     */
    SIBLING("SIBLING", "Sibling", "👫", "#E91E63", "GENERATIONAL"),
    
    // =============================================================================
    // Cultural Reactions
    // =============================================================================
    
    /**
     * Namaste reaction - Traditional greeting and respect
     */
    NAMASTE("NAMASTE", "Namaste", "🙏", "#4CAF50", "CULTURAL"),
    
    /**
     * Om reaction - Spiritual and cultural significance
     */
    OM("OM", "Om", "🕉️", "#9C27B0", "CULTURAL"),
    
    /**
     * Festival reaction - Festival and celebration
     */
    FESTIVAL("FESTIVAL", "Festival", "🎉", "#FF5722", "CULTURAL"),
    
    /**
     * Prayer reaction - Spiritual and religious
     */
    PRAYER("PRAYER", "Prayer", "🙏", "#795548", "CULTURAL"),
    
    /**
     * Ritual reaction - Traditional family rituals
     */
    RITUAL("RITUAL", "Ritual", "🕯️", "#3F51B5", "CULTURAL");
    
    private final String typeName;
    private final String displayName;
    private final String icon;
    private final String colorCode;
    private final String category;
    
    ReactionType(String typeName, String displayName, String icon, String colorCode, String category) {
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
     * Check if this reaction type is family-specific
     */
    public boolean isFamilySpecific() {
        return "FAMILY".equals(category);
    }
    
    /**
     * Check if this reaction type is generational
     */
    public boolean isGenerational() {
        return "GENERATIONAL".equals(category);
    }
    
    /**
     * Check if this reaction type is cultural
     */
    public boolean isCultural() {
        return "CULTURAL".equals(category);
    }
    
    /**
     * Check if this reaction type is core emotional
     */
    public boolean isCore() {
        return "CORE".equals(category);
    }
    
    /**
     * Get reaction type by name
     */
    public static ReactionType fromTypeName(String typeName) {
        for (ReactionType type : values()) {
            if (type.typeName.equals(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown reaction type: " + typeName);
    }
    
    /**
     * Get all reaction types by category
     */
    public static ReactionType[] getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.category.equals(category))
                .toArray(ReactionType[]::new);
    }
    
    /**
     * Get family-specific reactions
     */
    public static ReactionType[] getFamilyReactions() {
        return getByCategory("FAMILY");
    }
    
    /**
     * Get generational reactions
     */
    public static ReactionType[] getGenerationalReactions() {
        return getByCategory("GENERATIONAL");
    }
    
    /**
     * Get cultural reactions
     */
    public static ReactionType[] getCulturalReactions() {
        return getByCategory("CULTURAL");
    }
    
    /**
     * Get core reactions
     */
    public static ReactionType[] getCoreReactions() {
        return getByCategory("CORE");
    }
}
