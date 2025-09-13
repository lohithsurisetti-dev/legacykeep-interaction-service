package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateBookmarkRequest;
import com.legacykeep.interaction.dto.request.UpdateBookmarkRequest;
import com.legacykeep.interaction.dto.response.BookmarkResponse;
import com.legacykeep.interaction.service.BookmarkService;
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
 * Bookmark Controller
 * 
 * REST controller for bookmark operations in the family legacy system.
 * Provides comprehensive bookmark management with family context and organization.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bookmark Management", description = "APIs for managing bookmarks of family legacy content")
public class BookmarkController {
    
    private final BookmarkService bookmarkService;
    
    // =============================================================================
    // Bookmark CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Create a new bookmark", description = "Create a new bookmark for family legacy content")
    public ResponseEntity<BookmarkResponse> createBookmark(
            @Valid @RequestBody CreateBookmarkRequest request,
            @Parameter(description = "User ID creating the bookmark") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating bookmark for content: {} by user: {} - name: {}", 
                request.getContentId(), userId, request.getBookmarkName());
        
        BookmarkResponse response = bookmarkService.createBookmark(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get bookmarks for content", description = "Get paginated bookmarks for specific content")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks for content: {} by user: {}", contentId, userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{bookmarkId}")
    @Operation(summary = "Get bookmark by ID", description = "Get a specific bookmark by its ID")
    public ResponseEntity<BookmarkResponse> getBookmarkById(
            @Parameter(description = "Bookmark ID") @PathVariable Long bookmarkId,
            @Parameter(description = "User ID requesting the bookmark") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting bookmark by ID: {} for user: {}", bookmarkId, userId);
        
        BookmarkResponse response = bookmarkService.getBookmarkById(bookmarkId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{bookmarkId}")
    @Operation(summary = "Update bookmark", description = "Update an existing bookmark")
    public ResponseEntity<BookmarkResponse> updateBookmark(
            @Parameter(description = "Bookmark ID") @PathVariable Long bookmarkId,
            @Valid @RequestBody UpdateBookmarkRequest request,
            @Parameter(description = "User ID updating the bookmark") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Updating bookmark: {} by user: {}", bookmarkId, userId);
        
        BookmarkResponse response = bookmarkService.updateBookmark(bookmarkId, request, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{bookmarkId}")
    @Operation(summary = "Delete bookmark", description = "Delete a bookmark")
    public ResponseEntity<Void> deleteBookmark(
            @Parameter(description = "Bookmark ID") @PathVariable Long bookmarkId,
            @Parameter(description = "User ID deleting the bookmark") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Deleting bookmark: {} by user: {}", bookmarkId, userId);
        
        bookmarkService.deleteBookmark(bookmarkId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    // =============================================================================
    // User Bookmark Operations
    // =============================================================================
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's bookmarks", description = "Get all bookmarks by a specific user")
    public ResponseEntity<Page<BookmarkResponse>> getUserBookmarks(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks by user: {} for requesting user: {}", userId, requestingUserId);
        
        Page<BookmarkResponse> response = bookmarkService.getUserBookmarks(userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/user")
    @Operation(summary = "Get user's bookmark for content", description = "Get user's bookmark for specific content")
    public ResponseEntity<BookmarkResponse> getUserBookmark(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting user bookmark for content: {} by user: {}", contentId, userId);
        
        BookmarkResponse response = bookmarkService.getUserBookmark(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get bookmarks by family member", description = "Get bookmarks by a specific family member")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks by family member: {} for user: {}", familyMemberId, userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get bookmarks by generation", description = "Get bookmarks by generation level")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks by generation: {} for user: {}", generationLevel, userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cultural/{culturalTag}")
    @Operation(summary = "Get bookmarks by cultural context", description = "Get bookmarks by cultural tag")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksByCulturalContext(
            @Parameter(description = "Cultural tag") @PathVariable String culturalTag,
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks by cultural context: {} for user: {}", culturalTag, userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksByCulturalContext(culturalTag, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Organization Operations
    // =============================================================================
    
    @GetMapping("/name/{name}")
    @Operation(summary = "Get bookmarks by name", description = "Get bookmarks by name")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksByName(
            @Parameter(description = "Bookmark name") @PathVariable String name,
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks by name: {} for user: {}", name, userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksByName(name, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/with-names")
    @Operation(summary = "Get bookmarks with names", description = "Get bookmarks that have names")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksWithNames(
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks with names for user: {}", userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksWithNames(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/with-descriptions")
    @Operation(summary = "Get bookmarks with descriptions", description = "Get bookmarks that have descriptions")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarksWithDescriptions(
            @Parameter(description = "User ID requesting bookmarks") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting bookmarks with descriptions for user: {}", userId);
        
        Page<BookmarkResponse> response = bookmarkService.getBookmarksWithDescriptions(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get bookmark statistics", description = "Get bookmark statistics for content")
    public ResponseEntity<BookmarkService.BookmarkStatistics> getBookmarkStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting bookmark statistics for content: {} by user: {}", contentId, userId);
        
        BookmarkService.BookmarkStatistics response = bookmarkService.getBookmarkStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/most-bookmarked")
    @Operation(summary = "Get most bookmarked content", description = "Get content with most bookmarks")
    public ResponseEntity<Page<BookmarkResponse>> getMostBookmarkedContent(
            @Parameter(description = "User ID requesting most bookmarked content") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting most bookmarked content for user: {}", userId);
        
        Page<BookmarkResponse> response = bookmarkService.getMostBookmarkedContent(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @GetMapping("/search")
    @Operation(summary = "Search bookmarks", description = "Search bookmarks by name or description")
    public ResponseEntity<Page<BookmarkResponse>> searchBookmarks(
            @Parameter(description = "Search text") @RequestParam String searchText,
            @Parameter(description = "User ID performing search") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Searching bookmarks with text: {} for user: {}", searchText, userId);
        
        Page<BookmarkResponse> response = bookmarkService.searchBookmarks(searchText, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
}
