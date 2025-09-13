package com.legacykeep.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Share Response DTO
 * 
 * Response DTO for share operations with family context and targeting.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareResponse {
    
    private Long id;
    private UUID contentId;
    private UUID userId;
    private String userName;
    private String userAvatar;
    private String shareType;
    private UUID targetUserId;
    private String targetUserName;
    private UUID targetFamilyId;
    private String targetFamilyName;
    private String shareMessage;
    private String familyContext;
    private Integer generationLevel;
    private String generationName;
    private String relationshipContext;
    private Boolean isAnonymous;
    private Boolean isPrivate;
    private String metadata;
    private LocalDateTime createdAt;
    
    // Family context fields
    private String familyMemberName;
    private String relationshipToUser;
    private Boolean isFromSameGeneration;
    private Boolean isFromSameFamily;
}
