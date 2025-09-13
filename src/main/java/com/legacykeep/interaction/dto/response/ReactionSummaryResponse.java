package com.legacykeep.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Reaction Summary Response DTO
 * 
 * Response object for reaction summary operations in the family legacy system.
 * Provides aggregated reaction data with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionSummaryResponse {
    
    private UUID contentId;
    
    private Long totalReactions;
    
    private Long uniqueReactors;
    
    private Double averageIntensity;
    
    private List<ReactionTypeCount> reactionTypeCounts;
    
    private List<IntensityDistribution> intensityDistribution;
    
    private List<GenerationReactionCount> generationReactionCounts;
    
    private List<CulturalReactionCount> culturalReactionCounts;
    
    private List<FamilyReactionCount> familyReactionCounts;
    
    private ReactionBreakdown breakdown;
    
    private FamilyContext familyContext;
    
    private CulturalContext culturalContext;
    
    private EmotionalContext emotionalContext;
    
    // User-specific information
    private UUID userId;
    
    private Boolean hasUserReacted;
    
    private String userReactionType;
    
    private Integer userReactionIntensity;
    
    private LocalDateTime userReactionCreatedAt;
    
    // Nested DTOs
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionTypeCount {
        private String reactionType;
        private String displayName;
        private String icon;
        private String colorCode;
        private String category;
        private Long count;
        private Double percentage;
        private Boolean isFamilySpecific;
        private Boolean isGenerational;
        private Boolean isCultural;
        private Boolean isCore;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntensityDistribution {
        private Integer intensity;
        private String description;
        private Long count;
        private Double percentage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerationReactionCount {
        private Integer generationLevel;
        private String generationName;
        private String displayName;
        private Long reactionCount;
        private Double percentage;
        private List<ReactionTypeCount> topReactionTypes;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CulturalReactionCount {
        private String culturalTag;
        private String displayName;
        private String description;
        private Long reactionCount;
        private Double percentage;
        private List<ReactionTypeCount> topReactionTypes;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyReactionCount {
        private UUID familyId;
        private String familyName;
        private Long reactionCount;
        private Double percentage;
        private List<ReactionTypeCount> topReactionTypes;
        private List<GenerationReactionCount> generationBreakdown;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionBreakdown {
        private Long totalReactions;
        private Long coreReactions;
        private Long familyReactions;
        private Long generationalReactions;
        private Long culturalReactions;
        private Double familyReactionPercentage;
        private Double generationalReactionPercentage;
        private Double culturalReactionPercentage;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyContext {
        private UUID familyId;
        private String familyName;
        private Long totalFamilyMembers;
        private Long activeReactors;
        private Double familyEngagementRate;
        private List<GenerationReactionCount> generationActivity;
        private List<ReactionTypeCount> topFamilyReactions;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CulturalContext {
        private List<CulturalReactionCount> culturalActivity;
        private List<ReactionTypeCount> topCulturalReactions;
        private String dominantCulturalTheme;
        private Double culturalEngagementRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionalContext {
        private Double averageSentiment;
        private String dominantEmotion;
        private List<ReactionTypeCount> emotionalReactions;
        private Double positiveReactionPercentage;
        private Double negativeReactionPercentage;
        private Double neutralReactionPercentage;
    }
}
