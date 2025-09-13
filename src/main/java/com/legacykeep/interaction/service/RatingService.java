package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateRatingRequest;
import com.legacykeep.interaction.dto.request.UpdateRatingRequest;
import com.legacykeep.interaction.dto.response.RatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Rating Service Interface
 * 
 * Service interface for managing ratings with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface RatingService {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Create a new rating
     */
    RatingResponse createRating(CreateRatingRequest request, UUID userId);
    
    /**
     * Update an existing rating
     */
    RatingResponse updateRating(Long ratingId, UpdateRatingRequest request, UUID userId);
    
    /**
     * Delete a rating
     */
    void deleteRating(Long ratingId, UUID userId);
    
    /**
     * Get rating by ID
     */
    RatingResponse getRatingById(Long ratingId, UUID userId);
    
    /**
     * Get user's rating for content
     */
    RatingResponse getUserRating(UUID contentId, UUID userId);
    
    // =============================================================================
    // Content Rating Operations
    // =============================================================================
    
    /**
     * Get all ratings for content
     */
    Page<RatingResponse> getRatingsForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get user's ratings
     */
    Page<RatingResponse> getUserRatings(UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get ratings by family member
     */
    Page<RatingResponse> getRatingsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get ratings by generation level
     */
    Page<RatingResponse> getRatingsByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    /**
     * Get ratings by cultural context
     */
    Page<RatingResponse> getRatingsByCulturalContext(String culturalTag, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Get rating statistics for content
     */
    RatingStatistics getRatingStatistics(UUID contentId, UUID userId);
    
    /**
     * Get rating summary for content
     */
    RatingSummary getRatingSummary(UUID contentId, UUID userId);
    
    /**
     * Get high-rated content
     */
    Page<RatingResponse> getHighRatedContent(Pageable pageable, UUID userId);
    
    /**
     * Get ratings with text reviews
     */
    Page<RatingResponse> getRatingsWithText(Pageable pageable, UUID userId);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search ratings by text
     */
    Page<RatingResponse> searchRatings(String searchText, Pageable pageable, UUID userId);
    
    /**
     * Get ratings by rating value
     */
    Page<RatingResponse> getRatingsByValue(Integer ratingValue, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Inner Classes for Response Objects
    // =============================================================================
    
    /**
     * Rating Statistics
     */
    class RatingStatistics {
        private Long totalRatings;
        private Double averageRating;
        private Long ratingCount1;
        private Long ratingCount2;
        private Long ratingCount3;
        private Long ratingCount4;
        private Long ratingCount5;
        private Long ratingsWithText;
        private Double textReviewPercentage;
        
        // Constructors, getters, and setters
        public RatingStatistics() {}
        
        public RatingStatistics(Long totalRatings, Double averageRating, Long ratingCount1, Long ratingCount2, 
                               Long ratingCount3, Long ratingCount4, Long ratingCount5, Long ratingsWithText, 
                               Double textReviewPercentage) {
            this.totalRatings = totalRatings;
            this.averageRating = averageRating;
            this.ratingCount1 = ratingCount1;
            this.ratingCount2 = ratingCount2;
            this.ratingCount3 = ratingCount3;
            this.ratingCount4 = ratingCount4;
            this.ratingCount5 = ratingCount5;
            this.ratingsWithText = ratingsWithText;
            this.textReviewPercentage = textReviewPercentage;
        }
        
        // Getters and setters
        public Long getTotalRatings() { return totalRatings; }
        public void setTotalRatings(Long totalRatings) { this.totalRatings = totalRatings; }
        
        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
        
        public Long getRatingCount1() { return ratingCount1; }
        public void setRatingCount1(Long ratingCount1) { this.ratingCount1 = ratingCount1; }
        
        public Long getRatingCount2() { return ratingCount2; }
        public void setRatingCount2(Long ratingCount2) { this.ratingCount2 = ratingCount2; }
        
        public Long getRatingCount3() { return ratingCount3; }
        public void setRatingCount3(Long ratingCount3) { this.ratingCount3 = ratingCount3; }
        
        public Long getRatingCount4() { return ratingCount4; }
        public void setRatingCount4(Long ratingCount4) { this.ratingCount4 = ratingCount4; }
        
        public Long getRatingCount5() { return ratingCount5; }
        public void setRatingCount5(Long ratingCount5) { this.ratingCount5 = ratingCount5; }
        
        public Long getRatingsWithText() { return ratingsWithText; }
        public void setRatingsWithText(Long ratingsWithText) { this.ratingsWithText = ratingsWithText; }
        
        public Double getTextReviewPercentage() { return textReviewPercentage; }
        public void setTextReviewPercentage(Double textReviewPercentage) { this.textReviewPercentage = textReviewPercentage; }
    }
    
    /**
     * Rating Summary
     */
    class RatingSummary {
        private Long totalRatings;
        private Double averageRating;
        private String ratingText;
        private Boolean hasUserRated;
        private Integer userRating;
        
        // Constructors, getters, and setters
        public RatingSummary() {}
        
        public RatingSummary(Long totalRatings, Double averageRating, String ratingText, Boolean hasUserRated, Integer userRating) {
            this.totalRatings = totalRatings;
            this.averageRating = averageRating;
            this.ratingText = ratingText;
            this.hasUserRated = hasUserRated;
            this.userRating = userRating;
        }
        
        // Getters and setters
        public Long getTotalRatings() { return totalRatings; }
        public void setTotalRatings(Long totalRatings) { this.totalRatings = totalRatings; }
        
        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
        
        public String getRatingText() { return ratingText; }
        public void setRatingText(String ratingText) { this.ratingText = ratingText; }
        
        public Boolean getHasUserRated() { return hasUserRated; }
        public void setHasUserRated(Boolean hasUserRated) { this.hasUserRated = hasUserRated; }
        
        public Integer getUserRating() { return userRating; }
        public void setUserRating(Integer userRating) { this.userRating = userRating; }
    }
}
