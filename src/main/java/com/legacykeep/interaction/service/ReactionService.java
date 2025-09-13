package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateReactionRequest;
import com.legacykeep.interaction.dto.request.UpdateReactionRequest;
import com.legacykeep.interaction.dto.response.ReactionResponse;
import com.legacykeep.interaction.dto.response.ReactionSummaryResponse;
import com.legacykeep.interaction.entity.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Reaction Service Interface
 * 
 * Defines the contract for reaction operations in the family legacy system.
 * Supports multiple reaction types with intensity levels and family context.
 * Designed to capture the emotional response of family members to legacy content.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface ReactionService {
    
    // =============================================================================
    // Reaction CRUD Operations
    // =============================================================================
    
    /**
     * Add a reaction to legacy content.
     * 
     * @param request Reaction creation request
     * @param userId User ID adding the reaction
     * @return Created reaction response
     */
    ReactionResponse addReaction(CreateReactionRequest request, UUID userId);
    
    /**
     * Update an existing reaction.
     * 
     * @param reactionId Reaction ID to update
     * @param request Update request
     * @param userId User ID updating the reaction
     * @return Updated reaction response
     */
    ReactionResponse updateReaction(Long reactionId, UpdateReactionRequest request, UUID userId);
    
    /**
     * Remove a reaction from content.
     * 
     * @param reactionId Reaction ID to remove
     * @param userId User ID removing the reaction
     */
    void removeReaction(Long reactionId, UUID userId);
    
    /**
     * Get reactions for specific content.
     * 
     * @param contentId Content ID to get reactions for
     * @param pageable Pagination information
     * @param userId User ID requesting reactions
     * @return Page of reaction responses
     */
    Page<ReactionResponse> getReactionsForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get a specific reaction by ID.
     * 
     * @param reactionId Reaction ID
     * @param userId User ID requesting the reaction
     * @return Reaction response
     */
    ReactionResponse getReactionById(Long reactionId, UUID userId);
    
    // =============================================================================
    // Reaction Summary and Analytics
    // =============================================================================
    
    /**
     * Get reaction summary for content.
     * 
     * @param contentId Content ID
     * @param userId User ID requesting summary
     * @return Reaction summary response
     */
    ReactionSummaryResponse getReactionSummary(UUID contentId, UUID userId);
    
    /**
     * Get reaction breakdown by type for content.
     * 
     * @param contentId Content ID
     * @param userId User ID requesting breakdown
     * @return Reaction breakdown
     */
    ReactionBreakdown getReactionBreakdown(UUID contentId, UUID userId);
    
    /**
     * Get user's reaction to content.
     * 
     * @param contentId Content ID
     * @param userId User ID
     * @return User's reaction response
     */
    ReactionResponse getUserReaction(UUID contentId, UUID userId);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get reactions by family member.
     * 
     * @param familyMemberId Family member ID
     * @param pageable Pagination information
     * @param userId User ID requesting reactions
     * @return Page of reaction responses
     */
    Page<ReactionResponse> getReactionsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get reactions by generation level.
     * 
     * @param generationLevel Generation level
     * @param pageable Pagination information
     * @param userId User ID requesting reactions
     * @return Page of reaction responses
     */
    Page<ReactionResponse> getReactionsByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    /**
     * Get reactions by cultural context.
     * 
     * @param culturalTag Cultural tag
     * @param pageable Pagination information
     * @param userId User ID requesting reactions
     * @return Page of reaction responses
     */
    Page<ReactionResponse> getReactionsByCulturalContext(String culturalTag, Pageable pageable, UUID userId);
    
    /**
     * Get family reaction activity summary.
     * 
     * @param familyId Family ID
     * @param userId User ID requesting summary
     * @return Family reaction activity summary
     */
    FamilyReactionActivity getFamilyReactionActivity(UUID familyId, UUID userId);
    
    // =============================================================================
    // Reaction Type Operations
    // =============================================================================
    
    /**
     * Get available reaction types for user.
     * 
     * @param userId User ID
     * @return List of available reaction types
     */
    List<ReactionTypeInfo> getAvailableReactionTypes(UUID userId);
    
    /**
     * Get family-specific reaction types.
     * 
     * @param userId User ID
     * @return List of family-specific reaction types
     */
    List<ReactionTypeInfo> getFamilyReactionTypes(UUID userId);
    
    /**
     * Get generational reaction types.
     * 
     * @param userId User ID
     * @return List of generational reaction types
     */
    List<ReactionTypeInfo> getGenerationalReactionTypes(UUID userId);
    
    /**
     * Get cultural reaction types.
     * 
     * @param userId User ID
     * @return List of cultural reaction types
     */
    List<ReactionTypeInfo> getCulturalReactionTypes(UUID userId);
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    /**
     * Get reaction statistics for content.
     * 
     * @param contentId Content ID
     * @param userId User ID requesting statistics
     * @return Reaction statistics
     */
    ReactionStatistics getReactionStatistics(UUID contentId, UUID userId);
    
    /**
     * Get trending reactions in family.
     * 
     * @param familyId Family ID
     * @param limit Number of trending reactions to return
     * @param userId User ID requesting trending reactions
     * @return List of trending reactions
     */
    List<TrendingReaction> getTrendingReactions(UUID familyId, int limit, UUID userId);
    
    /**
     * Get reaction intensity analysis.
     * 
     * @param contentId Content ID
     * @param userId User ID requesting analysis
     * @return Reaction intensity analysis
     */
    ReactionIntensityAnalysis getReactionIntensityAnalysis(UUID contentId, UUID userId);
    
    // =============================================================================
    // DTO Classes
    // =============================================================================
    
    /**
     * Reaction Type Information DTO
     */
    class ReactionTypeInfo {
        private final String typeName;
        private final String displayName;
        private final String icon;
        private final String colorCode;
        private final String category;
        private final String description;
        private final boolean isFamilySpecific;
        private final boolean isGenerational;
        private final boolean isCultural;
        
        public ReactionTypeInfo(String typeName, String displayName, String icon, 
                              String colorCode, String category, String description,
                              boolean isFamilySpecific, boolean isGenerational, boolean isCultural) {
            this.typeName = typeName;
            this.displayName = displayName;
            this.icon = icon;
            this.colorCode = colorCode;
            this.category = category;
            this.description = description;
            this.isFamilySpecific = isFamilySpecific;
            this.isGenerational = isGenerational;
            this.isCultural = isCultural;
        }
        
        // Getters
        public String getTypeName() { return typeName; }
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public String getColorCode() { return colorCode; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public boolean isFamilySpecific() { return isFamilySpecific; }
        public boolean isGenerational() { return isGenerational; }
        public boolean isCultural() { return isCultural; }
    }
    
    /**
     * Reaction Breakdown DTO
     */
    class ReactionBreakdown {
        private final Long totalReactions;
        private final List<ReactionTypeCount> reactionTypeCounts;
        private final List<IntensityDistribution> intensityDistribution;
        private final List<GenerationReactionCount> generationReactionCounts;
        
        public ReactionBreakdown(Long totalReactions, List<ReactionTypeCount> reactionTypeCounts,
                               List<IntensityDistribution> intensityDistribution,
                               List<GenerationReactionCount> generationReactionCounts) {
            this.totalReactions = totalReactions;
            this.reactionTypeCounts = reactionTypeCounts;
            this.intensityDistribution = intensityDistribution;
            this.generationReactionCounts = generationReactionCounts;
        }
        
        // Getters
        public Long getTotalReactions() { return totalReactions; }
        public List<ReactionTypeCount> getReactionTypeCounts() { return reactionTypeCounts; }
        public List<IntensityDistribution> getIntensityDistribution() { return intensityDistribution; }
        public List<GenerationReactionCount> getGenerationReactionCounts() { return generationReactionCounts; }
    }
    
    /**
     * Reaction Type Count DTO
     */
    class ReactionTypeCount {
        private final String reactionType;
        private final String displayName;
        private final String icon;
        private final Long count;
        private final Double percentage;
        
        public ReactionTypeCount(String reactionType, String displayName, String icon, 
                               Long count, Double percentage) {
            this.reactionType = reactionType;
            this.displayName = displayName;
            this.icon = icon;
            this.count = count;
            this.percentage = percentage;
        }
        
        // Getters
        public String getReactionType() { return reactionType; }
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public Long getCount() { return count; }
        public Double getPercentage() { return percentage; }
    }
    
    /**
     * Intensity Distribution DTO
     */
    class IntensityDistribution {
        private final Integer intensity;
        private final Long count;
        private final Double percentage;
        
        public IntensityDistribution(Integer intensity, Long count, Double percentage) {
            this.intensity = intensity;
            this.count = count;
            this.percentage = percentage;
        }
        
        // Getters
        public Integer getIntensity() { return intensity; }
        public Long getCount() { return count; }
        public Double getPercentage() { return percentage; }
    }
    
    /**
     * Generation Reaction Count DTO
     */
    class GenerationReactionCount {
        private final Integer generationLevel;
        private final String generationName;
        private final Long reactionCount;
        private final Double percentage;
        
        public GenerationReactionCount(Integer generationLevel, String generationName, 
                                     Long reactionCount, Double percentage) {
            this.generationLevel = generationLevel;
            this.generationName = generationName;
            this.reactionCount = reactionCount;
            this.percentage = percentage;
        }
        
        // Getters
        public Integer getGenerationLevel() { return generationLevel; }
        public String getGenerationName() { return generationName; }
        public Long getReactionCount() { return reactionCount; }
        public Double getPercentage() { return percentage; }
    }
    
    /**
     * Family Reaction Activity DTO
     */
    class FamilyReactionActivity {
        private final Long totalReactions;
        private final Long activeReactors;
        private final List<ReactionTypeCount> topReactionTypes;
        private final List<GenerationReactionCount> generationActivity;
        private final List<CulturalReactionCount> culturalActivity;
        
        public FamilyReactionActivity(Long totalReactions, Long activeReactors,
                                    List<ReactionTypeCount> topReactionTypes,
                                    List<GenerationReactionCount> generationActivity,
                                    List<CulturalReactionCount> culturalActivity) {
            this.totalReactions = totalReactions;
            this.activeReactors = activeReactors;
            this.topReactionTypes = topReactionTypes;
            this.generationActivity = generationActivity;
            this.culturalActivity = culturalActivity;
        }
        
        // Getters
        public Long getTotalReactions() { return totalReactions; }
        public Long getActiveReactors() { return activeReactors; }
        public List<ReactionTypeCount> getTopReactionTypes() { return topReactionTypes; }
        public List<GenerationReactionCount> getGenerationActivity() { return generationActivity; }
        public List<CulturalReactionCount> getCulturalActivity() { return culturalActivity; }
    }
    
    /**
     * Cultural Reaction Count DTO
     */
    class CulturalReactionCount {
        private final String culturalTag;
        private final String displayName;
        private final Long reactionCount;
        private final Double percentage;
        
        public CulturalReactionCount(String culturalTag, String displayName, 
                                   Long reactionCount, Double percentage) {
            this.culturalTag = culturalTag;
            this.displayName = displayName;
            this.reactionCount = reactionCount;
            this.percentage = percentage;
        }
        
        // Getters
        public String getCulturalTag() { return culturalTag; }
        public String getDisplayName() { return displayName; }
        public Long getReactionCount() { return reactionCount; }
        public Double getPercentage() { return percentage; }
    }
    
    /**
     * Reaction Statistics DTO
     */
    class ReactionStatistics {
        private final Long totalReactions;
        private final Long uniqueReactors;
        private final Double averageIntensity;
        private final List<ReactionTypeCount> reactionTypeCounts;
        private final List<IntensityDistribution> intensityDistribution;
        
        public ReactionStatistics(Long totalReactions, Long uniqueReactors, Double averageIntensity,
                                List<ReactionTypeCount> reactionTypeCounts,
                                List<IntensityDistribution> intensityDistribution) {
            this.totalReactions = totalReactions;
            this.uniqueReactors = uniqueReactors;
            this.averageIntensity = averageIntensity;
            this.reactionTypeCounts = reactionTypeCounts;
            this.intensityDistribution = intensityDistribution;
        }
        
        // Getters
        public Long getTotalReactions() { return totalReactions; }
        public Long getUniqueReactors() { return uniqueReactors; }
        public Double getAverageIntensity() { return averageIntensity; }
        public List<ReactionTypeCount> getReactionTypeCounts() { return reactionTypeCounts; }
        public List<IntensityDistribution> getIntensityDistribution() { return intensityDistribution; }
    }
    
    /**
     * Trending Reaction DTO
     */
    class TrendingReaction {
        private final String reactionType;
        private final String displayName;
        private final String icon;
        private final Long count;
        private final Double trendPercentage;
        
        public TrendingReaction(String reactionType, String displayName, String icon, 
                              Long count, Double trendPercentage) {
            this.reactionType = reactionType;
            this.displayName = displayName;
            this.icon = icon;
            this.count = count;
            this.trendPercentage = trendPercentage;
        }
        
        // Getters
        public String getReactionType() { return reactionType; }
        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }
        public Long getCount() { return count; }
        public Double getTrendPercentage() { return trendPercentage; }
    }
    
    /**
     * Reaction Intensity Analysis DTO
     */
    class ReactionIntensityAnalysis {
        private final Double averageIntensity;
        private final Integer maxIntensity;
        private final Integer minIntensity;
        private final List<IntensityDistribution> intensityDistribution;
        private final List<ReactionTypeIntensity> reactionTypeIntensities;
        
        public ReactionIntensityAnalysis(Double averageIntensity, Integer maxIntensity, Integer minIntensity,
                                       List<IntensityDistribution> intensityDistribution,
                                       List<ReactionTypeIntensity> reactionTypeIntensities) {
            this.averageIntensity = averageIntensity;
            this.maxIntensity = maxIntensity;
            this.minIntensity = minIntensity;
            this.intensityDistribution = intensityDistribution;
            this.reactionTypeIntensities = reactionTypeIntensities;
        }
        
        // Getters
        public Double getAverageIntensity() { return averageIntensity; }
        public Integer getMaxIntensity() { return maxIntensity; }
        public Integer getMinIntensity() { return minIntensity; }
        public List<IntensityDistribution> getIntensityDistribution() { return intensityDistribution; }
        public List<ReactionTypeIntensity> getReactionTypeIntensities() { return reactionTypeIntensities; }
    }
    
    /**
     * Reaction Type Intensity DTO
     */
    class ReactionTypeIntensity {
        private final String reactionType;
        private final String displayName;
        private final Double averageIntensity;
        private final Integer maxIntensity;
        private final Integer minIntensity;
        
        public ReactionTypeIntensity(String reactionType, String displayName, 
                                   Double averageIntensity, Integer maxIntensity, Integer minIntensity) {
            this.reactionType = reactionType;
            this.displayName = displayName;
            this.averageIntensity = averageIntensity;
            this.maxIntensity = maxIntensity;
            this.minIntensity = minIntensity;
        }
        
        // Getters
        public String getReactionType() { return reactionType; }
        public String getDisplayName() { return displayName; }
        public Double getAverageIntensity() { return averageIntensity; }
        public Integer getMaxIntensity() { return maxIntensity; }
        public Integer getMinIntensity() { return minIntensity; }
    }
}
