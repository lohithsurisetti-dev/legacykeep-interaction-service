package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateContentViewRequest;
import com.legacykeep.interaction.dto.response.ContentViewResponse;
import com.legacykeep.interaction.service.ContentViewService;
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

import java.util.UUID;

/**
 * Content View Controller
 * 
 * REST controller for content view tracking operations in the family legacy system.
 * Provides comprehensive view tracking with family context and analytics.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/views")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Content View Tracking", description = "APIs for tracking content views with family context and analytics")
public class ContentViewController {
    
    private final ContentViewService contentViewService;
    
    // =============================================================================
    // Content View CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Create a new content view", description = "Create a new content view tracking record")
    public ResponseEntity<ContentViewResponse> createContentView(
            @Valid @RequestBody CreateContentViewRequest request,
            @Parameter(description = "User ID creating the view") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating content view for content: {} by user: {} - duration: {}s, completion: {}%", 
                request.getContentId(), userId, request.getViewDuration(), request.getViewCompletionPercentage());
        
        ContentViewResponse response = contentViewService.createContentView(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get views for content", description = "Get paginated views for specific content")
    public ResponseEntity<Page<ContentViewResponse>> getViewsForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views for content: {} by user: {}", contentId, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{viewId}")
    @Operation(summary = "Get content view by ID", description = "Get a specific content view by its ID")
    public ResponseEntity<ContentViewResponse> getContentViewById(
            @Parameter(description = "View ID") @PathVariable Long viewId,
            @Parameter(description = "User ID requesting the view") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting content view by ID: {} for user: {}", viewId, userId);
        
        ContentViewResponse response = contentViewService.getContentViewById(viewId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // User View Operations
    // =============================================================================
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's views", description = "Get all views by a specific user")
    public ResponseEntity<Page<ContentViewResponse>> getUserViews(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by user: {} for requesting user: {}", userId, requestingUserId);
        
        Page<ContentViewResponse> response = contentViewService.getUserViews(userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/user/{userId}")
    @Operation(summary = "Get views by content and user", description = "Get views by specific content and user")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByContentAndUser(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by content: {} and user: {} for requesting user: {}", contentId, userId, requestingUserId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByContentAndUser(contentId, userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get views by family member", description = "Get views by a specific family member")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by family member: {} for user: {}", familyMemberId, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get views by generation", description = "Get views by generation level")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by generation: {} for user: {}", generationLevel, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/generation/{generationLevel}")
    @Operation(summary = "Get views by content and generation", description = "Get views by content and generation level")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByContentAndGeneration(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by content: {} and generation: {} for user: {}", contentId, generationLevel, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByContentAndGeneration(contentId, generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get view statistics", description = "Get view statistics for content")
    public ResponseEntity<ContentViewService.ViewStatistics> getViewStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting view statistics for content: {} by user: {}", contentId, userId);
        
        ContentViewService.ViewStatistics response = contentViewService.getViewStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/most-viewed")
    @Operation(summary = "Get most viewed content", description = "Get content with most views")
    public ResponseEntity<Page<ContentViewResponse>> getMostViewedContent(
            @Parameter(description = "User ID requesting most viewed content") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting most viewed content for user: {}", userId);
        
        Page<ContentViewResponse> response = contentViewService.getMostViewedContent(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/highest-completion")
    @Operation(summary = "Get content with highest completion rates", description = "Get content with highest view completion rates")
    public ResponseEntity<Page<ContentViewResponse>> getContentWithHighestCompletionRates(
            @Parameter(description = "User ID requesting highest completion content") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting content with highest completion rates for user: {}", userId);
        
        Page<ContentViewResponse> response = contentViewService.getContentWithHighestCompletionRates(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Time-based Analytics
    // =============================================================================
    
    @GetMapping("/recent")
    @Operation(summary = "Get recent views", description = "Get recent views (last 7 days)")
    public ResponseEntity<Page<ContentViewResponse>> getRecentViews(
            @Parameter(description = "User ID requesting recent views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting recent views for user: {}", userId);
        
        Page<ContentViewResponse> response = contentViewService.getRecentViews(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get views by date range", description = "Get views within a specific date range")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam String startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam String endDate,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by date range: {} to {} for user: {}", startDate, endDate, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByDateRange(startDate, endDate, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/today")
    @Operation(summary = "Get views today", description = "Get views from today")
    public ResponseEntity<Page<ContentViewResponse>> getViewsToday(
            @Parameter(description = "User ID requesting today's views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views today for user: {}", userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsToday(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // View Analysis
    // =============================================================================
    
    @GetMapping("/high-completion")
    @Operation(summary = "Get views with high completion", description = "Get views with high completion rates")
    public ResponseEntity<Page<ContentViewResponse>> getViewsWithHighCompletion(
            @Parameter(description = "Minimum completion percentage") @RequestParam(defaultValue = "80.0") Double minCompletion,
            @Parameter(description = "User ID requesting high completion views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views with high completion: {}% for user: {}", minCompletion, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsWithHighCompletion(minCompletion, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/long-duration")
    @Operation(summary = "Get views with long duration", description = "Get views with long viewing duration")
    public ResponseEntity<Page<ContentViewResponse>> getViewsWithLongDuration(
            @Parameter(description = "Minimum duration in seconds") @RequestParam(defaultValue = "300") Integer minDuration,
            @Parameter(description = "User ID requesting long duration views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views with long duration: {}s for user: {}", minDuration, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsWithLongDuration(minDuration, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/completion-range")
    @Operation(summary = "Get views by completion range", description = "Get views within a completion percentage range")
    public ResponseEntity<Page<ContentViewResponse>> getViewsByCompletionRange(
            @Parameter(description = "Minimum completion percentage") @RequestParam Double minCompletion,
            @Parameter(description = "Maximum completion percentage") @RequestParam Double maxCompletion,
            @Parameter(description = "User ID requesting views") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting views by completion range: {}% to {}% for user: {}", minCompletion, maxCompletion, userId);
        
        Page<ContentViewResponse> response = contentViewService.getViewsByCompletionRange(minCompletion, maxCompletion, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
}
