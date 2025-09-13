package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Bookmark Request DTO
 * 
 * Request DTO for updating an existing bookmark.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookmarkRequest {
    
    @Size(max = 100, message = "Bookmark name must not exceed 100 characters")
    private String bookmarkName;
    
    @Size(max = 500, message = "Bookmark description must not exceed 500 characters")
    private String bookmarkDescription;
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String relationshipContext;
    
    private String culturalContext;
    
    private Boolean isPrivate;
    
    private String metadata;
}
