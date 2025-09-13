package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateReactionRequest;
import com.legacykeep.interaction.dto.request.UpdateReactionRequest;
import com.legacykeep.interaction.dto.response.ReactionResponse;
import com.legacykeep.interaction.dto.response.ReactionSummaryResponse;
import com.legacykeep.interaction.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Reaction Controller
 * 
 * REST controller for reaction operations in the family legacy system.
 * Provides comprehensive reaction management with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reaction Management", description = "APIs for managing reactions to family legacy content")
public class ReactionController {
    
    private final ReactionService reactionService;
    
    // =============================================================================
    // Reaction CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Add a reaction", description = "Add a reaction to family legacy content")
    public ResponseEntity<ReactionResponse> addReaction(
            @Valid @RequestBody CreateReactionRequest request,
            @Parameter(description = "User ID adding the reaction") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Adding reaction to content: {} by user: {} - type: {}", 
                request.getContentId(), userId, request.getReactionType());
        
        ReactionResponse response = reactionService.addReaction(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{reactionId}")
    @Operation(summary = "Update reaction", description = "Update an existing reaction")
    public ResponseEntity<ReactionResponse> updateReaction(
            @Parameter(description = "Reaction ID") @PathVariable Long reactionId,
            @Valid @RequestBody UpdateReactionRequest request,
            @Parameter(description = "User ID updating the reaction") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Updating reaction: {} by user: {}", reactionId, userId);
        
        ReactionResponse response = reactionService.updateReaction(reactionId, request, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{reactionId}")
    @Operation(summary = "Remove reaction", description = "Remove a reaction from content")
    public ResponseEntity<Void> removeReaction(
            @Parameter(description = "Reaction ID") @PathVariable Long reactionId,
            @Parameter(description = "User ID removing the reaction") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Removing reaction: {} by user: {}", reactionId, userId);
        
        reactionService.removeReaction(reactionId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get reactions for content", description = "Get paginated reactions for specific content")
    public ResponseEntity<Page<ReactionResponse>> getReactionsForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting reactions") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting reactions for content: {} by user: {}", contentId, userId);
        
        Page<ReactionResponse> response = reactionService.getReactionsForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{reactionId}")
    @Operation(summary = "Get reaction by ID", description = "Get a specific reaction by its ID")
    public ResponseEntity<ReactionResponse> getReactionById(
            @Parameter(description = "Reaction ID") @PathVariable Long reactionId,
            @Parameter(description = "User ID requesting the reaction") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting reaction by ID: {} for user: {}", reactionId, userId);
        
        ReactionResponse response = reactionService.getReactionById(reactionId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Reaction Summary and Analytics
    // =============================================================================
    
    @GetMapping("/content/{contentId}/summary")
    @Operation(summary = "Get reaction summary", description = "Get reaction summary for content")
    public ResponseEntity<ReactionSummaryResponse> getReactionSummary(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting summary") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting reaction summary for content: {} by user: {}", contentId, userId);
        
        ReactionSummaryResponse response = reactionService.getReactionSummary(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/breakdown")
    @Operation(summary = "Get reaction breakdown", description = "Get reaction breakdown by type for content")
    public ResponseEntity<ReactionService.ReactionBreakdown> getReactionBreakdown(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting breakdown") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting reaction breakdown for content: {} by user: {}", contentId, userId);
        
        ReactionService.ReactionBreakdown response = reactionService.getReactionBreakdown(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/user")
    @Operation(summary = "Get user's reaction", description = "Get user's reaction to content")
    public ResponseEntity<ReactionResponse> getUserReaction(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting user reaction for content: {} by user: {}", contentId, userId);
        
        ReactionResponse response = reactionService.getUserReaction(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get reactions by family member", description = "Get reactions by a specific family member")
    public ResponseEntity<Page<ReactionResponse>> getReactionsByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting reactions") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting reactions by family member: {} for user: {}", familyMemberId, userId);
        
        Page<ReactionResponse> response = reactionService.getReactionsByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get reactions by generation", description = "Get reactions by generation level")
    public ResponseEntity<Page<ReactionResponse>> getReactionsByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting reactions") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting reactions by generation: {} for user: {}", generationLevel, userId);
        
        Page<ReactionResponse> response = reactionService.getReactionsByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cultural/{culturalTag}")
    @Operation(summary = "Get reactions by cultural context", description = "Get reactions by cultural tag")
    public ResponseEntity<Page<ReactionResponse>> getReactionsByCulturalContext(
            @Parameter(description = "Cultural tag") @PathVariable String culturalTag,
            @Parameter(description = "User ID requesting reactions") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting reactions by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<ReactionResponse> response = reactionService.getReactionsByCulturalContext(culturalTag, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/family/{familyId}/activity")
    @Operation(summary = "Get family reaction activity", description = "Get family reaction activity summary")
    public ResponseEntity<ReactionService.FamilyReactionActivity> getFamilyReactionActivity(
            @Parameter(description = "Family ID") @PathVariable UUID familyId,
            @Parameter(description = "User ID requesting summary") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting family reaction activity for family: {} by user: {}", familyId, userId);
        
        ReactionService.FamilyReactionActivity response = reactionService.getFamilyReactionActivity(familyId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Reaction Type Operations
    // =============================================================================
    
    @GetMapping("/types")
    @Operation(summary = "Get available reaction types", description = "Get available reaction types for user")
    public ResponseEntity<List<ReactionService.ReactionTypeInfo>> getAvailableReactionTypes(
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting available reaction types for user: {}", userId);
        
        List<ReactionService.ReactionTypeInfo> response = reactionService.getAvailableReactionTypes(userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/types/family")
    @Operation(summary = "Get family reaction types", description = "Get family-specific reaction types")
    public ResponseEntity<List<ReactionService.ReactionTypeInfo>> getFamilyReactionTypes(
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting family reaction types for user: {}", userId);
        
        List<ReactionService.ReactionTypeInfo> response = reactionService.getFamilyReactionTypes(userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/types/generational")
    @Operation(summary = "Get generational reaction types", description = "Get generational reaction types")
    public ResponseEntity<List<ReactionService.ReactionTypeInfo>> getGenerationalReactionTypes(
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting generational reaction types for user: {}", userId);
        
        List<ReactionService.ReactionTypeInfo> response = reactionService.getGenerationalReactionTypes(userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/types/cultural")
    @Operation(summary = "Get cultural reaction types", description = "Get cultural reaction types")
    public ResponseEntity<List<ReactionService.ReactionTypeInfo>> getCulturalReactionTypes(
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting cultural reaction types for user: {}", userId);
        
        List<ReactionService.ReactionTypeInfo> response = reactionService.getCulturalReactionTypes(userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Insights
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get reaction statistics", description = "Get reaction statistics for content")
    public ResponseEntity<ReactionService.ReactionStatistics> getReactionStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting reaction statistics for content: {} by user: {}", contentId, userId);
        
        ReactionService.ReactionStatistics response = reactionService.getReactionStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/family/{familyId}/trending")
    @Operation(summary = "Get trending reactions", description = "Get trending reactions in family")
    public ResponseEntity<List<ReactionService.TrendingReaction>> getTrendingReactions(
            @Parameter(description = "Family ID") @PathVariable UUID familyId,
            @Parameter(description = "Number of trending reactions to return") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "User ID requesting trending reactions") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting trending reactions for family: {} by user: {}", familyId, userId);
        
        List<ReactionService.TrendingReaction> response = reactionService.getTrendingReactions(familyId, limit, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/intensity-analysis")
    @Operation(summary = "Get reaction intensity analysis", description = "Get reaction intensity analysis for content")
    public ResponseEntity<ReactionService.ReactionIntensityAnalysis> getReactionIntensityAnalysis(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting analysis") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting reaction intensity analysis for content: {} by user: {}", contentId, userId);
        
        ReactionService.ReactionIntensityAnalysis response = reactionService.getReactionIntensityAnalysis(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
}
