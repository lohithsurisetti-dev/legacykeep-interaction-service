package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Create Comment Request DTO
 * 
 * Request object for creating new comments on family legacy content.
 * Includes validation for family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    
    @NotNull(message = "Content ID is required")
    private UUID contentId;
    
    @NotBlank(message = "Comment text is required")
    @Size(min = 1, max = 2000, message = "Comment text must be between 1 and 2000 characters")
    private String commentText;
    
    private Long parentCommentId; // For replies
    
    private List<String> mentions; // List of mentioned user IDs
    
    private List<String> hashtags; // List of hashtags
    
    private List<String> mediaUrls; // List of media URLs in comment
    
    private String familyContext; // JSON string with family-specific context
    
    private List<String> culturalTags; // List of cultural tags
    
    private Integer generationLevel; // Generation level of the commenter
    
    private String relationshipContext; // JSON string with relationship context
    
    private String languageCode; // Language of the comment (e.g., "en", "hi", "ta")
    
    @Builder.Default
    private Boolean isAnonymous = false;
    
    @Builder.Default
    private Boolean isPrivate = false;
    
    private String metadata; // Additional metadata for the comment
}
