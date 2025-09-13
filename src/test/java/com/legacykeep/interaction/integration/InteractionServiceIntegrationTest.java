package com.legacykeep.interaction.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacykeep.interaction.dto.request.CreateCommentRequest;
import com.legacykeep.interaction.dto.request.CreateReactionRequest;
import com.legacykeep.interaction.entity.Comment;
import com.legacykeep.interaction.entity.Reaction;
import com.legacykeep.interaction.repository.CommentRepository;
import com.legacykeep.interaction.repository.ReactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Interaction Service Integration Test
 * 
 * Comprehensive integration tests for the Interaction Service.
 * Tests the complete flow from API endpoints to database operations.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Interaction Service Integration Tests")
class InteractionServiceIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ReactionRepository reactionRepository;
    
    private UUID testUserId;
    private UUID testContentId;
    private UUID testFamilyId;
    
    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testContentId = UUID.randomUUID();
        testFamilyId = UUID.randomUUID();
        
        // Clean up test data
        commentRepository.deleteAll();
        reactionRepository.deleteAll();
    }
    
    // =============================================================================
    // Comment Integration Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should create and retrieve comment successfully")
    void shouldCreateAndRetrieveCommentSuccessfully() throws Exception {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is an integration test comment")
                .mentions(List.of())
                .hashtags(List.of("#integration", "#test", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition", "heritage"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"integration-test\"}")
                .build();
        
        // When - Create comment
        String createResponse = mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.commentText").value("This is an integration test comment"))
                .andExpect(jsonPath("$.hashtags").isArray())
                .andExpect(jsonPath("$.hashtags[0]").value("#integration"))
                .andExpect(jsonPath("$.hashtags[1]").value("#test"))
                .andExpect(jsonPath("$.hashtags[2]").value("#family"))
                .andExpect(jsonPath("$.culturalTags").isArray())
                .andExpect(jsonPath("$.culturalTags[0]").value("tradition"))
                .andExpect(jsonPath("$.culturalTags[1]").value("heritage"))
                .andExpect(jsonPath("$.generationLevel").value(2))
                .andExpect(jsonPath("$.generationName").value("Generation 2"))
                .andExpect(jsonPath("$.isFromSameGeneration").value(true))
                .andExpect(jsonPath("$.isFromSameFamily").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extract comment ID from response
        Long commentId = objectMapper.readTree(createResponse).get("id").asLong();
        
        // Then - Retrieve comment
        mockMvc.perform(get("/api/v1/comments/{commentId}", commentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.commentText").value("This is an integration test comment"))
                .andExpect(jsonPath("$.hashtags").isArray())
                .andExpect(jsonPath("$.culturalTags").isArray())
                .andExpect(jsonPath("$.generationLevel").value(2));
        
        // Verify comment was saved in database
        Comment savedComment = commentRepository.findById(commentId).orElse(null);
        assert savedComment != null;
        assert savedComment.getContentId().equals(testContentId);
        assert savedComment.getUserId().equals(testUserId);
        assert savedComment.getCommentText().equals("This is an integration test comment");
        assert savedComment.getGenerationLevel().equals(2);
    }
    
    @Test
    @DisplayName("Should create reply and retrieve comment thread successfully")
    void shouldCreateReplyAndRetrieveCommentThreadSuccessfully() throws Exception {
        // Given - Create parent comment
        CreateCommentRequest parentRequest = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is a parent comment")
                .mentions(List.of())
                .hashtags(List.of("#parent", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        String parentResponse = mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Long parentCommentId = objectMapper.readTree(parentResponse).get("id").asLong();
        
        // When - Create reply
        CreateCommentRequest replyRequest = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is a reply to the parent comment")
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
        
        String replyResponse = mockMvc.perform(post("/api/v1/comments/{parentCommentId}/replies", parentCommentId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$.commentText").value("This is a reply to the parent comment"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Long replyCommentId = objectMapper.readTree(replyResponse).get("id").asLong();
        
        // Then - Retrieve comment thread
        mockMvc.perform(get("/api/v1/comments/{commentId}/thread", parentCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(parentCommentId))
                .andExpect(jsonPath("$.commentText").value("This is a parent comment"))
                .andExpect(jsonPath("$.replies").isArray())
                .andExpect(jsonPath("$.replies[0].id").value(replyCommentId))
                .andExpect(jsonPath("$.replies[0].parentCommentId").value(parentCommentId))
                .andExpect(jsonPath("$.replies[0].commentText").value("This is a reply to the parent comment"));
        
        // Verify reply was saved in database
        Comment savedReply = commentRepository.findById(replyCommentId).orElse(null);
        assert savedReply != null;
        assert savedReply.getParentComment().getId().equals(parentCommentId);
        assert savedReply.getCommentText().equals("This is a reply to the parent comment");
    }
    
    @Test
    @DisplayName("Should update comment successfully")
    void shouldUpdateCommentSuccessfully() throws Exception {
        // Given - Create comment
        CreateCommentRequest createRequest = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is the original comment")
                .mentions(List.of())
                .hashtags(List.of("#original", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        String createResponse = mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Long commentId = objectMapper.readTree(createResponse).get("id").asLong();
        
        // When - Update comment
        CreateCommentRequest updateRequest = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is the updated comment")
                .mentions(List.of())
                .hashtags(List.of("#updated", "#family"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition", "heritage"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        mockMvc.perform(put("/api/v1/comments/{commentId}", commentId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.commentText").value("This is the updated comment"))
                .andExpect(jsonPath("$.isEdited").value(true))
                .andExpect(jsonPath("$.editCount").value(1));
        
        // Verify comment was updated in database
        Comment updatedComment = commentRepository.findById(commentId).orElse(null);
        assert updatedComment != null;
        assert updatedComment.getCommentText().equals("This is the updated comment");
        assert updatedComment.getIsEdited().equals(true);
        assert updatedComment.getEditCount().equals(1);
    }
    
    // =============================================================================
    // Reaction Integration Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should create and retrieve reaction successfully")
    void shouldCreateAndRetrieveReactionSuccessfully() throws Exception {
        // Given
        CreateReactionRequest request = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.LOVE)
                .intensity(4)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"integration-test\"}")
                .build();
        
        // When - Create reaction
        String createResponse = mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.reactionType").value("LOVE"))
                .andExpect(jsonPath("$.reactionTypeName").value("Love"))
                .andExpect(jsonPath("$.reactionIcon").value("❤️"))
                .andExpect(jsonPath("$.intensity").value(4))
                .andExpect(jsonPath("$.intensityDescription").value("High"))
                .andExpect(jsonPath("$.generationLevel").value(2))
                .andExpect(jsonPath("$.isHighIntensity").value(true))
                .andExpect(jsonPath("$.intensityLevel").value("HIGH"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        // Extract reaction ID from response
        Long reactionId = objectMapper.readTree(createResponse).get("id").asLong();
        
        // Then - Retrieve reaction
        mockMvc.perform(get("/api/v1/reactions/{reactionId}", reactionId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reactionId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.reactionType").value("LOVE"))
                .andExpect(jsonPath("$.intensity").value(4));
        
        // Verify reaction was saved in database
        Reaction savedReaction = reactionRepository.findById(reactionId).orElse(null);
        assert savedReaction != null;
        assert savedReaction.getContentId().equals(testContentId);
        assert savedReaction.getUserId().equals(testUserId);
        assert savedReaction.getReactionType().equals(Reaction.ReactionType.LOVE);
        assert savedReaction.getIntensity().equals(4);
        assert savedReaction.getGenerationLevel().equals(2);
    }
    
    @Test
    @DisplayName("Should update reaction successfully")
    void shouldUpdateReactionSuccessfully() throws Exception {
        // Given - Create reaction
        CreateReactionRequest createRequest = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.LIKE)
                .intensity(2)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        String createResponse = mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Long reactionId = objectMapper.readTree(createResponse).get("id").asLong();
        
        // When - Update reaction
        CreateReactionRequest updateRequest = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.BLESSING)
                .intensity(5)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        mockMvc.perform(put("/api/v1/reactions/{reactionId}", reactionId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reactionId))
                .andExpect(jsonPath("$.reactionType").value("BLESSING"))
                .andExpect(jsonPath("$.reactionTypeName").value("Blessing"))
                .andExpect(jsonPath("$.reactionIcon").value("🙏"))
                .andExpect(jsonPath("$.intensity").value(5))
                .andExpect(jsonPath("$.intensityDescription").value("Very High"));
        
        // Verify reaction was updated in database
        Reaction updatedReaction = reactionRepository.findById(reactionId).orElse(null);
        assert updatedReaction != null;
        assert updatedReaction.getReactionType().equals(Reaction.ReactionType.BLESSING);
        assert updatedReaction.getIntensity().equals(5);
    }
    
    @Test
    @DisplayName("Should get reaction summary successfully")
    void shouldGetReactionSummarySuccessfully() throws Exception {
        // Given - Create multiple reactions
        CreateReactionRequest request1 = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.LOVE)
                .intensity(4)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        CreateReactionRequest request2 = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.BLESSING)
                .intensity(5)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        // Create reactions
        mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());
        
        mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());
        
        // When - Get reaction summary
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}/summary", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.totalReactions").value(2))
                .andExpect(jsonPath("$.uniqueReactors").value(1))
                .andExpect(jsonPath("$.averageIntensity").value(4.5))
                .andExpect(jsonPath("$.reactionTypeCounts").isArray())
                .andExpect(jsonPath("$.intensityDistribution").isArray())
                .andExpect(jsonPath("$.hasUserReacted").value(true));
    }
    
    // =============================================================================
    // Search and Discovery Integration Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should search comments successfully")
    void shouldSearchCommentsSuccessfully() throws Exception {
        // Given - Create comments with searchable text
        CreateCommentRequest request1 = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is a comment about family traditions")
                .mentions(List.of())
                .hashtags(List.of("#family", "#traditions"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("tradition"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        CreateCommentRequest request2 = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("This is another comment about family heritage")
                .mentions(List.of())
                .hashtags(List.of("#family", "#heritage"))
                .mediaUrls(List.of())
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .culturalTags(List.of("heritage"))
                .generationLevel(2)
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .languageCode("en")
                .isAnonymous(false)
                .isPrivate(false)
                .build();
        
        // Create comments
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());
        
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());
        
        // When - Search comments
        mockMvc.perform(get("/api/v1/comments/search")
                        .header("X-User-ID", testUserId.toString())
                        .param("searchText", "family")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(2));
        
        // When - Search by hashtag
        mockMvc.perform(get("/api/v1/comments/hashtag/family")
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(2));
    }
    
    // =============================================================================
    // Error Handling Integration Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should handle validation errors gracefully")
    void shouldHandleValidationErrorsGracefully() throws Exception {
        // Given - Invalid request (empty comment text)
        CreateCommentRequest invalidRequest = CreateCommentRequest.builder()
                .contentId(testContentId)
                .commentText("") // Invalid - empty text
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.commentText").exists());
    }
    
    @Test
    @DisplayName("Should handle not found errors gracefully")
    void shouldHandleNotFoundErrorsGracefully() throws Exception {
        // Given - Non-existent comment ID
        Long nonExistentCommentId = 999L;
        
        // When & Then
        mockMvc.perform(get("/api/v1/comments/{commentId}", nonExistentCommentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETERS"));
    }
}
