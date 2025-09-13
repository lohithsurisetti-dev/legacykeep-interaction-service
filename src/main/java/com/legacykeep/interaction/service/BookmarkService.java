package com.legacykeep.interaction.service;

import com.legacykeep.interaction.dto.request.CreateBookmarkRequest;
import com.legacykeep.interaction.dto.request.UpdateBookmarkRequest;
import com.legacykeep.interaction.dto.response.BookmarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Bookmark Service Interface
 * 
 * Service interface for managing bookmarks with family context and organization.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public interface BookmarkService {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Create a new bookmark
     */
    BookmarkResponse createBookmark(CreateBookmarkRequest request, UUID userId);
    
    /**
     * Update an existing bookmark
     */
    BookmarkResponse updateBookmark(Long bookmarkId, UpdateBookmarkRequest request, UUID userId);
    
    /**
     * Delete a bookmark
     */
    void deleteBookmark(Long bookmarkId, UUID userId);
    
    /**
     * Get bookmark by ID
     */
    BookmarkResponse getBookmarkById(Long bookmarkId, UUID userId);
    
    /**
     * Get user's bookmark for content
     */
    BookmarkResponse getUserBookmark(UUID contentId, UUID userId);
    
    // =============================================================================
    // Content Bookmark Operations
    // =============================================================================
    
    /**
     * Get all bookmarks for content
     */
    Page<BookmarkResponse> getBookmarksForContent(UUID contentId, Pageable pageable, UUID userId);
    
    /**
     * Get user's bookmarks
     */
    Page<BookmarkResponse> getUserBookmarks(UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Get bookmarks by family member
     */
    Page<BookmarkResponse> getBookmarksByFamilyMember(UUID familyMemberId, Pageable pageable, UUID userId);
    
    /**
     * Get bookmarks by generation level
     */
    Page<BookmarkResponse> getBookmarksByGeneration(Integer generationLevel, Pageable pageable, UUID userId);
    
    /**
     * Get bookmarks by cultural context
     */
    Page<BookmarkResponse> getBookmarksByCulturalContext(String culturalTag, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Organization Operations
    // =============================================================================
    
    /**
     * Get bookmarks by name
     */
    Page<BookmarkResponse> getBookmarksByName(String name, Pageable pageable, UUID userId);
    
    /**
     * Get bookmarks with names
     */
    Page<BookmarkResponse> getBookmarksWithNames(Pageable pageable, UUID userId);
    
    /**
     * Get bookmarks with descriptions
     */
    Page<BookmarkResponse> getBookmarksWithDescriptions(Pageable pageable, UUID userId);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Get bookmark statistics for content
     */
    BookmarkStatistics getBookmarkStatistics(UUID contentId, UUID userId);
    
    /**
     * Get most bookmarked content
     */
    Page<BookmarkResponse> getMostBookmarkedContent(Pageable pageable, UUID userId);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search bookmarks by name or description
     */
    Page<BookmarkResponse> searchBookmarks(String searchText, Pageable pageable, UUID userId);
    
    // =============================================================================
    // Inner Classes for Response Objects
    // =============================================================================
    
    /**
     * Bookmark Statistics
     */
    class BookmarkStatistics {
        private Long totalBookmarks;
        private Long bookmarksWithNames;
        private Long bookmarksWithDescriptions;
        private Long privateBookmarks;
        private Long publicBookmarks;
        private Double namedBookmarkPercentage;
        private Double describedBookmarkPercentage;
        
        // Constructors, getters, and setters
        public BookmarkStatistics() {}
        
        public BookmarkStatistics(Long totalBookmarks, Long bookmarksWithNames, Long bookmarksWithDescriptions, 
                                 Long privateBookmarks, Long publicBookmarks, Double namedBookmarkPercentage, 
                                 Double describedBookmarkPercentage) {
            this.totalBookmarks = totalBookmarks;
            this.bookmarksWithNames = bookmarksWithNames;
            this.bookmarksWithDescriptions = bookmarksWithDescriptions;
            this.privateBookmarks = privateBookmarks;
            this.publicBookmarks = publicBookmarks;
            this.namedBookmarkPercentage = namedBookmarkPercentage;
            this.describedBookmarkPercentage = describedBookmarkPercentage;
        }
        
        // Getters and setters
        public Long getTotalBookmarks() { return totalBookmarks; }
        public void setTotalBookmarks(Long totalBookmarks) { this.totalBookmarks = totalBookmarks; }
        
        public Long getBookmarksWithNames() { return bookmarksWithNames; }
        public void setBookmarksWithNames(Long bookmarksWithNames) { this.bookmarksWithNames = bookmarksWithNames; }
        
        public Long getBookmarksWithDescriptions() { return bookmarksWithDescriptions; }
        public void setBookmarksWithDescriptions(Long bookmarksWithDescriptions) { this.bookmarksWithDescriptions = bookmarksWithDescriptions; }
        
        public Long getPrivateBookmarks() { return privateBookmarks; }
        public void setPrivateBookmarks(Long privateBookmarks) { this.privateBookmarks = privateBookmarks; }
        
        public Long getPublicBookmarks() { return publicBookmarks; }
        public void setPublicBookmarks(Long publicBookmarks) { this.publicBookmarks = publicBookmarks; }
        
        public Double getNamedBookmarkPercentage() { return namedBookmarkPercentage; }
        public void setNamedBookmarkPercentage(Double namedBookmarkPercentage) { this.namedBookmarkPercentage = namedBookmarkPercentage; }
        
        public Double getDescribedBookmarkPercentage() { return describedBookmarkPercentage; }
        public void setDescribedBookmarkPercentage(Double describedBookmarkPercentage) { this.describedBookmarkPercentage = describedBookmarkPercentage; }
    }
}
