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
 * Bookmark Entity
 * 
 * Represents user bookmarks for content with family context and organization.
 * Supports saving content for later with custom names and descriptions.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "bookmarks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content_id", nullable = false)
    private UUID contentId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "bookmark_name", length = 100)
    private String bookmarkName;
    
    @Column(name = "bookmark_description", columnDefinition = "TEXT")
    private String bookmarkDescription;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_context", columnDefinition = "jsonb")
    private String familyContext;
    
    @Column(name = "generation_level")
    private Integer generationLevel;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relationship_context", columnDefinition = "jsonb")
    private String relationshipContext;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cultural_context", columnDefinition = "jsonb")
    private String culturalContext;
    
    @Column(name = "is_private", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
