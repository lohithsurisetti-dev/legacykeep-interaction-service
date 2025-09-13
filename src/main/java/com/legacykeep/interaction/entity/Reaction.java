package com.legacykeep.interaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reaction Entity
 * 
 * Represents user reactions to family legacy content.
 * Supports multiple reaction types with intensity levels and family context.
 * Designed to capture the emotional response of family members to legacy content.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "reactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content_id", nullable = false)
    private UUID contentId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, length = 20)
    private ReactionType reactionType;
    
    @Column(name = "intensity", nullable = false)
    @Builder.Default
    private Integer intensity = 1; // 1-5 scale for reaction intensity
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_context", columnDefinition = "jsonb")
    private String familyContext; // JSON object with family-specific context
    
    @Column(name = "generation_level")
    private Integer generationLevel; // Generation level of the reactor
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relationship_context", columnDefinition = "jsonb")
    private String relationshipContext; // JSON object with relationship context
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cultural_context", columnDefinition = "jsonb")
    private String culturalContext; // JSON object with cultural context
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "emotional_context", columnDefinition = "jsonb")
    private String emotionalContext; // JSON object with emotional context
    
    @Column(name = "is_anonymous", nullable = false)
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Column(name = "is_private", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // Additional metadata for the reaction
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Reaction Type Enumeration
     */
    public enum ReactionType {
        // Core Emotional Reactions
        LIKE("LIKE", "Like", "👍", "#4CAF50", "CORE"),
        LOVE("LOVE", "Love", "❤️", "#E91E63", "CORE"),
        HEART("HEART", "Heart", "💖", "#F06292", "CORE"),
        LAUGH("LAUGH", "Laugh", "😂", "#FF9800", "CORE"),
        WOW("WOW", "Wow", "😮", "#9C27B0", "CORE"),
        SAD("SAD", "Sad", "😢", "#607D8B", "CORE"),
        ANGRY("ANGRY", "Angry", "😠", "#F44336", "CORE"),
        
        // Family-Specific Reactions
        BLESSING("BLESSING", "Blessing", "🙏", "#8BC34A", "FAMILY"),
        PRIDE("PRIDE", "Pride", "🏆", "#FFC107", "FAMILY"),
        GRATITUDE("GRATITUDE", "Gratitude", "🙏", "#4CAF50", "FAMILY"),
        MEMORY("MEMORY", "Memory", "🧠", "#9E9E9E", "FAMILY"),
        WISDOM("WISDOM", "Wisdom", "🧙‍♂️", "#795548", "FAMILY"),
        TRADITION("TRADITION", "Tradition", "🏛️", "#3F51B5", "FAMILY"),
        RESPECT("RESPECT", "Respect", "🙇‍♂️", "#795548", "FAMILY"),
        HONOR("HONOR", "Honor", "👑", "#FFD700", "FAMILY"),
        LEGACY("LEGACY", "Legacy", "📜", "#8D6E63", "FAMILY"),
        HERITAGE("HERITAGE", "Heritage", "🏛️", "#5D4037", "FAMILY"),
        
        // Generational Reactions
        GRANDPARENT("GRANDPARENT", "Grandparent", "👴", "#9E9E9E", "GENERATIONAL"),
        PARENT("PARENT", "Parent", "👨", "#607D8B", "GENERATIONAL"),
        CHILD("CHILD", "Child", "👶", "#FF9800", "GENERATIONAL"),
        SIBLING("SIBLING", "Sibling", "👫", "#E91E63", "GENERATIONAL"),
        
        // Cultural Reactions
        NAMASTE("NAMASTE", "Namaste", "🙏", "#4CAF50", "CULTURAL"),
        OM("OM", "Om", "🕉️", "#9C27B0", "CULTURAL"),
        FESTIVAL("FESTIVAL", "Festival", "🎉", "#FF5722", "CULTURAL"),
        PRAYER("PRAYER", "Prayer", "🙏", "#795548", "CULTURAL"),
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
    }
    
    /**
     * Check if this reaction is family-specific
     */
    public boolean isFamilySpecific() {
        return reactionType.isFamilySpecific();
    }
    
    /**
     * Check if this reaction is generational
     */
    public boolean isGenerational() {
        return reactionType.isGenerational();
    }
    
    /**
     * Check if this reaction is cultural
     */
    public boolean isCultural() {
        return reactionType.isCultural();
    }
    
    /**
     * Check if this reaction is core emotional
     */
    public boolean isCore() {
        return reactionType.isCore();
    }
    
    /**
     * Check if this reaction has high intensity
     */
    public boolean isHighIntensity() {
        return intensity >= 4;
    }
    
    /**
     * Check if this reaction has low intensity
     */
    public boolean isLowIntensity() {
        return intensity <= 2;
    }
    
    /**
     * Check if this reaction is visible to family
     */
    public boolean isVisible() {
        return !isPrivate;
    }
    
    /**
     * Check if this reaction is anonymous
     */
    public boolean isAnonymous() {
        return isAnonymous;
    }
}
