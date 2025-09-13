package com.legacykeep.interaction.dto.request;

import com.legacykeep.interaction.entity.Reaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Reaction Request DTO
 * 
 * Request object for creating new reactions to family legacy content.
 * Includes validation for reaction intensity and family context.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReactionRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @NotNull(message = "Reaction type is required")
    private Reaction.ReactionType reactionType;
    
    @Min(value = 1, message = "Intensity must be at least 1")
    @Max(value = 5, message = "Intensity must be at most 5")
    @Builder.Default
    private Integer intensity = 1;
    
    private String familyContext; // JSON string with family-specific context
    
    private Integer generationLevel; // Generation level of the reactor
    
    private String relationshipContext; // JSON string with relationship context
    
    private String culturalContext; // JSON string with cultural context
    
    private String emotionalContext; // JSON string with emotional context
    
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private String metadata; // Additional metadata for the reaction
}
