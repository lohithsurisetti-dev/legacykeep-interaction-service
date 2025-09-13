package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Rating Request DTO
 * 
 * Request DTO for updating an existing rating.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRatingRequest {
    
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
    
    private Boolean isAnonymous;
    
    private Boolean isPrivate;
    
    private String metadata;
}
