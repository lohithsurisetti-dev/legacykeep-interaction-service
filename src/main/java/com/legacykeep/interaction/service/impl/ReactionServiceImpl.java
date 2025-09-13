package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateReactionRequest;
import com.legacykeep.interaction.dto.request.UpdateReactionRequest;
import com.legacykeep.interaction.dto.response.ReactionResponse;
import com.legacykeep.interaction.dto.response.ReactionSummaryResponse;
import com.legacykeep.interaction.entity.Reaction;
import com.legacykeep.interaction.repository.ReactionRepository;
import com.legacykeep.interaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Reaction Service Implementation
 * 
 * Implementation of reaction operations in the family legacy system.
 * Provides comprehensive reaction management with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReactionServiceImpl implements ReactionService {
    
    private final ReactionRepository reactionRepository;
    
    // =============================================================================
    // Reaction CRUD Operations
    // =============================================================================
    
    @Override
    public ReactionResponse addReaction(CreateReactionRequest request, UUID userId) {
        log.info("Adding reaction to content: {} by user: {} - type: {}", 
                request.getContentId(), userId, request.getReactionType());
        
        // Validate content exists (would integrate with Legacy Service)
        // validateContentExists(request.getContentId());
        
        // Check if user already has a reaction to this content
        Reaction existingReaction = reactionRepository.findByContentIdAndUserId(request.getContentId(), userId);
        if (existingReaction != null) {
            // Update existing reaction
            existingReaction.setReactionType(request.getReactionType());
            existingReaction.setIntensity(request.getIntensity());
            existingReaction.setFamilyContext(request.getFamilyContext());
            existingReaction.setRelationshipContext(request.getRelationshipContext());
            existingReaction.setCulturalContext(request.getCulturalContext());
            existingReaction.setEmotionalContext(request.getEmotionalContext());
            existingReaction.setIsAnonymous(request.getIsAnonymous());
            existingReaction.setIsPrivate(request.getIsPrivate());
            existingReaction.setMetadata(request.getMetadata());
            
            Reaction updatedReaction = reactionRepository.save(existingReaction);
            log.info("Reaction updated successfully: {}", updatedReaction.getId());
            
            return convertToReactionResponse(updatedReaction, userId);
        }
        
        // Create new reaction
        Reaction reaction = Reaction.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .reactionType(request.getReactionType())
                .intensity(request.getIntensity())
                .familyContext(request.getFamilyContext())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .culturalContext(request.getCulturalContext())
                .emotionalContext(request.getEmotionalContext())
                .isAnonymous(request.getIsAnonymous())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .build();
        
        Reaction savedReaction = reactionRepository.save(reaction);
        
        log.info("Reaction added successfully: {}", savedReaction.getId());
        
        return convertToReactionResponse(savedReaction, userId);
    }
    
    @Override
    public ReactionResponse updateReaction(Long reactionId, UpdateReactionRequest request, UUID userId) {
        log.info("Updating reaction: {} by user: {}", reactionId, userId);
        
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new IllegalArgumentException("Reaction not found"));
        
        // Check if user owns the reaction
        if (!reaction.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to update this reaction");
        }
        
        // Update reaction fields
        if (request.getReactionType() != null) {
            reaction.setReactionType(request.getReactionType());
        }
        if (request.getIntensity() != null) {
            reaction.setIntensity(request.getIntensity());
        }
        if (request.getFamilyContext() != null) {
            reaction.setFamilyContext(request.getFamilyContext());
        }
        if (request.getRelationshipContext() != null) {
            reaction.setRelationshipContext(request.getRelationshipContext());
        }
        if (request.getCulturalContext() != null) {
            reaction.setCulturalContext(request.getCulturalContext());
        }
        if (request.getEmotionalContext() != null) {
            reaction.setEmotionalContext(request.getEmotionalContext());
        }
        if (request.getIsAnonymous() != null) {
            reaction.setIsAnonymous(request.getIsAnonymous());
        }
        if (request.getIsPrivate() != null) {
            reaction.setIsPrivate(request.getIsPrivate());
        }
        if (request.getMetadata() != null) {
            reaction.setMetadata(request.getMetadata());
        }
        
        Reaction updatedReaction = reactionRepository.save(reaction);
        
        log.info("Reaction updated successfully: {}", reactionId);
        
        return convertToReactionResponse(updatedReaction, userId);
    }
    
    @Override
    public void removeReaction(Long reactionId, UUID userId) {
        log.info("Removing reaction: {} by user: {}", reactionId, userId);
        
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new IllegalArgumentException("Reaction not found"));
        
        // Check if user owns the reaction
        if (!reaction.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to remove this reaction");
        }
        
        reactionRepository.delete(reaction);
        
        log.info("Reaction removed successfully: {}", reactionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ReactionResponse> getReactionsForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting reactions for content: {} by user: {}", contentId, userId);
        
        Page<Reaction> reactions = reactionRepository.findByContentId(contentId, pageable);
        
        return reactions.map(reaction -> convertToReactionResponse(reaction, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReactionResponse getReactionById(Long reactionId, UUID userId) {
        log.info("Getting reaction by ID: {} for user: {}", reactionId, userId);
        
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new IllegalArgumentException("Reaction not found"));
        
        return convertToReactionResponse(reaction, userId);
    }
    
    // =============================================================================
    // Reaction Summary and Analytics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public ReactionSummaryResponse getReactionSummary(UUID contentId, UUID userId) {
        log.info("Getting reaction summary for content: {} by user: {}", contentId, userId);
        
        Long totalReactions = reactionRepository.countByContentId(contentId);
        Long uniqueReactors = reactionRepository.countUniqueReactorsByContentId(contentId);
        Double averageIntensity = reactionRepository.getAverageIntensityByContentId(contentId);
        
        // Get reaction breakdown
        List<Object[]> reactionBreakdown = reactionRepository.getReactionBreakdownByContentId(contentId);
        List<ReactionSummaryResponse.ReactionTypeCount> reactionTypeCounts = reactionBreakdown.stream()
                .map(data -> ReactionSummaryResponse.ReactionTypeCount.builder()
                        .reactionType(((Reaction.ReactionType) data[0]).getTypeName())
                        .displayName(((Reaction.ReactionType) data[0]).getDisplayName())
                        .icon(((Reaction.ReactionType) data[0]).getIcon())
                        .colorCode(((Reaction.ReactionType) data[0]).getColorCode())
                        .category(((Reaction.ReactionType) data[0]).getCategory())
                        .count((Long) data[1])
                        .percentage(calculatePercentage((Long) data[1], totalReactions))
                        .isFamilySpecific(((Reaction.ReactionType) data[0]).isFamilySpecific())
                        .isGenerational(((Reaction.ReactionType) data[0]).isGenerational())
                        .isCultural(((Reaction.ReactionType) data[0]).isCultural())
                        .isCore(((Reaction.ReactionType) data[0]).isCore())
                        .build())
                .collect(Collectors.toList());
        
        // Get intensity distribution
        List<Object[]> intensityData = reactionRepository.getIntensityDistributionByContentId(contentId);
        List<ReactionSummaryResponse.IntensityDistribution> intensityDistribution = intensityData.stream()
                .map(data -> ReactionSummaryResponse.IntensityDistribution.builder()
                        .intensity((Integer) data[0])
                        .description(getIntensityDescription((Integer) data[0]))
                        .count((Long) data[1])
                        .percentage(calculatePercentage((Long) data[1], totalReactions))
                        .build())
                .collect(Collectors.toList());
        
        // Get generation breakdown
        List<Object[]> generationData = reactionRepository.getGenerationReactionBreakdownByContentId(contentId);
        List<ReactionSummaryResponse.GenerationReactionCount> generationReactionCounts = generationData.stream()
                .map(data -> ReactionSummaryResponse.GenerationReactionCount.builder()
                        .generationLevel((Integer) data[0])
                        .generationName("Generation " + data[0])
                        .displayName("Generation " + data[0])
                        .reactionCount((Long) data[1])
                        .percentage(calculatePercentage((Long) data[1], totalReactions))
                        .topReactionTypes(List.of()) // Would calculate from data
                        .build())
                .collect(Collectors.toList());
        
        // Get cultural breakdown
        List<Object[]> culturalData = reactionRepository.getCulturalReactionBreakdownByContentId(contentId);
        List<ReactionSummaryResponse.CulturalReactionCount> culturalReactionCounts = culturalData.stream()
                .map(data -> ReactionSummaryResponse.CulturalReactionCount.builder()
                        .culturalTag((String) data[0])
                        .displayName((String) data[0])
                        .description("Cultural context: " + data[0])
                        .reactionCount((Long) data[1])
                        .percentage(calculatePercentage((Long) data[1], totalReactions))
                        .topReactionTypes(List.of()) // Would calculate from data
                        .build())
                .collect(Collectors.toList());
        
        // Get user's reaction
        Reaction userReaction = reactionRepository.getUserReactionToContent(contentId, userId);
        
        return ReactionSummaryResponse.builder()
                .contentId(contentId)
                .totalReactions(totalReactions)
                .uniqueReactors(uniqueReactors)
                .averageIntensity(averageIntensity)
                .reactionTypeCounts(reactionTypeCounts)
                .intensityDistribution(intensityDistribution)
                .generationReactionCounts(generationReactionCounts)
                .culturalReactionCounts(culturalReactionCounts)
                .familyReactionCounts(List.of()) // Would calculate from data
                .breakdown(createReactionBreakdown(reactionTypeCounts, totalReactions))
                .familyContext(createFamilyContext(contentId, totalReactions))
                .culturalContext(createCulturalContext(culturalReactionCounts))
                .emotionalContext(createEmotionalContext(reactionTypeCounts))
                .userId(userId)
                .hasUserReacted(userReaction != null)
                .userReactionType(userReaction != null ? userReaction.getReactionType().getTypeName() : null)
                .userReactionIntensity(userReaction != null ? userReaction.getIntensity() : null)
                .userReactionCreatedAt(userReaction != null ? userReaction.getCreatedAt() : null)
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReactionBreakdown getReactionBreakdown(UUID contentId, UUID userId) {
        log.info("Getting reaction breakdown for content: {} by user: {}", contentId, userId);
        
        Long totalReactions = reactionRepository.countByContentId(contentId);
        
        // Get reaction type counts
        List<Object[]> reactionData = reactionRepository.getReactionBreakdownByContentId(contentId);
        List<ReactionTypeCount> reactionTypeCounts = reactionData.stream()
                .map(data -> new ReactionTypeCount(
                        ((Reaction.ReactionType) data[0]).getTypeName(),
                        ((Reaction.ReactionType) data[0]).getDisplayName(),
                        ((Reaction.ReactionType) data[0]).getIcon(),
                        (Long) data[1],
                        calculatePercentage((Long) data[1], totalReactions)
                ))
                .collect(Collectors.toList());
        
        // Get intensity distribution
        List<Object[]> intensityData = reactionRepository.getIntensityDistributionByContentId(contentId);
        List<IntensityDistribution> intensityDistribution = intensityData.stream()
                .map(data -> new IntensityDistribution(
                        (Integer) data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], totalReactions)
                ))
                .collect(Collectors.toList());
        
        // Get generation breakdown
        List<Object[]> generationData = reactionRepository.getGenerationReactionBreakdownByContentId(contentId);
        List<GenerationReactionCount> generationReactionCounts = generationData.stream()
                .map(data -> new GenerationReactionCount(
                        (Integer) data[0],
                        "Generation " + data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], totalReactions)
                ))
                .collect(Collectors.toList());
        
        return new ReactionBreakdown(totalReactions, reactionTypeCounts, intensityDistribution, generationReactionCounts);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReactionResponse getUserReaction(UUID contentId, UUID userId) {
        log.info("Getting user reaction for content: {} by user: {}", contentId, userId);
        
        Reaction reaction = reactionRepository.getUserReactionToContent(contentId, userId);
        
        if (reaction == null) {
            return null;
        }
        
        return convertToReactionResponse(reaction, userId);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ReactionResponse> getReactionsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting reactions by family member: {} for user: {}", familyMemberId, userId);
        
        Page<Reaction> reactions = reactionRepository.findByUserId(familyMemberId, pageable);
        
        return reactions.map(reaction -> convertToReactionResponse(reaction, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ReactionResponse> getReactionsByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting reactions by generation: {} for user: {}", generationLevel, userId);
        
        Page<Reaction> reactions = reactionRepository.findByGenerationLevel(generationLevel, pageable);
        
        return reactions.map(reaction -> convertToReactionResponse(reaction, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ReactionResponse> getReactionsByCulturalContext(String culturalTag, Pageable pageable, UUID userId) {
        log.info("Getting reactions by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<Reaction> reactions = reactionRepository.findByCulturalContext(culturalTag, pageable);
        
        return reactions.map(reaction -> convertToReactionResponse(reaction, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public FamilyReactionActivity getFamilyReactionActivity(UUID familyId, UUID userId) {
        log.info("Getting family reaction activity for family: {} by user: {}", familyId, userId);
        
        // Get top reaction types
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Object[]> topReactionData = reactionRepository.getTopReactionTypes(thirtyDaysAgo);
        List<ReactionTypeCount> topReactionTypes = topReactionData.stream()
                .map(data -> new ReactionTypeCount(
                        ((Reaction.ReactionType) data[0]).getTypeName(),
                        ((Reaction.ReactionType) data[0]).getDisplayName(),
                        ((Reaction.ReactionType) data[0]).getIcon(),
                        (Long) data[1],
                        calculatePercentage((Long) data[1], 0L) // Would calculate total
                ))
                .collect(Collectors.toList());
        
        // Get generation activity
        List<Object[]> generationData = reactionRepository.getFamilyReactionActivityByGeneration(thirtyDaysAgo);
        List<GenerationReactionCount> generationActivity = generationData.stream()
                .map(data -> new GenerationReactionCount(
                        (Integer) data[0],
                        "Generation " + data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], 0L) // Would calculate total
                ))
                .collect(Collectors.toList());
        
        // Get cultural activity
        List<Object[]> culturalData = reactionRepository.getFamilyReactionActivityByCulturalContext(thirtyDaysAgo);
        List<CulturalReactionCount> culturalActivity = culturalData.stream()
                .map(data -> new CulturalReactionCount(
                        (String) data[0],
                        (String) data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], 0L) // Would calculate total
                ))
                .collect(Collectors.toList());
        
        return new FamilyReactionActivity(
                0L, // totalReactions - would calculate from data
                0L, // activeReactors - would integrate with user service
                topReactionTypes,
                generationActivity,
                culturalActivity
        );
    }
    
    // =============================================================================
    // Reaction Type Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public List<ReactionTypeInfo> getAvailableReactionTypes(UUID userId) {
        log.info("Getting available reaction types for user: {}", userId);
        
        return List.of(
                new ReactionTypeInfo("LIKE", "Like", "👍", "#4CAF50", "CORE", "Simple appreciation", false, false, false),
                new ReactionTypeInfo("LOVE", "Love", "❤️", "#E91E63", "CORE", "Deep emotional connection", false, false, false),
                new ReactionTypeInfo("BLESSING", "Blessing", "🙏", "#8BC34A", "FAMILY", "Traditional family blessing", true, false, false),
                new ReactionTypeInfo("PRIDE", "Pride", "🏆", "#FFC107", "FAMILY", "Family pride and honor", true, false, false),
                new ReactionTypeInfo("GRANDPARENT", "Grandparent", "👴", "#9E9E9E", "GENERATIONAL", "Special reaction for grandparent content", false, true, false),
                new ReactionTypeInfo("NAMASTE", "Namaste", "🙏", "#4CAF50", "CULTURAL", "Traditional greeting and respect", false, false, true)
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReactionTypeInfo> getFamilyReactionTypes(UUID userId) {
        log.info("Getting family reaction types for user: {}", userId);
        
        return getAvailableReactionTypes(userId).stream()
                .filter(ReactionTypeInfo::isFamilySpecific)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReactionTypeInfo> getGenerationalReactionTypes(UUID userId) {
        log.info("Getting generational reaction types for user: {}", userId);
        
        return getAvailableReactionTypes(userId).stream()
                .filter(ReactionTypeInfo::isGenerational)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReactionTypeInfo> getCulturalReactionTypes(UUID userId) {
        log.info("Getting cultural reaction types for user: {}", userId);
        
        return getAvailableReactionTypes(userId).stream()
                .filter(ReactionTypeInfo::isCultural)
                .collect(Collectors.toList());
    }
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public ReactionStatistics getReactionStatistics(UUID contentId, UUID userId) {
        log.info("Getting reaction statistics for content: {} by user: {}", contentId, userId);
        
        Long totalReactions = reactionRepository.countByContentId(contentId);
        Long uniqueReactors = reactionRepository.countUniqueReactorsByContentId(contentId);
        Double averageIntensity = reactionRepository.getAverageIntensityByContentId(contentId);
        
        // Get reaction type counts
        List<Object[]> reactionData = reactionRepository.getReactionBreakdownByContentId(contentId);
        List<ReactionTypeCount> reactionTypeCounts = reactionData.stream()
                .map(data -> new ReactionTypeCount(
                        ((Reaction.ReactionType) data[0]).getTypeName(),
                        ((Reaction.ReactionType) data[0]).getDisplayName(),
                        ((Reaction.ReactionType) data[0]).getIcon(),
                        (Long) data[1],
                        calculatePercentage((Long) data[1], totalReactions)
                ))
                .collect(Collectors.toList());
        
        // Get intensity distribution
        List<Object[]> intensityData = reactionRepository.getIntensityDistributionByContentId(contentId);
        List<IntensityDistribution> intensityDistribution = intensityData.stream()
                .map(data -> new IntensityDistribution(
                        (Integer) data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], totalReactions)
                ))
                .collect(Collectors.toList());
        
        return new ReactionStatistics(totalReactions, uniqueReactors, averageIntensity, reactionTypeCounts, intensityDistribution);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TrendingReaction> getTrendingReactions(UUID familyId, int limit, UUID userId) {
        log.info("Getting trending reactions for family: {} by user: {}", familyId, userId);
        
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Object[]> trendingData = reactionRepository.getTrendingReactions(sevenDaysAgo);
        
        return trendingData.stream()
                .limit(limit)
                .map(data -> new TrendingReaction(
                        ((Reaction.ReactionType) data[0]).getTypeName(),
                        ((Reaction.ReactionType) data[0]).getDisplayName(),
                        ((Reaction.ReactionType) data[0]).getIcon(),
                        (Long) data[1],
                        0.0 // trendPercentage - would calculate from historical data
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReactionIntensityAnalysis getReactionIntensityAnalysis(UUID contentId, UUID userId) {
        log.info("Getting reaction intensity analysis for content: {} by user: {}", contentId, userId);
        
        Object[] intensityStats = reactionRepository.getReactionIntensityStatistics(contentId);
        
        Double averageIntensity = (Double) intensityStats[2];
        Integer minIntensity = (Integer) intensityStats[0];
        Integer maxIntensity = (Integer) intensityStats[1];
        
        // Get intensity distribution
        List<Object[]> intensityData = reactionRepository.getIntensityDistributionByContentId(contentId);
        List<IntensityDistribution> intensityDistribution = intensityData.stream()
                .map(data -> new IntensityDistribution(
                        (Integer) data[0],
                        (Long) data[1],
                        calculatePercentage((Long) data[1], 0L) // Would calculate total
                ))
                .collect(Collectors.toList());
        
        // Get reaction type intensities
        List<ReactionTypeIntensity> reactionTypeIntensities = List.of(); // Would calculate from data
        
        return new ReactionIntensityAnalysis(averageIntensity, maxIntensity, minIntensity, intensityDistribution, reactionTypeIntensities);
    }
    
    // =============================================================================
    // Private Helper Methods
    // =============================================================================
    
    private ReactionResponse convertToReactionResponse(Reaction reaction, UUID userId) {
        return ReactionResponse.builder()
                .id(reaction.getId())
                .contentId(reaction.getContentId())
                .userId(reaction.getUserId())
                .userName("User Name") // Would integrate with User Service
                .userAvatar("User Avatar") // Would integrate with User Service
                .reactionType(reaction.getReactionType())
                .reactionTypeName(reaction.getReactionType().getDisplayName())
                .reactionIcon(reaction.getReactionType().getIcon())
                .reactionColor(reaction.getReactionType().getColorCode())
                .reactionCategory(reaction.getReactionType().getCategory())
                .intensity(reaction.getIntensity())
                .intensityDescription(getIntensityDescription(reaction.getIntensity()))
                .familyContext(reaction.getFamilyContext())
                .generationLevel(reaction.getGenerationLevel())
                .generationName("Generation " + reaction.getGenerationLevel())
                .relationshipContext(reaction.getRelationshipContext())
                .culturalContext(reaction.getCulturalContext())
                .emotionalContext(reaction.getEmotionalContext())
                .isAnonymous(reaction.getIsAnonymous())
                .isPrivate(reaction.getIsPrivate())
                .metadata(reaction.getMetadata())
                .createdAt(reaction.getCreatedAt())
                .familyMemberName("Family Member") // Would integrate with User Service
                .relationshipToUser("Relationship") // Would integrate with Relationship Service
                .isFromSameGeneration(false) // Would calculate based on user data
                .isFromSameFamily(false) // Would calculate based on family data
                .culturalDisplayName("Cultural Context") // Would process cultural context
                .emotionalDisplayName("Emotional Context") // Would process emotional context
                .isFamilySpecific(reaction.getReactionType().isFamilySpecific())
                .isGenerational(reaction.getReactionType().isGenerational())
                .isCultural(reaction.getReactionType().isCultural())
                .isCore(reaction.getReactionType().isCore())
                .isHighIntensity(reaction.getIntensity() >= 4)
                .isLowIntensity(reaction.getIntensity() <= 2)
                .intensityLevel(getIntensityLevel(reaction.getIntensity()))
                .build();
    }
    
    private String getIntensityDescription(Integer intensity) {
        return switch (intensity) {
            case 1 -> "Very Low";
            case 2 -> "Low";
            case 3 -> "Medium";
            case 4 -> "High";
            case 5 -> "Very High";
            default -> "Unknown";
        };
    }
    
    private String getIntensityLevel(Integer intensity) {
        return switch (intensity) {
            case 1, 2 -> "LOW";
            case 3 -> "MEDIUM";
            case 4, 5 -> "HIGH";
            default -> "UNKNOWN";
        };
    }
    
    private Double calculatePercentage(Long count, Long total) {
        if (total == null || total == 0) {
            return 0.0;
        }
        return (count.doubleValue() / total.doubleValue()) * 100.0;
    }
    
    private ReactionSummaryResponse.ReactionBreakdown createReactionBreakdown(
            List<ReactionSummaryResponse.ReactionTypeCount> reactionTypeCounts, Long totalReactions) {
        
        Long familyReactions = reactionTypeCounts.stream()
                .filter(rtc -> rtc.getIsFamilySpecific() != null && rtc.getIsFamilySpecific())
                .mapToLong(ReactionSummaryResponse.ReactionTypeCount::getCount)
                .sum();
        
        Long generationalReactions = reactionTypeCounts.stream()
                .filter(rtc -> rtc.getIsGenerational() != null && rtc.getIsGenerational())
                .mapToLong(ReactionSummaryResponse.ReactionTypeCount::getCount)
                .sum();
        
        Long culturalReactions = reactionTypeCounts.stream()
                .filter(rtc -> rtc.getIsCultural() != null && rtc.getIsCultural())
                .mapToLong(ReactionSummaryResponse.ReactionTypeCount::getCount)
                .sum();
        
        Long coreReactions = reactionTypeCounts.stream()
                .filter(rtc -> rtc.getIsCore() != null && rtc.getIsCore())
                .mapToLong(ReactionSummaryResponse.ReactionTypeCount::getCount)
                .sum();
        
        return ReactionSummaryResponse.ReactionBreakdown.builder()
                .totalReactions(totalReactions)
                .coreReactions(coreReactions)
                .familyReactions(familyReactions)
                .generationalReactions(generationalReactions)
                .culturalReactions(culturalReactions)
                .familyReactionPercentage(calculatePercentage(familyReactions, totalReactions))
                .generationalReactionPercentage(calculatePercentage(generationalReactions, totalReactions))
                .culturalReactionPercentage(calculatePercentage(culturalReactions, totalReactions))
                .build();
    }
    
    private ReactionSummaryResponse.FamilyContext createFamilyContext(UUID contentId, Long totalReactions) {
        return ReactionSummaryResponse.FamilyContext.builder()
                .familyId(UUID.randomUUID()) // Would get from content
                .familyName("Family Name") // Would get from content
                .totalFamilyMembers(0L) // Would integrate with User Service
                .activeReactors(0L) // Would calculate from data
                .familyEngagementRate(0.0) // Would calculate from data
                .generationActivity(List.of()) // Would calculate from data
                .topFamilyReactions(List.of()) // Would calculate from data
                .build();
    }
    
    private ReactionSummaryResponse.CulturalContext createCulturalContext(
            List<ReactionSummaryResponse.CulturalReactionCount> culturalReactionCounts) {
        
        return ReactionSummaryResponse.CulturalContext.builder()
                .culturalActivity(culturalReactionCounts)
                .topCulturalReactions(List.of()) // Would calculate from data
                .dominantCulturalTheme("Unknown") // Would calculate from data
                .culturalEngagementRate(0.0) // Would calculate from data
                .build();
    }
    
    private ReactionSummaryResponse.EmotionalContext createEmotionalContext(
            List<ReactionSummaryResponse.ReactionTypeCount> reactionTypeCounts) {
        
        return ReactionSummaryResponse.EmotionalContext.builder()
                .averageSentiment(0.0) // Would calculate from sentiment analysis
                .dominantEmotion("Neutral") // Would calculate from data
                .emotionalReactions(reactionTypeCounts)
                .positiveReactionPercentage(0.0) // Would calculate from data
                .negativeReactionPercentage(0.0) // Would calculate from data
                .neutralReactionPercentage(0.0) // Would calculate from data
                .build();
    }
}
