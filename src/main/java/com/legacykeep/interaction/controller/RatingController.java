package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateRatingRequest;
import com.legacykeep.interaction.dto.request.UpdateRatingRequest;
import com.legacykeep.interaction.dto.response.RatingResponse;
import com.legacykeep.interaction.service.RatingService;
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
 * Rating Controller
 * 
 * REST controller for rating operations in the family legacy system.
 * Provides comprehensive rating management with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Rating Management", description = "APIs for managing ratings on family legacy content")
public class RatingController {
    
    private final RatingService ratingService;
    
    // =============================================================================
    // Rating CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Create a new rating", description = "Create a new rating for family legacy content")
    public ResponseEntity<RatingResponse> createRating(
            @Valid @RequestBody CreateRatingRequest request,
            @Parameter(description = "User ID creating the rating") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating rating for content: {} by user: {} - rating: {}", 
                request.getContentId(), userId, request.getRatingValue());
        
        RatingResponse response = ratingService.createRating(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get ratings for content", description = "Get paginated ratings for specific content")
    public ResponseEntity<Page<RatingResponse>> getRatingsForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting ratings") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings for content: {} by user: {}", contentId, userId);
        
        Page<RatingResponse> response = ratingService.getRatingsForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{ratingId}")
    @Operation(summary = "Get rating by ID", description = "Get a specific rating by its ID")
    public ResponseEntity<RatingResponse> getRatingById(
            @Parameter(description = "Rating ID") @PathVariable Long ratingId,
            @Parameter(description = "User ID requesting the rating") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting rating by ID: {} for user: {}", ratingId, userId);
        
        RatingResponse response = ratingService.getRatingById(ratingId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{ratingId}")
    @Operation(summary = "Update rating", description = "Update an existing rating")
    public ResponseEntity<RatingResponse> updateRating(
            @Parameter(description = "Rating ID") @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request,
            @Parameter(description = "User ID updating the rating") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Updating rating: {} by user: {}", ratingId, userId);
        
        RatingResponse response = ratingService.updateRating(ratingId, request, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{ratingId}")
    @Operation(summary = "Delete rating", description = "Delete a rating")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "Rating ID") @PathVariable Long ratingId,
            @Parameter(description = "User ID deleting the rating") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Deleting rating: {} by user: {}", ratingId, userId);
        
        ratingService.deleteRating(ratingId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    // =============================================================================
    // User Rating Operations
    // =============================================================================
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's ratings", description = "Get all ratings by a specific user")
    public ResponseEntity<Page<RatingResponse>> getUserRatings(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings by user: {} for requesting user: {}", userId, requestingUserId);
        
        Page<RatingResponse> response = ratingService.getUserRatings(userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/user")
    @Operation(summary = "Get user's rating for content", description = "Get user's rating for specific content")
    public ResponseEntity<RatingResponse> getUserRating(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting user rating for content: {} by user: {}", contentId, userId);
        
        RatingResponse response = ratingService.getUserRating(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get ratings by family member", description = "Get ratings by a specific family member")
    public ResponseEntity<Page<RatingResponse>> getRatingsByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting ratings") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings by family member: {} for user: {}", familyMemberId, userId);
        
        Page<RatingResponse> response = ratingService.getRatingsByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get ratings by generation", description = "Get ratings by generation level")
    public ResponseEntity<Page<RatingResponse>> getRatingsByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting ratings") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings by generation: {} for user: {}", generationLevel, userId);
        
        Page<RatingResponse> response = ratingService.getRatingsByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cultural/{culturalTag}")
    @Operation(summary = "Get ratings by cultural context", description = "Get ratings by cultural tag")
    public ResponseEntity<Page<RatingResponse>> getRatingsByCulturalContext(
            @Parameter(description = "Cultural tag") @PathVariable String culturalTag,
            @Parameter(description = "User ID requesting ratings") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<RatingResponse> response = ratingService.getRatingsByCulturalContext(culturalTag, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get rating statistics", description = "Get rating statistics for content")
    public ResponseEntity<RatingService.RatingStatistics> getRatingStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting rating statistics for content: {} by user: {}", contentId, userId);
        
        RatingService.RatingStatistics response = ratingService.getRatingStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/summary")
    @Operation(summary = "Get rating summary", description = "Get rating summary for content")
    public ResponseEntity<RatingService.RatingSummary> getRatingSummary(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting summary") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting rating summary for content: {} by user: {}", contentId, userId);
        
        RatingService.RatingSummary response = ratingService.getRatingSummary(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/high-rated")
    @Operation(summary = "Get high-rated content", description = "Get content with high ratings (4+ stars)")
    public ResponseEntity<Page<RatingResponse>> getHighRatedContent(
            @Parameter(description = "User ID requesting high-rated content") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting high-rated content for user: {}", userId);
        
        Page<RatingResponse> response = ratingService.getHighRatedContent(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/with-text")
    @Operation(summary = "Get ratings with text reviews", description = "Get ratings that include text reviews")
    public ResponseEntity<Page<RatingResponse>> getRatingsWithText(
            @Parameter(description = "User ID requesting ratings with text") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings with text for user: {}", userId);
        
        Page<RatingResponse> response = ratingService.getRatingsWithText(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @GetMapping("/search")
    @Operation(summary = "Search ratings", description = "Search ratings by text content")
    public ResponseEntity<Page<RatingResponse>> searchRatings(
            @Parameter(description = "Search text") @RequestParam String searchText,
            @Parameter(description = "User ID performing search") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Searching ratings with text: {} for user: {}", searchText, userId);
        
        Page<RatingResponse> response = ratingService.searchRatings(searchText, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/value/{ratingValue}")
    @Operation(summary = "Get ratings by value", description = "Get ratings by specific rating value")
    public ResponseEntity<Page<RatingResponse>> getRatingsByValue(
            @Parameter(description = "Rating value (1-5)") @PathVariable Integer ratingValue,
            @Parameter(description = "User ID requesting ratings") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting ratings by value: {} for user: {}", ratingValue, userId);
        
        Page<RatingResponse> response = ratingService.getRatingsByValue(ratingValue, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
}
