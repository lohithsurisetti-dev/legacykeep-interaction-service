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
 * Share Entity
 * 
 * Represents content sharing information with family context and targeting.
 * Supports sharing to specific users, family groups, or general sharing.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "shares")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Share {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content_id", nullable = false)
    private UUID contentId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "share_type", nullable = false)
    @Builder.Default
    private String shareType = "SHARE";
    
    @Column(name = "target_user_id")
    private UUID targetUserId;
    
    @Column(name = "target_family_id")
    private UUID targetFamilyId;
    
    @Column(name = "share_message", columnDefinition = "TEXT")
    private String shareMessage;
    
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
