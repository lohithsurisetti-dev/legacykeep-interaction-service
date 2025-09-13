package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Rating Request DTO
 * 
 * Request DTO for creating a new rating for family legacy content.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRatingRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @NotNull(message = "Rating value is required")
    @Min(value = 1, message = "Rating value must be at least 1")
    @Max(value = 5, message = "Rating value must be at most 5")
    private Integer ratingValue;
    
    @Size(max = 1000, message = "Rating text must not exceed 1000 characters")
    private String ratingText;
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String relationshipContext;
    
    private String culturalContext;
    
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private String metadata;
}
