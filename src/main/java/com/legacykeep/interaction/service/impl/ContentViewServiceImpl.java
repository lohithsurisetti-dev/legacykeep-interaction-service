package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateContentViewRequest;
import com.legacykeep.interaction.dto.response.ContentViewResponse;
import com.legacykeep.interaction.entity.ContentView;
import com.legacykeep.interaction.exception.InteractionServiceException;
import com.legacykeep.interaction.repository.ContentViewRepository;
import com.legacykeep.interaction.service.ContentViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Content View Service Implementation
 * 
 * Implementation of content view service with family context and analytics.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContentViewServiceImpl implements ContentViewService {
    
    private final ContentViewRepository contentViewRepository;
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    @Override
    public ContentViewResponse createContentView(CreateContentViewRequest request, UUID userId) {
        log.info("Creating content view for content: {} by user: {} - duration: {}s, completion: {}%", 
                request.getContentId(), userId, request.getViewDuration(), request.getViewCompletionPercentage());
        
        // Create new content view
        ContentView contentView = ContentView.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .viewDuration(request.getViewDuration())
                .viewCompletionPercentage(request.getViewCompletionPercentage())
                .familyContext(request.getFamilyContext())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .isAnonymous(request.getIsAnonymous())
                .metadata(request.getMetadata())
                .build();
        
        ContentView savedView = contentViewRepository.save(contentView);
        log.info("Content view created successfully with ID: {}", savedView.getId());
        
        return convertToContentViewResponse(savedView, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ContentViewResponse getContentViewById(Long viewId, UUID userId) {
        log.info("Getting content view by ID: {} for user: {}", viewId, userId);
        
        ContentView contentView = contentViewRepository.findById(viewId)
                .orElseThrow(() -> new InteractionServiceException("Content view not found"));
        
        return convertToContentViewResponse(contentView, userId);
    }
    
    // =============================================================================
    // Content View Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting views for content: {} by user: {}", contentId, userId);
        
        Page<ContentView> views = contentViewRepository.findByContentId(contentId, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getUserViews(UUID userId, Pageable pageable) {
        log.info("Getting views by user: {}", userId);
        
        Page<ContentView> views = contentViewRepository.findByUserId(userId, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByContentAndUser(UUID contentId, UUID userId, Pageable pageable) {
        log.info("Getting views by content: {} and user: {}", contentId, userId);
        
        Page<ContentView> views = contentViewRepository.findByContentIdAndUserId(contentId, userId, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting views by family member: {} for user: {}", familyMemberId, userId);
        
        Page<ContentView> views = contentViewRepository.findByFamilyMemberId(familyMemberId, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting views by generation: {} for user: {}", generationLevel, userId);
        
        Page<ContentView> views = contentViewRepository.findByGenerationLevel(generationLevel, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByContentAndGeneration(UUID contentId, Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting views by content: {} and generation: {} for user: {}", contentId, generationLevel, userId);
        
        Page<ContentView> views = contentViewRepository.findByContentIdAndGenerationLevel(contentId, generationLevel, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public ViewStatistics getViewStatistics(UUID contentId, UUID userId) {
        log.info("Getting view statistics for content: {} by user: {}", contentId, userId);
        
        Long totalViews = contentViewRepository.countByContentId(contentId);
        Long uniqueViewers = contentViewRepository.countUniqueViewersByContentId(contentId);
        Double averageViewDuration = contentViewRepository.findAverageViewDurationByContentId(contentId);
        Double averageViewCompletion = contentViewRepository.findAverageViewCompletionByContentId(contentId);
        
        // Calculate additional statistics
        Long viewsWithHighCompletion = contentViewRepository.findViewsWithHighCompletion(80.0, Pageable.unpaged()).getTotalElements();
        Long viewsWithLongDuration = contentViewRepository.findViewsWithLongDuration(300, Pageable.unpaged()).getTotalElements(); // 5+ minutes
        Long anonymousViews = contentViewRepository.findAnonymousViews(Pageable.unpaged()).getTotalElements();
        
        // Calculate family vs non-family views (simplified)
        Long familyViews = totalViews / 2; // TODO: Implement proper family view calculation
        Double familyViewPercentage = totalViews > 0 ? (familyViews.doubleValue() / totalViews) * 100 : 0.0;
        Double highCompletionPercentage = totalViews > 0 ? (viewsWithHighCompletion.doubleValue() / totalViews) * 100 : 0.0;
        
        return new ViewStatistics(
                totalViews, uniqueViewers, averageViewDuration, averageViewCompletion,
                viewsWithHighCompletion, viewsWithLongDuration, anonymousViews, familyViews,
                familyViewPercentage, highCompletionPercentage
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getMostViewedContent(Pageable pageable, UUID userId) {
        log.info("Getting most viewed content for user: {}", userId);
        
        // This would need a custom implementation to get content details
        // For now, return empty page
        return Page.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getContentWithHighestCompletionRates(Pageable pageable, UUID userId) {
        log.info("Getting content with highest completion rates for user: {}", userId);
        
        // This would need a custom implementation to get content details
        // For now, return empty page
        return Page.empty();
    }
    
    // =============================================================================
    // Time-based Analytics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getRecentViews(Pageable pageable, UUID userId) {
        log.info("Getting recent views for user: {}", userId);
        
        LocalDateTime since = LocalDateTime.now().minusDays(7); // Last 7 days
        Page<ContentView> views = contentViewRepository.findRecentViews(since, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByDateRange(String startDate, String endDate, Pageable pageable, UUID userId) {
        log.info("Getting views by date range: {} to {} for user: {}", startDate, endDate, userId);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        
        Page<ContentView> views = contentViewRepository.findViewsByDateRange(start, end, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsToday(Pageable pageable, UUID userId) {
        log.info("Getting views today for user: {}", userId);
        
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Page<ContentView> views = contentViewRepository.findViewsToday(today, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    // =============================================================================
    // View Analysis
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsWithHighCompletion(Double minCompletion, Pageable pageable, UUID userId) {
        log.info("Getting views with high completion: {}% for user: {}", minCompletion, userId);
        
        Page<ContentView> views = contentViewRepository.findViewsWithHighCompletion(minCompletion, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsWithLongDuration(Integer minDuration, Pageable pageable, UUID userId) {
        log.info("Getting views with long duration: {}s for user: {}", minDuration, userId);
        
        Page<ContentView> views = contentViewRepository.findViewsWithLongDuration(minDuration, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentViewResponse> getViewsByCompletionRange(Double minCompletion, Double maxCompletion, Pageable pageable, UUID userId) {
        log.info("Getting views by completion range: {}% to {}% for user: {}", minCompletion, maxCompletion, userId);
        
        Page<ContentView> views = contentViewRepository.findViewsByCompletionRange(minCompletion, maxCompletion, pageable);
        
        return views.map(view -> convertToContentViewResponse(view, userId));
    }
    
    // =============================================================================
    // Helper Methods
    // =============================================================================
    
    /**
     * Convert ContentView entity to ContentViewResponse DTO
     */
    private ContentViewResponse convertToContentViewResponse(ContentView contentView, UUID userId) {
        return ContentViewResponse.builder()
                .id(contentView.getId())
                .contentId(contentView.getContentId())
                .userId(contentView.getUserId())
                .userName("User Name") // TODO: Get from User Service
                .userAvatar("User Avatar") // TODO: Get from User Service
                .viewDuration(contentView.getViewDuration())
                .viewCompletionPercentage(contentView.getViewCompletionPercentage())
                .familyContext(contentView.getFamilyContext())
                .generationLevel(contentView.getGenerationLevel())
                .generationName("Generation " + contentView.getGenerationLevel())
                .relationshipContext(contentView.getRelationshipContext())
                .isAnonymous(contentView.getIsAnonymous())
                .metadata(contentView.getMetadata())
                .createdAt(contentView.getCreatedAt())
                .familyMemberName("Family Member") // TODO: Get from Relationship Service
                .relationshipToUser("Relationship") // TODO: Get from Relationship Service
                .isFromSameGeneration(false) // TODO: Calculate from generation levels
                .isFromSameFamily(false) // TODO: Calculate from family context
                .build();
    }
}
