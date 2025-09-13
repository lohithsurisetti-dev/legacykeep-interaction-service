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

/**
 * Interaction Type Entity
 * 
 * Represents the configuration and metadata for different types of interactions
 * in the family legacy system. This entity stores the definition of interaction
 * types with their display properties and family context.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "interaction_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionTypeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "type_name", unique = true, nullable = false, length = 50)
    private String typeName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private InteractionCategory category;
    
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;
    
    @Column(name = "icon_name", length = 50)
    private String iconName;
    
    @Column(name = "color_code", length = 7)
    private String colorCode;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(name = "requires_content", nullable = false)
    @Builder.Default
    private Boolean requiresContent = true;
    
    @Column(name = "requires_user", nullable = false)
    @Builder.Default
    private Boolean requiresUser = true;
    
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
     * Interaction Category Enumeration
     */
    public enum InteractionCategory {
        ENGAGEMENT("ENGAGEMENT", "Engagement", "Core engagement interactions"),
        SOCIAL("SOCIAL", "Social", "Social and community interactions"),
        CONTENT("CONTENT", "Content", "Content-specific interactions"),
        FAMILY("FAMILY", "Family", "Family-specific interactions"),
        CULTURAL("CULTURAL", "Cultural", "Cultural and traditional interactions"),
        GENERATIONAL("GENERATIONAL", "Generational", "Generational interactions");
        
        private final String categoryName;
        private final String displayName;
        private final String description;
        
        InteractionCategory(String categoryName, String displayName, String description) {
            this.categoryName = categoryName;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getCategoryName() {
            return categoryName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
