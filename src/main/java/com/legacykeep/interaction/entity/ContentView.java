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
 * Content View Entity
 * 
 * Represents content view tracking for analytics with family context.
 * Tracks view duration, completion percentage, and engagement metrics.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "content_views")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentView {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content_id", nullable = false)
    private UUID contentId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "view_duration")
    private Integer viewDuration; // in seconds
    
    @Column(name = "view_completion_percentage")
    private Double viewCompletionPercentage;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_context", columnDefinition = "jsonb")
    private String familyContext;
    
    @Column(name = "generation_level")
    private Integer generationLevel;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relationship_context", columnDefinition = "jsonb")
    private String relationshipContext;
    
    @Column(name = "is_anonymous", nullable = false)
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
