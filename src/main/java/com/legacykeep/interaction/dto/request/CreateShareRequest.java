package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Share Request DTO
 * 
 * Request DTO for creating a new share for family legacy content.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShareRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @NotBlank(message = "Share type is required")
    private String shareType; // SHARE, REPOST, FORWARD
    
    private UUID targetUserId;
    
    private UUID targetFamilyId;
    
    @Size(max = 500, message = "Share message must not exceed 500 characters")
    private String shareMessage;
    
    private String familyContext;
    
    private Integer generationLevel;
    
    private String relationshipContext;
    
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private String metadata;
}
