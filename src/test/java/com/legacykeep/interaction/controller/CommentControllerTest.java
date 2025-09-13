package com.legacykeep.interaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacykeep.interaction.dto.request.CreateCommentRequest;
import com.legacykeep.interaction.dto.request.UpdateCommentRequest;
import com.legacykeep.interaction.dto.response.CommentResponse;
import com.legacykeep.interaction.entity.Comment;
import com.legacykeep.interaction.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comment Controller Test
 * 
 * Comprehensive tests for the Comment Controller API endpoints.
 * Tests all CRUD operations, replies, interactions, and family context features.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@WebMvcTest(CommentController.class)
@DisplayName("Comment Controller Tests")
class CommentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CommentService commentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID testUserId;
    private UUID testContentId;
    private UUID testFamilyId;
    private Long testCommentId;
    private CommentResponse testCommentResponse;
    
    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testContentId = UUID.randomUUID();
        testFamilyId = UUID.randomUUID();
        testCommentId = 1L;
        
        testCommentResponse = CommentResponse.builder()
                .id(testCommentId)
                .contentId(testContentId)
                .userId(testUserId)
                .userName("Test User")
                .userAvatar("test-avatar.jpg")
                .commentText("This is a test comment")
                .mentions(List.of())
                .hashtags(List.of("#test", "#family"))
                .mediaUrls(List.of())
                .isEdited(false)
                .editCount(0)
                .editHistory(null)
                .status(Comment.CommentStatus.ACTIVE)
                .moderationStatus(Comment.ModerationStatus.APPROVED)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition", "heritage"))
                .generationLevel(2)
                .generationName("Generation 2")
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .sentimentScore(0.8)
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .replyCount(0)
                .likeCount(5)
                .reactionCount(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .replies(List.of())
                .hasUserLiked(false)
                .hasUserReacted(false)
                .userReactionType(null)
                .familyMemberName("Test Family Member")
                .relationshipToUser("Sibling")
                .isFromSameGeneration(true)
                .isFromSameFamily(true)
                .culturalContext("Traditional family values")
                .emotionalContext("Positive sentiment")
                .moderationReason(null)
                .moderatorId(null)
                .moderatorName(null)
                .moderatedAt(null)
                .build();
    }
    
    // =============================================================================
    // Comment CRUD Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should create a new comment successfully")
    void shouldCreateCommentSuccessfully() throws Exception {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is a test comment")
                .mentions(List.of())
                .hashtags(List.of("#test", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition", "heritage"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"test\"}")
                .build();
        
        when(commentService.createComment(any(CreateCommentRequest.class), eq(testUserId)))
                .thenReturn(testCommentResponse);
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.commentText").value("This is a test comment"))
                .andExpect(jsonPath("$.hashtags").isArray())
                .andExpect(jsonPath("$.hashtags[0]").value("#test"))
                .andExpect(jsonPath("$.hashtags[1]").value("#family"))
                .andExpect(jsonPath("$.culturalTags").isArray())
                .andExpect(jsonPath("$.culturalTags[0]").value("tradition"))
                .andExpect(jsonPath("$.culturalTags[1]").value("heritage"))
                .andExpect(jsonPath("$.generationLevel").value(2))
                .andExpect(jsonPath("$.generationName").value("Generation 2"))
                .andExpect(jsonPath("$.likeCount").value(5))
                .andExpect(jsonPath("$.reactionCount").value(3))
                .andExpect(jsonPath("$.isFromSameGeneration").value(true))
                .andExpect(jsonPath("$.isFromSameFamily").value(true));
        
        verify(commentService).createComment(any(CreateCommentRequest.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comments for content successfully")
    void shouldGetCommentsForContentSuccessfully() throws Exception {
        // Given
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsForContent(eq(testContentId), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/content/{contentId}", testContentId)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.content[0].contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.content[0].commentText").value("This is a test comment"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
        
        verify(commentService).getCommentsForContent(eq(testContentId), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comment by ID successfully")
    void shouldGetCommentByIdSuccessfully() throws Exception {
        // Given
        when(commentService.getCommentById(eq(testCommentId), eq(testUserId)))
                .thenReturn(testCommentResponse);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/{commentId}", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.commentText").value("This is a test comment"))
                .andExpect(jsonPath("$.generationLevel").value(2))
                .andExpect(jsonPath("$.likeCount").value(5));
        
        verify(commentService).getCommentById(eq(testCommentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should update comment successfully")
    void shouldUpdateCommentSuccessfully() throws Exception {
        // Given
        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .commentText("This is an updated test comment")
                .mentions(List.of())
                .hashtags(List.of("#updated", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition", "heritage", "updated"))
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"test\",\"updated\":true}")
                .editReason("Updated for testing")
                .build();
        
        CommentResponse updatedResponse = CommentResponse.builder()
                .id(testCommentId)
                .contentId(testContentId)
                .userId(testUserId)
                .userName("Test User")
                .commentText("This is an updated test comment")
                .isEdited(true)
                .editCount(1)
                .likeCount(5)
                .reactionCount(3)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(commentService.updateComment(eq(testCommentId), any(UpdateCommentRequest.class), eq(testUserId)))
                .thenReturn(updatedResponse);
        
        // When & Then
        mockMvc.perform(put("/api/v1/comments/{commentId}", testCommentId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.commentText").value("This is an updated test comment"))
                .andExpect(jsonPath("$.isEdited").value(true))
                .andExpect(jsonPath("$.editCount").value(1));
        
        verify(commentService).updateComment(eq(testCommentId), any(UpdateCommentRequest.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should delete comment successfully")
    void shouldDeleteCommentSuccessfully() throws Exception {
        // Given
        doNothing().when(commentService).deleteComment(eq(testCommentId), eq(testUserId));
        
        // When & Then
        mockMvc.perform(delete("/api/v1/comments/{commentId}", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isNoContent());
        
        verify(commentService).deleteComment(eq(testCommentId), eq(testUserId));
    }
    
    // =============================================================================
    // Reply Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should create a reply successfully")
    void shouldCreateReplySuccessfully() throws Exception {
        // Given
        Long parentCommentId = 2L;
        CreateCommentRequest request = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is a reply to the comment")
                .mentions(List.of())
                .hashtags(List.of("#reply", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        CommentResponse replyResponse = CommentResponse.builder()
                .id(3L)
                .contentId(testContentId)
                .userId(testUserId)
                .parentCommentId(parentCommentId)
                .commentText("This is a reply to the comment")
                .likeCount(2)
                .reactionCount(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(commentService.createReply(eq(parentCommentId), any(CreateCommentRequest.class), eq(testUserId)))
                .thenReturn(replyResponse);
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments/{parentCommentId}/replies", parentCommentId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$.commentText").value("This is a reply to the comment"))
                .andExpect(jsonPath("$.likeCount").value(2))
                .andExpect(jsonPath("$.reactionCount").value(1));
        
        verify(commentService).createReply(eq(parentCommentId), any(CreateCommentRequest.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get replies for comment successfully")
    void shouldGetRepliesForCommentSuccessfully() throws Exception {
        // Given
        Long parentCommentId = 2L;
        CommentResponse replyResponse = CommentResponse.builder()
                .id(3L)
                .contentId(testContentId)
                .userId(testUserId)
                .parentCommentId(parentCommentId)
                .commentText("This is a reply")
                .likeCount(2)
                .reactionCount(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Page<CommentResponse> replyPage = new PageImpl<>(List.of(replyResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getRepliesForComment(eq(parentCommentId), any(Pageable.class), eq(testUserId)))
                .thenReturn(replyPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/{parentCommentId}/replies", parentCommentId)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(3L))
                .andExpect(jsonPath("$.content[0].parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$.content[0].commentText").value("This is a reply"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getRepliesForComment(eq(parentCommentId), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comment thread successfully")
    void shouldGetCommentThreadSuccessfully() throws Exception {
        // Given
        CommentResponse replyResponse = CommentResponse.builder()
                .id(3L)
                .contentId(testContentId)
                .userId(testUserId)
                .parentCommentId(testCommentId)
                .commentText("This is a reply")
                .likeCount(2)
                .reactionCount(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        CommentResponse threadResponse = CommentResponse.builder()
                .id(testCommentId)
                .contentId(testContentId)
                .userId(testUserId)
                .commentText("This is a test comment")
                .likeCount(5)
                .reactionCount(3)
                .replies(List.of(replyResponse))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(commentService.getCommentThread(eq(testCommentId), eq(testUserId)))
                .thenReturn(threadResponse);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/{commentId}/thread", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCommentId))
                .andExpect(jsonPath("$.commentText").value("This is a test comment"))
                .andExpect(jsonPath("$.replies").isArray())
                .andExpect(jsonPath("$.replies[0].id").value(3L))
                .andExpect(jsonPath("$.replies[0].parentCommentId").value(testCommentId))
                .andExpect(jsonPath("$.replies[0].commentText").value("This is a reply"));
        
        verify(commentService).getCommentThread(eq(testCommentId), eq(testUserId));
    }
    
    // =============================================================================
    // Comment Interactions Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should like comment successfully")
    void shouldLikeCommentSuccessfully() throws Exception {
        // Given
        doNothing().when(commentService).likeComment(eq(testCommentId), eq(testUserId));
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments/{commentId}/like", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk());
        
        verify(commentService).likeComment(eq(testCommentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should unlike comment successfully")
    void shouldUnlikeCommentSuccessfully() throws Exception {
        // Given
        doNothing().when(commentService).unlikeComment(eq(testCommentId), eq(testUserId));
        
        // When & Then
        mockMvc.perform(delete("/api/v1/comments/{commentId}/like", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk());
        
        verify(commentService).unlikeComment(eq(testCommentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should check if user liked comment successfully")
    void shouldCheckIfUserLikedCommentSuccessfully() throws Exception {
        // Given
        when(commentService.hasUserLikedComment(eq(testCommentId), eq(testUserId)))
                .thenReturn(true);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/{commentId}/liked", testCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        verify(commentService).hasUserLikedComment(eq(testCommentId), eq(testUserId));
    }
    
    // =============================================================================
    // Family Context Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get comments by family member successfully")
    void shouldGetCommentsByFamilyMemberSuccessfully() throws Exception {
        // Given
        UUID familyMemberId = UUID.randomUUID();
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsByFamilyMember(eq(familyMemberId), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/family-member/{familyMemberId}", familyMemberId)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getCommentsByFamilyMember(eq(familyMemberId), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comments by generation successfully")
    void shouldGetCommentsByGenerationSuccessfully() throws Exception {
        // Given
        Integer generationLevel = 2;
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsByGeneration(eq(generationLevel), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/generation/{generationLevel}", generationLevel)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.content[0].generationLevel").value(2))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getCommentsByGeneration(eq(generationLevel), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comments by cultural context successfully")
    void shouldGetCommentsByCulturalContextSuccessfully() throws Exception {
        // Given
        String culturalTag = "tradition";
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsByCulturalContext(eq(culturalTag), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/cultural/{culturalTag}", culturalTag)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.content[0].culturalTags").isArray())
                .andExpect(jsonPath("$.content[0].culturalTags[0]").value("tradition"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getCommentsByCulturalContext(eq(culturalTag), any(Pageable.class), eq(testUserId));
    }
    
    // =============================================================================
    // Search and Discovery Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should search comments successfully")
    void shouldSearchCommentsSuccessfully() throws Exception {
        // Given
        String searchText = "test comment";
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.searchComments(eq(searchText), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/search")
                        .header("X-User-ID", testUserId.toString())
                        .param("searchText", searchText)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.content[0].commentText").value("This is a test comment"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).searchComments(eq(searchText), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get comments by hashtag successfully")
    void shouldGetCommentsByHashtagSuccessfully() throws Exception {
        // Given
        String hashtag = "family";
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsByHashtag(eq(hashtag), any(Pageable.class), eq(testUserId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/hashtag/{hashtag}", hashtag)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.content[0].hashtags").isArray())
                .andExpect(jsonPath("$.content[0].hashtags[1]").value("#family"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getCommentsByHashtag(eq(hashtag), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get trending hashtags successfully")
    void shouldGetTrendingHashtagsSuccessfully() throws Exception {
        // Given
        List<String> trendingHashtags = List.of("#family", "#tradition", "#heritage", "#love", "#memories");
        
        when(commentService.getTrendingHashtags(eq(10), eq(testUserId)))
                .thenReturn(trendingHashtags);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/trending/hashtags")
                        .header("X-User-ID", testUserId.toString())
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("#family"))
                .andExpect(jsonPath("$[1]").value("#tradition"))
                .andExpect(jsonPath("$[2]").value("#heritage"))
                .andExpect(jsonPath("$[3]").value("#love"))
                .andExpect(jsonPath("$[4]").value("#memories"));
        
        verify(commentService).getTrendingHashtags(eq(10), eq(testUserId));
    }
    
    // =============================================================================
    // Moderation Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should moderate comment successfully")
    void shouldModerateCommentSuccessfully() throws Exception {
        // Given
        UUID moderatorId = UUID.randomUUID();
        boolean approved = true;
        String reason = "Content is appropriate for family";
        
        doNothing().when(commentService).moderateComment(eq(testCommentId), eq(approved), eq(moderatorId), eq(reason));
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments/{commentId}/moderate", testCommentId)
                        .header("X-User-ID", moderatorId.toString())
                        .param("approved", String.valueOf(approved))
                        .param("reason", reason))
                .andExpect(status().isOk());
        
        verify(commentService).moderateComment(eq(testCommentId), eq(approved), eq(moderatorId), eq(reason));
    }
    
    @Test
    @DisplayName("Should flag comment successfully")
    void shouldFlagCommentSuccessfully() throws Exception {
        // Given
        String reason = "Inappropriate content";
        
        doNothing().when(commentService).flagComment(eq(testCommentId), eq(testUserId), eq(reason));
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments/{commentId}/flag", testCommentId)
                        .header("X-User-ID", testUserId.toString())
                        .param("reason", reason))
                .andExpect(status().isOk());
        
        verify(commentService).flagComment(eq(testCommentId), eq(testUserId), eq(reason));
    }
    
    @Test
    @DisplayName("Should get comments pending moderation successfully")
    void shouldGetCommentsPendingModerationSuccessfully() throws Exception {
        // Given
        UUID moderatorId = UUID.randomUUID();
        Page<CommentResponse> commentPage = new PageImpl<>(List.of(testCommentResponse), PageRequest.of(0, 20), 1);
        
        when(commentService.getCommentsPendingModeration(any(Pageable.class), eq(moderatorId)))
                .thenReturn(commentPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/moderation/pending")
                        .header("X-User-ID", moderatorId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testCommentId))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(commentService).getCommentsPendingModeration(any(Pageable.class), eq(moderatorId));
    }
    
    // =============================================================================
    // Analytics and Insights Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get comment statistics successfully")
    void shouldGetCommentStatisticsSuccessfully() throws Exception {
        // Given
        CommentService.CommentStatistics statistics = new CommentService.CommentStatistics(
                10L, 5L, 25L, 15L, 0.8, 
                List.of("#family", "#tradition"), 
                List.of("@user1", "@user2")
        );
        
        when(commentService.getCommentStatistics(eq(testContentId), eq(testUserId)))
                .thenReturn(statistics);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/content/{contentId}/statistics", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalComments").value(10))
                .andExpect(jsonPath("$.totalReplies").value(5))
                .andExpect(jsonPath("$.totalLikes").value(25))
                .andExpect(jsonPath("$.totalReactions").value(15))
                .andExpect(jsonPath("$.averageSentiment").value(0.8))
                .andExpect(jsonPath("$.topHashtags").isArray())
                .andExpect(jsonPath("$.topHashtags[0]").value("#family"))
                .andExpect(jsonPath("$.topHashtags[1]").value("#tradition"))
                .andExpect(jsonPath("$.topMentions").isArray())
                .andExpect(jsonPath("$.topMentions[0]").value("@user1"))
                .andExpect(jsonPath("$.topMentions[1]").value("@user2"));
        
        verify(commentService).getCommentStatistics(eq(testContentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get family comment activity successfully")
    void shouldGetFamilyCommentActivitySuccessfully() throws Exception {
        // Given
        CommentService.GenerationActivity generationActivity = new CommentService.GenerationActivity(
                2, "Generation 2", 15L, 5L
        );
        
        CommentService.CulturalActivity culturalActivity = new CommentService.CulturalActivity(
                "tradition", "Tradition", 8L, 3L
        );
        
        CommentService.FamilyCommentActivity activity = new CommentService.FamilyCommentActivity(
                25L, 10L, List.of(generationActivity), List.of(culturalActivity)
        );
        
        when(commentService.getFamilyCommentActivity(eq(testFamilyId), eq(testUserId)))
                .thenReturn(activity);
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/family/{familyId}/activity", testFamilyId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalComments").value(25))
                .andExpect(jsonPath("$.activeCommenters").value(10))
                .andExpect(jsonPath("$.generationActivity").isArray())
                .andExpect(jsonPath("$.generationActivity[0].generationLevel").value(2))
                .andExpect(jsonPath("$.generationActivity[0].generationName").value("Generation 2"))
                .andExpect(jsonPath("$.generationActivity[0].commentCount").value(15))
                .andExpect(jsonPath("$.generationActivity[0].activeMembers").value(5))
                .andExpect(jsonPath("$.culturalActivity").isArray())
                .andExpect(jsonPath("$.culturalActivity[0].culturalTag").value("tradition"))
                .andExpect(jsonPath("$.culturalActivity[0].displayName").value("Tradition"))
                .andExpect(jsonPath("$.culturalActivity[0].commentCount").value(8))
                .andExpect(jsonPath("$.culturalActivity[0].uniqueCommenters").value(3));
        
        verify(commentService).getFamilyCommentActivity(eq(testFamilyId), eq(testUserId));
    }
    
    // =============================================================================
    // Validation Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should return validation error for invalid comment text")
    void shouldReturnValidationErrorForInvalidCommentText() throws Exception {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("") // Empty comment text
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.commentText").exists());
    }
    
    @Test
    @DisplayName("Should return validation error for missing content ID")
    void shouldReturnValidationErrorForMissingContentId() throws Exception {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
                .commentText("This is a test comment")
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.contentId").exists());
    }
    
    @Test
    @DisplayName("Should return validation error for comment text too long")
    void shouldReturnValidationErrorForCommentTextTooLong() throws Exception {
        // Given
        String longCommentText = "a".repeat(2001); // Exceeds 2000 character limit
        CreateCommentRequest request = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText(longCommentText)
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.commentText").exists());
    }
}
