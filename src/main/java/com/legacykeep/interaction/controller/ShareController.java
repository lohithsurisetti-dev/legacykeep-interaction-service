package com.legacykeep.interaction.controller;

import com.legacykeep.interaction.dto.request.CreateShareRequest;
import com.legacykeep.interaction.dto.response.ShareResponse;
import com.legacykeep.interaction.service.ShareService;
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
 * Share Controller
 * 
 * REST controller for share operations in the family legacy system.
 * Provides comprehensive share management with family context and targeting.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/shares")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Share Management", description = "APIs for managing shares of family legacy content")
public class ShareController {
    
    private final ShareService shareService;
    
    // =============================================================================
    // Share CRUD Operations
    // =============================================================================
    
    @PostMapping
    @Operation(summary = "Create a new share", description = "Create a new share for family legacy content")
    public ResponseEntity<ShareResponse> createShare(
            @Valid @RequestBody CreateShareRequest request,
            @Parameter(description = "User ID creating the share") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Creating share for content: {} by user: {} - type: {}", 
                request.getContentId(), userId, request.getShareType());
        
        ShareResponse response = shareService.createShare(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/content/{contentId}")
    @Operation(summary = "Get shares for content", description = "Get paginated shares for specific content")
    public ResponseEntity<Page<ShareResponse>> getSharesForContent(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares for content: {} by user: {}", contentId, userId);
        
        Page<ShareResponse> response = shareService.getSharesForContent(contentId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{shareId}")
    @Operation(summary = "Get share by ID", description = "Get a specific share by its ID")
    public ResponseEntity<ShareResponse> getShareById(
            @Parameter(description = "Share ID") @PathVariable Long shareId,
            @Parameter(description = "User ID requesting the share") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting share by ID: {} for user: {}", shareId, userId);
        
        ShareResponse response = shareService.getShareById(shareId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{shareId}")
    @Operation(summary = "Delete share", description = "Delete a share")
    public ResponseEntity<Void> deleteShare(
            @Parameter(description = "Share ID") @PathVariable Long shareId,
            @Parameter(description = "User ID deleting the share") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Deleting share: {} by user: {}", shareId, userId);
        
        shareService.deleteShare(shareId, userId);
        
        return ResponseEntity.noContent().build();
    }
    
    // =============================================================================
    // User Share Operations
    // =============================================================================
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's shares", description = "Get all shares by a specific user")
    public ResponseEntity<Page<ShareResponse>> getUserShares(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares by user: {} for requesting user: {}", userId, requestingUserId);
        
        Page<ShareResponse> response = shareService.getUserShares(userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/received/{userId}")
    @Operation(summary = "Get shares received by user", description = "Get shares received by a specific user")
    public ResponseEntity<Page<ShareResponse>> getSharesReceivedByUser(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Requesting user ID") @RequestHeader("X-User-ID") UUID requestingUserId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares received by user: {} for requesting user: {}", userId, requestingUserId);
        
        Page<ShareResponse> response = shareService.getSharesReceivedByUser(userId, pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/family/{familyId}")
    @Operation(summary = "Get shares for family", description = "Get shares for a specific family")
    public ResponseEntity<Page<ShareResponse>> getSharesForFamily(
            @Parameter(description = "Family ID") @PathVariable UUID familyId,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares for family: {} by user: {}", familyId, userId);
        
        Page<ShareResponse> response = shareService.getSharesForFamily(familyId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    @GetMapping("/family-member/{familyMemberId}")
    @Operation(summary = "Get shares by family member", description = "Get shares by a specific family member")
    public ResponseEntity<Page<ShareResponse>> getSharesByFamilyMember(
            @Parameter(description = "Family member ID") @PathVariable UUID familyMemberId,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares by family member: {} for user: {}", familyMemberId, userId);
        
        Page<ShareResponse> response = shareService.getSharesByFamilyMember(familyMemberId, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/generation/{generationLevel}")
    @Operation(summary = "Get shares by generation", description = "Get shares by generation level")
    public ResponseEntity<Page<ShareResponse>> getSharesByGeneration(
            @Parameter(description = "Generation level") @PathVariable Integer generationLevel,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares by generation: {} for user: {}", generationLevel, userId);
        
        Page<ShareResponse> response = shareService.getSharesByGeneration(generationLevel, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Share Type Operations
    // =============================================================================
    
    @GetMapping("/type/{shareType}")
    @Operation(summary = "Get shares by type", description = "Get shares by specific type")
    public ResponseEntity<Page<ShareResponse>> getSharesByType(
            @Parameter(description = "Share type") @PathVariable String shareType,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares by type: {} for user: {}", shareType, userId);
        
        Page<ShareResponse> response = shareService.getSharesByType(shareType, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/content/{contentId}/type/{shareType}")
    @Operation(summary = "Get shares by content and type", description = "Get shares by content and specific type")
    public ResponseEntity<Page<ShareResponse>> getSharesByContentAndType(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "Share type") @PathVariable String shareType,
            @Parameter(description = "User ID requesting shares") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting shares by content: {} and type: {} for user: {}", contentId, shareType, userId);
        
        Page<ShareResponse> response = shareService.getSharesByContentAndType(contentId, shareType, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    @GetMapping("/content/{contentId}/statistics")
    @Operation(summary = "Get share statistics", description = "Get share statistics for content")
    public ResponseEntity<ShareService.ShareStatistics> getShareStatistics(
            @Parameter(description = "Content ID") @PathVariable UUID contentId,
            @Parameter(description = "User ID requesting statistics") @RequestHeader("X-User-ID") UUID userId) {
        
        log.info("Getting share statistics for content: {} by user: {}", contentId, userId);
        
        ShareService.ShareStatistics response = shareService.getShareStatistics(contentId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/most-shared")
    @Operation(summary = "Get most shared content", description = "Get content with most shares")
    public ResponseEntity<Page<ShareResponse>> getMostSharedContent(
            @Parameter(description = "User ID requesting most shared content") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting most shared content for user: {}", userId);
        
        Page<ShareResponse> response = shareService.getMostSharedContent(pageable, userId);
        
        return ResponseEntity.ok(response);
    }
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    @GetMapping("/search")
    @Operation(summary = "Search shares", description = "Search shares by message content")
    public ResponseEntity<Page<ShareResponse>> searchShares(
            @Parameter(description = "Search text") @RequestParam String searchText,
            @Parameter(description = "User ID performing search") @RequestHeader("X-User-ID") UUID userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Searching shares with text: {} for user: {}", searchText, userId);
        
        Page<ShareResponse> response = shareService.searchShares(searchText, pageable, userId);
        
        return ResponseEntity.ok(response);
    }
}
