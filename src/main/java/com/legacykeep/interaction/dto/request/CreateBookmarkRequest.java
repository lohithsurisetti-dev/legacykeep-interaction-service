package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Bookmark Request DTO
 * 
 * Request DTO for creating a new bookmark for family legacy content.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookmarkRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @Size(max = 100, message = "Bookmark name must not exceed 100 characters")
    private String bookmarkName;
    
    @Size(max = 500, message = "Bookmark description must not exceed 500 characters")
    private String bookmarkDescription;
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String relationshipContext;
    
    private String culturalContext;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private String metadata;
}
