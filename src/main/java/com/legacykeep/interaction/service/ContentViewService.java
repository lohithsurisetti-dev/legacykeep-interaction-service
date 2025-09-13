package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateContentViewRequest;
import com.legacykeep.interaction.dto.response.ContentViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Content View Service Interface
 * 
 * Service interface for managing content view tracking with family context and analytics.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface ContentViewService {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Create a new content view
     */
    ContentViewResponse createContentView(CreateContentViewRequest request, UUID userId);
    
    /**
     * Get content view by ID
     */
    ContentViewResponse getContentViewById(Long viewId, UUID userId);
    
    // =============================================================================
    // Content View Operations
    // =============================================================================
    
    /**
     * Get all views for content
     */
    Page<ContentViewResponse> getViewsForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get user's views
     */
    Page<ContentViewResponse> getUserViews(UUID userId, Pageable pageable);
    
    /**
     * Get views by content and user
     */
    Page<ContentViewResponse> getViewsByContentAndUser(UUID contentId, UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get views by family member
     */
    Page<ContentViewResponse> getViewsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get views by generation level
     */
    Page<ContentViewResponse> getViewsByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    /**
     * Get views by content and generation
     */
    Page<ContentViewResponse> getViewsByContentAndGeneration(UUID contentId, Integer generationLevel, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Get view statistics for content
     */
    ViewStatistics getViewStatistics(UUID contentId, UUID userId);
    
    /**
     * Get most viewed content
     */
    Page<ContentViewResponse> getMostViewedContent(Pageable pageable, UUID userId);
    
    /**
     * Get content with highest completion rates
     */
    Page<ContentViewResponse> getContentWithHighestCompletionRates(Pageable pageable, UUID userId);
    
    // =============================================================================
    // Time-based Analytics
    // =============================================================================
    
    /**
     * Get recent views
     */
    Page<ContentViewResponse> getRecentViews(Pageable pageable, UUID userId);
    
    /**
     * Get views by date range
     */
    Page<ContentViewResponse> getViewsByDateRange(String startDate, String endDate, Pageable pageable, UUID userId);
    
    /**
     * Get views today
     */
    Page<ContentViewResponse> getViewsToday(Pageable pageable, UUID userId);
    
    // =============================================================================
    // View Analysis
    // =============================================================================
    
    /**
     * Get views with high completion rates
     */
    Page<ContentViewResponse> getViewsWithHighCompletion(Double minCompletion, Pageable pageable, UUID userId);
    
    /**
     * Get views with long duration
     */
    Page<ContentViewResponse> getViewsWithLongDuration(Integer minDuration, Pageable pageable, UUID userId);
    
    /**
     * Get views by completion range
     */
    Page<ContentViewResponse> getViewsByCompletionRange(Double minCompletion, Double maxCompletion, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Inner Classes for Response Objects
    // =============================================================================
    
    /**
     * View Statistics
     */
    class ViewStatistics {
        private Long totalViews;
        private Long uniqueViewers;
        private Double averageViewDuration;
        private Double averageViewCompletion;
        private Long viewsWithHighCompletion;
        private Long viewsWithLongDuration;
        private Long anonymousViews;
        private Long familyViews;
        private Double familyViewPercentage;
        private Double highCompletionPercentage;
        
        // Constructors, getters, and setters
        public ViewStatistics() {}
        
        public ViewStatistics(Long totalViews, Long uniqueViewers, Double averageViewDuration, Double averageViewCompletion,
                             Long viewsWithHighCompletion, Long viewsWithLongDuration, Long anonymousViews, Long familyViews,
                             Double familyViewPercentage, Double highCompletionPercentage) {
            this.totalViews = totalViews;
            this.uniqueViewers = uniqueViewers;
            this.averageViewDuration = averageViewDuration;
            this.averageViewCompletion = averageViewCompletion;
            this.viewsWithHighCompletion = viewsWithHighCompletion;
            this.viewsWithLongDuration = viewsWithLongDuration;
            this.anonymousViews = anonymousViews;
            this.familyViews = familyViews;
            this.familyViewPercentage = familyViewPercentage;
            this.highCompletionPercentage = highCompletionPercentage;
        }
        
        // Getters and setters
        public Long getTotalViews() { return totalViews; }
        public void setTotalViews(Long totalViews) { this.totalViews = totalViews; }
        
        public Long getUniqueViewers() { return uniqueViewers; }
        public void setUniqueViewers(Long uniqueViewers) { this.uniqueViewers = uniqueViewers; }
        
        public Double getAverageViewDuration() { return averageViewDuration; }
        public void setAverageViewDuration(Double averageViewDuration) { this.averageViewDuration = averageViewDuration; }
        
        public Double getAverageViewCompletion() { return averageViewCompletion; }
        public void setAverageViewCompletion(Double averageViewCompletion) { this.averageViewCompletion = averageViewCompletion; }
        
        public Long getViewsWithHighCompletion() { return viewsWithHighCompletion; }
        public void setViewsWithHighCompletion(Long viewsWithHighCompletion) { this.viewsWithHighCompletion = viewsWithHighCompletion; }
        
        public Long getViewsWithLongDuration() { return viewsWithLongDuration; }
        public void setViewsWithLongDuration(Long viewsWithLongDuration) { this.viewsWithLongDuration = viewsWithLongDuration; }
        
        public Long getAnonymousViews() { return anonymousViews; }
        public void setAnonymousViews(Long anonymousViews) { this.anonymousViews = anonymousViews; }
        
        public Long getFamilyViews() { return familyViews; }
        public void setFamilyViews(Long familyViews) { this.familyViews = familyViews; }
        
        public Double getFamilyViewPercentage() { return familyViewPercentage; }
        public void setFamilyViewPercentage(Double familyViewPercentage) { this.familyViewPercentage = familyViewPercentage; }
        
        public Double getHighCompletionPercentage() { return highCompletionPercentage; }
        public void setHighCompletionPercentage(Double highCompletionPercentage) { this.highCompletionPercentage = highCompletionPercentage; }
    }
}
