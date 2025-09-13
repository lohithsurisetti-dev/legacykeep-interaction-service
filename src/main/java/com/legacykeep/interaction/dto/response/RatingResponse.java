package com.legacykeep.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Rating Response DTO
 * 
 * Response DTO for rating operations with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    
    private Long id;
    private UUID contentId;
    private UUID userId;
    private String userName;
    private String userAvatar;
    private Integer ratingValue;
    private String ratingText;
    private String familyContext;
    private Integer generationLevel;
    private String generationName;
    private String relationshipContext;
    private String culturalContext;
    private Boolean isAnonymous;
    private Boolean isPrivate;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Family context fields
    private String familyMemberName;
    private String relationshipToUser;
    private Boolean isFromSameGeneration;
    private Boolean isFromSameFamily;
}
