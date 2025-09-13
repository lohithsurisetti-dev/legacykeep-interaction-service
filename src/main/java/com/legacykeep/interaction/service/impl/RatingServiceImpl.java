package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateRatingRequest;
import com.legacykeep.interaction.dto.request.UpdateRatingRequest;
import com.legacykeep.interaction.dto.response.RatingResponse;
import com.legacykeep.interaction.entity.Rating;
import com.legacykeep.interaction.exception.InteractionServiceException;
import com.legacykeep.interaction.repository.RatingRepository;
import com.legacykeep.interaction.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Rating Service Implementation
 * 
 * Implementation of rating service with family context and cultural sensitivity.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RatingServiceImpl implements RatingService {
    
    private final RatingRepository ratingRepository;
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    @Override
    public RatingResponse createRating(CreateRatingRequest request, UUID userId) {
        log.info("Creating rating for content: {} by user: {} - rating: {}", 
                request.getContentId(), userId, request.getRatingValue());
        
        // Check if user already rated this content
        Optional<Rating> existingRating = ratingRepository.findByContentIdAndUserId(request.getContentId(), userId);
        if (existingRating.isPresent()) {
            throw new InteractionServiceException("User has already rated this content");
        }
        
        // Create new rating
        Rating rating = Rating.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .ratingValue(request.getRatingValue())
                .ratingText(request.getRatingText())
                .familyContext(request.getFamilyContext())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .culturalContext(request.getCulturalContext())
                .isAnonymous(request.getIsAnonymous())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .build();
        
        Rating savedRating = ratingRepository.save(rating);
        log.info("Rating created successfully with ID: {}", savedRating.getId());
        
        return convertToRatingResponse(savedRating, userId);
    }
    
    @Override
    public RatingResponse updateRating(Long ratingId, UpdateRatingRequest request, UUID userId) {
        log.info("Updating rating: {} by user: {}", ratingId, userId);
        
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new InteractionServiceException("Rating not found"));
        
        // Check if user owns this rating
        if (!rating.getUserId().equals(userId)) {
            throw new InteractionServiceException("User can only update their own ratings");
        }
        
        // Update rating fields
        rating.setRatingValue(request.getRatingValue());
        rating.setRatingText(request.getRatingText());
        rating.setFamilyContext(request.getFamilyContext());
        rating.setGenerationLevel(request.getGenerationLevel());
        rating.setRelationshipContext(request.getRelationshipContext());
        rating.setCulturalContext(request.getCulturalContext());
        if (request.getIsAnonymous() != null) {
            rating.setIsAnonymous(request.getIsAnonymous());
        }
        if (request.getIsPrivate() != null) {
            rating.setIsPrivate(request.getIsPrivate());
        }
        rating.setMetadata(request.getMetadata());
        
        Rating updatedRating = ratingRepository.save(rating);
        log.info("Rating updated successfully: {}", updatedRating.getId());
        
        return convertToRatingResponse(updatedRating, userId);
    }
    
    @Override
    public void deleteRating(Long ratingId, UUID userId) {
        log.info("Deleting rating: {} by user: {}", ratingId, userId);
        
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new InteractionServiceException("Rating not found"));
        
        // Check if user owns this rating
        if (!rating.getUserId().equals(userId)) {
            throw new InteractionServiceException("User can only delete their own ratings");
        }
        
        ratingRepository.delete(rating);
        log.info("Rating deleted successfully: {}", ratingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRatingById(Long ratingId, UUID userId) {
        log.info("Getting rating by ID: {} for user: {}", ratingId, userId);
        
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new InteractionServiceException("Rating not found"));
        
        return convertToRatingResponse(rating, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RatingResponse getUserRating(UUID contentId, UUID userId) {
        log.info("Getting user rating for content: {} by user: {}", contentId, userId);
        
        Optional<Rating> rating = ratingRepository.findByContentIdAndUserId(contentId, userId);
        if (rating.isEmpty()) {
            throw new InteractionServiceException("User has not rated this content");
        }
        
        return convertToRatingResponse(rating.get(), userId);
    }
    
    // =============================================================================
    // Content Rating Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting ratings for content: {} by user: {}", contentId, userId);
        
        Page<Rating> ratings = ratingRepository.findByContentId(contentId, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getUserRatings(UUID userId, Pageable pageable) {
        log.info("Getting ratings by user: {}", userId);
        
        Page<Rating> ratings = ratingRepository.findByUserId(userId, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting ratings by family member: {} for user: {}", familyMemberId, userId);
        
        Page<Rating> ratings = ratingRepository.findByFamilyMemberId(familyMemberId, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting ratings by generation: {} for user: {}", generationLevel, userId);
        
        Page<Rating> ratings = ratingRepository.findByGenerationLevel(generationLevel, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsByCulturalContext(String culturalTag, Pageable pageable, UUID userId) {
        log.info("Getting ratings by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<Rating> ratings = ratingRepository.findByCulturalContext(culturalTag, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public RatingStatistics getRatingStatistics(UUID contentId, UUID userId) {
        log.info("Getting rating statistics for content: {} by user: {}", contentId, userId);
        
        Long totalRatings = ratingRepository.countByContentId(contentId);
        Double averageRating = ratingRepository.getAverageRatingByContentId(contentId);
        List<Object[]> distribution = ratingRepository.getRatingDistributionByContentId(contentId);
        
        // Parse distribution data
        Long ratingCount1 = 0L, ratingCount2 = 0L, ratingCount3 = 0L, ratingCount4 = 0L, ratingCount5 = 0L;
        for (Object[] row : distribution) {
            Integer ratingValue = (Integer) row[0];
            Long count = (Long) row[1];
            switch (ratingValue) {
                case 1: ratingCount1 = count; break;
                case 2: ratingCount2 = count; break;
                case 3: ratingCount3 = count; break;
                case 4: ratingCount4 = count; break;
                case 5: ratingCount5 = count; break;
            }
        }
        
        // Count ratings with text
        Long ratingsWithText = ratingRepository.findRatingsWithText(Pageable.unpaged()).getTotalElements();
        Double textReviewPercentage = totalRatings > 0 ? (ratingsWithText.doubleValue() / totalRatings) * 100 : 0.0;
        
        return new RatingStatistics(
                totalRatings, averageRating, ratingCount1, ratingCount2, ratingCount3, 
                ratingCount4, ratingCount5, ratingsWithText, textReviewPercentage
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public RatingSummary getRatingSummary(UUID contentId, UUID userId) {
        log.info("Getting rating summary for content: {} by user: {}", contentId, userId);
        
        Long totalRatings = ratingRepository.countByContentId(contentId);
        Double averageRating = ratingRepository.getAverageRatingByContentId(contentId);
        
        // Check if user has rated this content
        Optional<Rating> userRating = ratingRepository.findByContentIdAndUserId(contentId, userId);
        Boolean hasUserRated = userRating.isPresent();
        Integer userRatingValue = userRating.map(Rating::getRatingValue).orElse(null);
        
        return new RatingSummary(totalRatings, averageRating, null, hasUserRated, userRatingValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getHighRatedContent(Pageable pageable, UUID userId) {
        log.info("Getting high-rated content for user: {}", userId);
        
        Page<Rating> ratings = ratingRepository.findHighRatedContent(pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsWithText(Pageable pageable, UUID userId) {
        log.info("Getting ratings with text for user: {}", userId);
        
        Page<Rating> ratings = ratingRepository.findRatingsWithText(pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> searchRatings(String searchText, Pageable pageable, UUID userId) {
        log.info("Searching ratings with text: {} for user: {}", searchText, userId);
        
        Page<Rating> ratings = ratingRepository.searchRatingsByText(searchText, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRatingsByValue(Integer ratingValue, Pageable pageable, UUID userId) {
        log.info("Getting ratings by value: {} for user: {}", ratingValue, userId);
        
        Page<Rating> ratings = ratingRepository.findByRatingValue(ratingValue, pageable);
        
        return ratings.map(rating -> convertToRatingResponse(rating, userId));
    }
    
    // =============================================================================
    // Helper Methods
    // =============================================================================
    
    /**
     * Convert Rating entity to RatingResponse DTO
     */
    private RatingResponse convertToRatingResponse(Rating rating, UUID userId) {
        return RatingResponse.builder()
                .id(rating.getId())
                .contentId(rating.getContentId())
                .userId(rating.getUserId())
                .userName("User Name") // TODO: Get from User Service
                .userAvatar("User Avatar") // TODO: Get from User Service
                .ratingValue(rating.getRatingValue())
                .ratingText(rating.getRatingText())
                .familyContext(rating.getFamilyContext())
                .generationLevel(rating.getGenerationLevel())
                .generationName("Generation " + rating.getGenerationLevel())
                .relationshipContext(rating.getRelationshipContext())
                .culturalContext(rating.getCulturalContext())
                .isAnonymous(rating.getIsAnonymous())
                .isPrivate(rating.getIsPrivate())
                .metadata(rating.getMetadata())
                .createdAt(rating.getCreatedAt())
                .updatedAt(rating.getUpdatedAt())
                .familyMemberName("Family Member") // TODO: Get from Relationship Service
                .relationshipToUser("Relationship") // TODO: Get from Relationship Service
                .isFromSameGeneration(false) // TODO: Calculate from generation levels
                .isFromSameFamily(false) // TODO: Calculate from family context
                .build();
    }
}
