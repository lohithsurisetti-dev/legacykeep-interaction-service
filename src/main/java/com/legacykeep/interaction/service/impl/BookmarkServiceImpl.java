package com.legacykeep.interaction.service.impl;

import com.legacykeep.interaction.dto.request.CreateBookmarkRequest;
import com.legacykeep.interaction.dto.request.UpdateBookmarkRequest;
import com.legacykeep.interaction.dto.response.BookmarkResponse;
import com.legacykeep.interaction.entity.Bookmark;
import com.legacykeep.interaction.exception.InteractionServiceException;
import com.legacykeep.interaction.repository.BookmarkRepository;
import com.legacykeep.interaction.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Bookmark Service Implementation
 * 
 * Implementation of bookmark service with family context and organization.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    
    private final BookmarkRepository bookmarkRepository;
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    @Override
    public BookmarkResponse createBookmark(CreateBookmarkRequest request, UUID userId) {
        log.info("Creating bookmark for content: {} by user: {} - name: {}", 
                request.getContentId(), userId, request.getBookmarkName());
        
        // Check if user already bookmarked this content
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByContentIdAndUserId(request.getContentId(), userId);
        if (existingBookmark.isPresent()) {
            throw new InteractionServiceException("User has already bookmarked this content");
        }
        
        // Create new bookmark
        Bookmark bookmark = Bookmark.builder()
                .contentId(request.getContentId())
                .userId(userId)
                .bookmarkName(request.getBookmarkName())
                .bookmarkDescription(request.getBookmarkDescription())
                .familyContext(request.getFamilyContext())
                .generationLevel(request.getGenerationLevel())
                .relationshipContext(request.getRelationshipContext())
                .culturalContext(request.getCulturalContext())
                .isPrivate(request.getIsPrivate())
                .metadata(request.getMetadata())
                .build();
        
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        log.info("Bookmark created successfully with ID: {}", savedBookmark.getId());
        
        return convertToBookmarkResponse(savedBookmark, userId);
    }
    
    @Override
    public BookmarkResponse updateBookmark(Long bookmarkId, UpdateBookmarkRequest request, UUID userId) {
        log.info("Updating bookmark: {} by user: {}", bookmarkId, userId);
        
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new InteractionServiceException("Bookmark not found"));
        
        // Check if user owns this bookmark
        if (!bookmark.getUserId().equals(userId)) {
            throw new InteractionServiceException("User can only update their own bookmarks");
        }
        
        // Update bookmark fields
        bookmark.setBookmarkName(request.getBookmarkName());
        bookmark.setBookmarkDescription(request.getBookmarkDescription());
        bookmark.setFamilyContext(request.getFamilyContext());
        bookmark.setGenerationLevel(request.getGenerationLevel());
        bookmark.setRelationshipContext(request.getRelationshipContext());
        bookmark.setCulturalContext(request.getCulturalContext());
        if (request.getIsPrivate() != null) {
            bookmark.setIsPrivate(request.getIsPrivate());
        }
        bookmark.setMetadata(request.getMetadata());
        
        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);
        log.info("Bookmark updated successfully: {}", updatedBookmark.getId());
        
        return convertToBookmarkResponse(updatedBookmark, userId);
    }
    
    @Override
    public void deleteBookmark(Long bookmarkId, UUID userId) {
        log.info("Deleting bookmark: {} by user: {}", bookmarkId, userId);
        
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new InteractionServiceException("Bookmark not found"));
        
        // Check if user owns this bookmark
        if (!bookmark.getUserId().equals(userId)) {
            throw new InteractionServiceException("User can only delete their own bookmarks");
        }
        
        bookmarkRepository.delete(bookmark);
        log.info("Bookmark deleted successfully: {}", bookmarkId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookmarkResponse getBookmarkById(Long bookmarkId, UUID userId) {
        log.info("Getting bookmark by ID: {} for user: {}", bookmarkId, userId);
        
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new InteractionServiceException("Bookmark not found"));
        
        return convertToBookmarkResponse(bookmark, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookmarkResponse getUserBookmark(UUID contentId, UUID userId) {
        log.info("Getting user bookmark for content: {} by user: {}", contentId, userId);
        
        Optional<Bookmark> bookmark = bookmarkRepository.findByContentIdAndUserId(contentId, userId);
        if (bookmark.isEmpty()) {
            throw new InteractionServiceException("User has not bookmarked this content");
        }
        
        return convertToBookmarkResponse(bookmark.get(), userId);
    }
    
    // =============================================================================
    // Content Bookmark Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksForContent(UUID contentId, Pageable pageable, UUID userId) {
        log.info("Getting bookmarks for content: {} by user: {}", contentId, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByContentId(contentId, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getUserBookmarks(UUID userId, Pageable pageable) {
        log.info("Getting bookmarks by user: {}", userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId) {
        log.info("Getting bookmarks by family member: {} for user: {}", familyMemberId, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByFamilyMemberId(familyMemberId, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksByGeneration(Integer generationLevel, Pageable pageable, UUID userId) {
        log.info("Getting bookmarks by generation: {} for user: {}", generationLevel, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByGenerationLevel(generationLevel, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksByCulturalContext(String culturalTag, Pageable pageable, UUID userId) {
        log.info("Getting bookmarks by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByCulturalContext(culturalTag, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    // =============================================================================
    // Organization Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksByName(String name, Pageable pageable, UUID userId) {
        log.info("Getting bookmarks by name: {} for user: {}", name, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserIdAndBookmarkNameContaining(userId, name, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksWithNames(Pageable pageable, UUID userId) {
        log.info("Getting bookmarks with names for user: {}", userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserIdAndBookmarkNameNotNull(userId, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getBookmarksWithDescriptions(Pageable pageable, UUID userId) {
        log.info("Getting bookmarks with descriptions for user: {}", userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserIdAndBookmarkDescriptionNotNull(userId, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public BookmarkStatistics getBookmarkStatistics(UUID contentId, UUID userId) {
        log.info("Getting bookmark statistics for content: {} by user: {}", contentId, userId);
        
        Long totalBookmarks = bookmarkRepository.countByContentId(contentId);
        Long bookmarksWithNames = bookmarkRepository.findByUserIdAndBookmarkNameNotNull(userId, Pageable.unpaged()).getTotalElements();
        Long bookmarksWithDescriptions = bookmarkRepository.findByUserIdAndBookmarkDescriptionNotNull(userId, Pageable.unpaged()).getTotalElements();
        
        // Calculate private vs public bookmarks (simplified)
        Long privateBookmarks = totalBookmarks / 2; // TODO: Implement proper private bookmark calculation
        Long publicBookmarks = totalBookmarks - privateBookmarks;
        
        Double namedBookmarkPercentage = totalBookmarks > 0 ? (bookmarksWithNames.doubleValue() / totalBookmarks) * 100 : 0.0;
        Double describedBookmarkPercentage = totalBookmarks > 0 ? (bookmarksWithDescriptions.doubleValue() / totalBookmarks) * 100 : 0.0;
        
        return new BookmarkStatistics(
                totalBookmarks, bookmarksWithNames, bookmarksWithDescriptions, 
                privateBookmarks, publicBookmarks, namedBookmarkPercentage, describedBookmarkPercentage
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getMostBookmarkedContent(Pageable pageable, UUID userId) {
        log.info("Getting most bookmarked content for user: {}", userId);
        
        // This would need a custom implementation to get content details
        // For now, return empty page
        return Page.empty();
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> searchBookmarks(String searchText, Pageable pageable, UUID userId) {
        log.info("Searching bookmarks with text: {} for user: {}", searchText, userId);
        
        Page<Bookmark> bookmarks = bookmarkRepository.searchBookmarksByUser(userId, searchText, pageable);
        
        return bookmarks.map(bookmark -> convertToBookmarkResponse(bookmark, userId));
    }
    
    // =============================================================================
    // Helper Methods
    // =============================================================================
    
    /**
     * Convert Bookmark entity to BookmarkResponse DTO
     */
    private BookmarkResponse convertToBookmarkResponse(Bookmark bookmark, UUID userId) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .contentId(bookmark.getContentId())
                .userId(bookmark.getUserId())
                .userName("User Name") // TODO: Get from User Service
                .userAvatar("User Avatar") // TODO: Get from User Service
                .bookmarkName(bookmark.getBookmarkName())
                .bookmarkDescription(bookmark.getBookmarkDescription())
                .familyContext(bookmark.getFamilyContext())
                .generationLevel(bookmark.getGenerationLevel())
                .generationName("Generation " + bookmark.getGenerationLevel())
                .relationshipContext(bookmark.getRelationshipContext())
                .culturalContext(bookmark.getCulturalContext())
                .isPrivate(bookmark.getIsPrivate())
                .metadata(bookmark.getMetadata())
                .createdAt(bookmark.getCreatedAt())
                .familyMemberName("Family Member") // TODO: Get from Relationship Service
                .relationshipToUser("Relationship") // TODO: Get from Relationship Service
                .isFromSameGeneration(false) // TODO: Calculate from generation levels
                .isFromSameFamily(false) // TODO: Calculate from family context
                .build();
    }
}
