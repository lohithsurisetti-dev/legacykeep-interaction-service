package com.legacykeep.interaction.dto.response;

import com.legacykeep.interaction.entity.Reaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reaction Response DTO
 * 
 * Response object for reaction operations in the family legacy system.
 * Includes all reaction information with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {
    
    private Long id;
    
    private UUID contentId;
    
    private UUID userId;
    
    private String userName; // User display name
    
    private String userAvatar; // User avatar URL
    
    private Reaction.ReactionType reactionType;
    
    private String reactionTypeName; // Display name for reaction type
    
    private String reactionIcon; // Icon for reaction type
    
    private String reactionColor; // Color for reaction type
    
    private String reactionCategory; // Category of reaction type
    
    private Integer intensity;
    
    private String intensityDescription; // Description of intensity level
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String generationName; // Display name for generation level
    
    private String relationshipContext;
    
    private String culturalContext;
    
    private String emotionalContext;
    
    private Boolean isAnonymous;
    
    private Boolean isPrivate;
    
    private String metadata;
    
    private LocalDateTime createdAt;
    
    // Family context information
    private String familyMemberName; // Family member display name
    
    private String relationshipToUser; // Relationship to the requesting user
    
    private Boolean isFromSameGeneration;
    
    private Boolean isFromSameFamily;
    
    // Cultural context
    private String culturalDisplayName; // Display name for cultural context
    
    private String emotionalDisplayName; // Display name for emotional context
    
    // Reaction type information
    private Boolean isFamilySpecific;
    
    private Boolean isGenerational;
    
    private Boolean isCultural;
    
    private Boolean isCore;
    
    // Intensity information
    private Boolean isHighIntensity;
    
    private Boolean isLowIntensity;
    
    private String intensityLevel; // "LOW", "MEDIUM", "HIGH"
}
