package com.legacykeep.interaction.repository;

import com.legacykeep.interaction.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Bookmark Repository
 * 
 * Repository for managing bookmark entities with custom query methods.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    
    // =============================================================================
    // Basic CRUD Operations
    // =============================================================================
    
    /**
     * Find bookmark by content ID and user ID
     */
    @Query("SELECT b FROM Bookmark b WHERE b.contentId = :contentId AND b.userId = :userId")
    Optional<Bookmark> findByContentIdAndUserId(@Param("contentId") UUID contentId, @Param("userId") UUID userId);
    
    /**
     * Find all bookmarks for a specific content
     */
    @Query("SELECT b FROM Bookmark b WHERE b.contentId = :contentId ORDER BY b.createdAt DESC")
    Page<Bookmark> findByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find all bookmarks by a specific user
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId ORDER BY b.createdAt DESC")
    Page<Bookmark> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    // =============================================================================
    // Family Context Operations
    // =============================================================================
    
    /**
     * Find bookmarks by family member
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :familyMemberId ORDER BY b.createdAt DESC")
    Page<Bookmark> findByFamilyMemberId(@Param("familyMemberId") UUID familyMemberId, Pageable pageable);
    
    /**
     * Find bookmarks by generation level
     */
    @Query("SELECT b FROM Bookmark b WHERE b.generationLevel = :generationLevel ORDER BY b.createdAt DESC")
    Page<Bookmark> findByGenerationLevel(@Param("generationLevel") Integer generationLevel, Pageable pageable);
    
    /**
     * Find bookmarks by cultural context
     */
    @Query("SELECT b FROM Bookmark b WHERE b.culturalContext LIKE %:culturalTag% ORDER BY b.createdAt DESC")
    Page<Bookmark> findByCulturalContext(@Param("culturalTag") String culturalTag, Pageable pageable);
    
    // =============================================================================
    // Organization Operations
    // =============================================================================
    
    /**
     * Find bookmarks by name
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND b.bookmarkName LIKE %:name% ORDER BY b.createdAt DESC")
    Page<Bookmark> findByUserIdAndBookmarkNameContaining(@Param("userId") UUID userId, @Param("name") String name, Pageable pageable);
    
    /**
     * Find bookmarks with names
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND b.bookmarkName IS NOT NULL AND b.bookmarkName != '' ORDER BY b.createdAt DESC")
    Page<Bookmark> findByUserIdAndBookmarkNameNotNull(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find bookmarks with descriptions
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND b.bookmarkDescription IS NOT NULL AND b.bookmarkDescription != '' ORDER BY b.createdAt DESC")
    Page<Bookmark> findByUserIdAndBookmarkDescriptionNotNull(@Param("userId") UUID userId, Pageable pageable);
    
    // =============================================================================
    // Analytics and Statistics
    // =============================================================================
    
    /**
     * Count bookmarks for content
     */
    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.contentId = :contentId")
    Long countByContentId(@Param("contentId") UUID contentId);
    
    /**
     * Count bookmarks by user
     */
    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.userId = :userId")
    Long countByUserId(@Param("userId") UUID userId);
    
    /**
     * Find most bookmarked content
     */
    @Query("SELECT b.contentId, COUNT(b) as bookmarkCount FROM Bookmark b GROUP BY b.contentId ORDER BY bookmarkCount DESC")
    List<Object[]> findMostBookmarkedContent(Pageable pageable);
    
    // =============================================================================
    // Time-based Queries
    // =============================================================================
    
    /**
     * Find recent bookmarks
     */
    @Query("SELECT b FROM Bookmark b WHERE b.createdAt >= :since ORDER BY b.createdAt DESC")
    Page<Bookmark> findRecentBookmarks(@Param("since") LocalDateTime since, Pageable pageable);
    
    /**
     * Find bookmarks by date range
     */
    @Query("SELECT b FROM Bookmark b WHERE b.createdAt BETWEEN :startDate AND :endDate ORDER BY b.createdAt DESC")
    Page<Bookmark> findBookmarksByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    // =============================================================================
    // Privacy and Visibility
    // =============================================================================
    
    /**
     * Find public bookmarks for content
     */
    @Query("SELECT b FROM Bookmark b WHERE b.contentId = :contentId AND b.isPrivate = false ORDER BY b.createdAt DESC")
    Page<Bookmark> findPublicBookmarksByContentId(@Param("contentId") UUID contentId, Pageable pageable);
    
    /**
     * Find private bookmarks by user
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND b.isPrivate = true ORDER BY b.createdAt DESC")
    Page<Bookmark> findPrivateBookmarksByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    // =============================================================================
    // Search Operations
    // =============================================================================
    
    /**
     * Search bookmarks by name or description
     */
    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND (b.bookmarkName LIKE %:searchText% OR b.bookmarkDescription LIKE %:searchText%) ORDER BY b.createdAt DESC")
    Page<Bookmark> searchBookmarksByUser(@Param("userId") UUID userId, @Param("searchText") String searchText, Pageable pageable);
}
