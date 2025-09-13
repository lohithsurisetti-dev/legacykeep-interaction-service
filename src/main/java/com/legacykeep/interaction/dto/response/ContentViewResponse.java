package com.legacykeep.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Content View Response DTO
 * 
 * Response DTO for content view tracking operations with family context and analytics.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentViewResponse {
    
    private Long id;
    private UUID contentId;
    private UUID userId;
    private String userName;
    private String userAvatar;
    private Integer viewDuration;
    private Double viewCompletionPercentage;
    private String familyContext;
    private Integer generationLevel;
    private String generationName;
    private String relationshipContext;
    private Boolean isAnonymous;
    private String metadata;
    private LocalDateTime createdAt;
    
    // Family context fields
    private String familyMemberName;
    private String relationshipToUser;
    private Boolean isFromSameGeneration;
    private Boolean isFromSameFamily;
}
