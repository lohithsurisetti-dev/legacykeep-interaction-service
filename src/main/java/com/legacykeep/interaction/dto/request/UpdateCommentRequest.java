package com.legacykeep.interaction.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Update Comment Request DTO
 * 
 * Request object for updating existing comments in the family legacy system.
 * Includes validation for family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    
    @NotBlank(message = "Comment text is required")
    @Size(min = 1, max = 2000, message = "Comment text must be between 1 and 2000 characters")
    private String commentText;
    
    private List<String> mentions; // List of mentioned user IDs
    
    private List<String> hashtags; // List of hashtags
    
    private List<String> mediaUrls; // List of media URLs in comment
    
    private String familyContext; // JSON string with family-specific context
    
    private List<String> culturalTags; // List of cultural tags
    
    private String relationshipContext; // JSON string with relationship context
    
    private String languageCode; // Language of the comment (e.g., "en", "hi", "ta")
    
    private Boolean isAnonymous; // Whether the comment is anonymous
    
    private Boolean isPrivate; // Whether the comment is private
    
    private String metadata; // Additional metadata for the comment
    
    private String editReason; // Reason for editing the comment
}
