package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Content View Request DTO
 * 
 * Request DTO for creating a new content view tracking record.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContentViewRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @Min(value = 0, message = "View duration must be non-negative")
    private Integer viewDuration; // in seconds
    
    @Min(value = 0, message = "View completion percentage must be non-negative")
    @Max(value = 100, message = "View completion percentage cannot exceed 100")
    private Double viewCompletionPercentage;
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String relationshipContext;
    
    @Builder.Default
    private Boolean isAnonymous = false;
    
    private String metadata;
}
