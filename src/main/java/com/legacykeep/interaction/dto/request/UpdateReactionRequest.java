package com.legacykeep.interaction.dto.request;

import com.legacykeep.interaction.entity.Reaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Reaction Request DTO
 * 
 * Request object for updating existing reactions in the family legacy system.
 * Includes validation for reaction intensity and family context.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReactionRequest {
    
    private Reaction.ReactionType reactionType;
    
    @Min(value = 1, message = "Intensity must be at least 1")
    @Max(value = 5, message = "Intensity must be at most 5")
    private Integer intensity;
    
    private String familyContext; // JSON string with family-specific context
    
    private String relationshipContext; // JSON string with relationship context
    
    private String culturalContext; // JSON string with cultural context
    
    private String emotionalContext; // JSON string with emotional context
    
    private Boolean isAnonymous; // Whether the reaction is anonymous
    
    private Boolean isPrivate; // Whether the reaction is private
    
    private String metadata; // Additional metadata for the reaction
}
