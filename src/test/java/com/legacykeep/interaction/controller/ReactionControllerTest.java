package com.legacykeep.interaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacykeep.interaction.dto.request.CreateReactionRequest;
import com.legacykeep.interaction.dto.request.UpdateReactionRequest;
import com.legacykeep.interaction.dto.response.ReactionResponse;
import com.legacykeep.interaction.dto.response.ReactionSummaryResponse;
import com.legacykeep.interaction.entity.Reaction;
import com.legacykeep.interaction.service.ReactionService;
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
 * Reaction Controller Test
 * 
 * Comprehensive tests for the Reaction Controller API endpoints.
 * Tests all CRUD operations, analytics, and family context features.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@WebMvcTest(ReactionController.class)
@DisplayName("Reaction Controller Tests")
class ReactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ReactionService reactionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID testUserId;
    private UUID testContentId;
    private UUID testFamilyId;
    private Long testReactionId;
    private ReactionResponse testReactionResponse;
    
    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testContentId = UUID.randomUUID();
        testFamilyId = UUID.randomUUID();
        testReactionId = 1L;
        
        testReactionResponse = ReactionResponse.builder()
                .id(testReactionId)
                .contentId(testContentId)
                .userId(testUserId)
                .userName("Test User")
                .userAvatar("test-avatar.jpg")
                .reactionType(Reaction.ReactionType.LOVE)
                .reactionTypeName("Love")
                .reactionIcon("❤️")
                .reactionColor("#E91E63")
                .reactionCategory("CORE")
                .intensity(4)
                .intensityDescription("High")
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .generationLevel(2)
                .generationName("Generation 2")
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"test\"}")
                .createdAt(LocalDateTime.now())
                .familyMemberName("Test Family Member")
                .relationshipToUser("Sibling")
                .isFromSameGeneration(true)
                .isFromSameFamily(true)
                .culturalDisplayName("Traditional")
                .emotionalDisplayName("Positive")
                .isFamilySpecific(false)
                .isGenerational(false)
                .isCultural(false)
                .isCore(true)
                .isHighIntensity(true)
                .isLowIntensity(false)
                .intensityLevel("HIGH")
                .build();
    }
    
    // =============================================================================
    // Reaction CRUD Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should add reaction successfully")
    void shouldAddReactionSuccessfully() throws Exception {
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
                .metadata("{\"source\":\"test\"}")
                .build();
        
        when(reactionService.addReaction(any(CreateReactionRequest.class), eq(testUserId)))
                .thenReturn(testReactionResponse);
        
        // When & Then
        mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testReactionId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.reactionType").value("LOVE"))
                .andExpect(jsonPath("$.reactionTypeName").value("Love"))
                .andExpect(jsonPath("$.reactionIcon").value("❤️"))
                .andExpect(jsonPath("$.intensity").value(4))
                .andExpect(jsonPath("$.intensityDescription").value("High"))
                .andExpect(jsonPath("$.generationLevel").value(2))
                .andExpect(jsonPath("$.isHighIntensity").value(true))
                .andExpect(jsonPath("$.intensityLevel").value("HIGH"));
        
        verify(reactionService).addReaction(any(CreateReactionRequest.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should update reaction successfully")
    void shouldUpdateReactionSuccessfully() throws Exception {
        // Given
        UpdateReactionRequest request = UpdateReactionRequest.builder()
                .reactionType(Reaction.ReactionType.BLESSING)
                .intensity(5)
                .familyContext("{\"familyId\":\"" + testFamilyId + "\"}")
                .relationshipContext("{\"relationship\":\"sibling\"}")
                .culturalContext("{\"cultural\":\"traditional\"}")
                .emotionalContext("{\"emotional\":\"positive\"}")
                .isAnonymous(false)
                .isPrivate(false)
                .metadata("{\"source\":\"test\",\"updated\":true}")
                .build();
        
        ReactionResponse updatedResponse = ReactionResponse.builder()
                .id(testReactionId)
                .contentId(testContentId)
                .userId(testUserId)
                .reactionType(Reaction.ReactionType.BLESSING)
                .reactionTypeName("Blessing")
                .reactionIcon("🙏")
                .intensity(5)
                .intensityDescription("Very High")
                .isHighIntensity(true)
                .intensityLevel("HIGH")
                .createdAt(LocalDateTime.now())
                .build();
        
        when(reactionService.updateReaction(eq(testReactionId), any(UpdateReactionRequest.class), eq(testUserId)))
                .thenReturn(updatedResponse);
        
        // When & Then
        mockMvc.perform(put("/api/v1/reactions/{reactionId}", testReactionId)
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testReactionId))
                .andExpect(jsonPath("$.reactionType").value("BLESSING"))
                .andExpect(jsonPath("$.reactionTypeName").value("Blessing"))
                .andExpect(jsonPath("$.reactionIcon").value("🙏"))
                .andExpect(jsonPath("$.intensity").value(5))
                .andExpect(jsonPath("$.intensityDescription").value("Very High"));
        
        verify(reactionService).updateReaction(eq(testReactionId), any(UpdateReactionRequest.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should remove reaction successfully")
    void shouldRemoveReactionSuccessfully() throws Exception {
        // Given
        doNothing().when(reactionService).removeReaction(eq(testReactionId), eq(testUserId));
        
        // When & Then
        mockMvc.perform(delete("/api/v1/reactions/{reactionId}", testReactionId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isNoContent());
        
        verify(reactionService).removeReaction(eq(testReactionId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get reactions for content successfully")
    void shouldGetReactionsForContentSuccessfully() throws Exception {
        // Given
        Page<ReactionResponse> reactionPage = new PageImpl<>(List.of(testReactionResponse), PageRequest.of(0, 20), 1);
        
        when(reactionService.getReactionsForContent(eq(testContentId), any(Pageable.class), eq(testUserId)))
                .thenReturn(reactionPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}", testContentId)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testReactionId))
                .andExpect(jsonPath("$.content[0].contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.content[0].reactionType").value("LOVE"))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(reactionService).getReactionsForContent(eq(testContentId), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get reaction by ID successfully")
    void shouldGetReactionByIdSuccessfully() throws Exception {
        // Given
        when(reactionService.getReactionById(eq(testReactionId), eq(testUserId)))
                .thenReturn(testReactionResponse);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/{reactionId}", testReactionId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testReactionId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.reactionType").value("LOVE"))
                .andExpect(jsonPath("$.intensity").value(4));
        
        verify(reactionService).getReactionById(eq(testReactionId), eq(testUserId));
    }
    
    // =============================================================================
    // Reaction Summary and Analytics Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get reaction summary successfully")
    void shouldGetReactionSummarySuccessfully() throws Exception {
        // Given
        ReactionSummaryResponse.ReactionTypeCount reactionTypeCount = ReactionSummaryResponse.ReactionTypeCount.builder()
                .reactionType("LOVE")
                .displayName("Love")
                .icon("❤️")
                .colorCode("#E91E63")
                .category("CORE")
                .count(15L)
                .percentage(60.0)
                .isFamilySpecific(false)
                .isGenerational(false)
                .isCultural(false)
                .isCore(true)
                .build();
        
        ReactionSummaryResponse.IntensityDistribution intensityDistribution = ReactionSummaryResponse.IntensityDistribution.builder()
                .intensity(4)
                .description("High")
                .count(10L)
                .percentage(40.0)
                .build();
        
        ReactionSummaryResponse summary = ReactionSummaryResponse.builder()
                .contentId(testContentId)
                .totalReactions(25L)
                .uniqueReactors(15L)
                .averageIntensity(3.5)
                .reactionTypeCounts(List.of(reactionTypeCount))
                .intensityDistribution(List.of(intensityDistribution))
                .generationReactionCounts(List.of())
                .culturalReactionCounts(List.of())
                .familyReactionCounts(List.of())
                .breakdown(null)
                .familyContext(null)
                .culturalContext(null)
                .emotionalContext(null)
                .userId(testUserId)
                .hasUserReacted(true)
                .userReactionType("LOVE")
                .userReactionIntensity(4)
                .userReactionCreatedAt(LocalDateTime.now())
                .build();
        
        when(reactionService.getReactionSummary(eq(testContentId), eq(testUserId)))
                .thenReturn(summary);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}/summary", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.totalReactions").value(25))
                .andExpect(jsonPath("$.uniqueReactors").value(15))
                .andExpect(jsonPath("$.averageIntensity").value(3.5))
                .andExpect(jsonPath("$.reactionTypeCounts").isArray())
                .andExpect(jsonPath("$.reactionTypeCounts[0].reactionType").value("LOVE"))
                .andExpect(jsonPath("$.reactionTypeCounts[0].count").value(15))
                .andExpect(jsonPath("$.reactionTypeCounts[0].percentage").value(60.0))
                .andExpect(jsonPath("$.intensityDistribution").isArray())
                .andExpect(jsonPath("$.intensityDistribution[0].intensity").value(4))
                .andExpect(jsonPath("$.intensityDistribution[0].count").value(10))
                .andExpect(jsonPath("$.hasUserReacted").value(true))
                .andExpect(jsonPath("$.userReactionType").value("LOVE"))
                .andExpect(jsonPath("$.userReactionIntensity").value(4));
        
        verify(reactionService).getReactionSummary(eq(testContentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get reaction breakdown successfully")
    void shouldGetReactionBreakdownSuccessfully() throws Exception {
        // Given
        ReactionService.ReactionTypeCount reactionTypeCount = new ReactionService.ReactionTypeCount(
                "LOVE", "Love", "❤️", 15L, 60.0
        );
        
        ReactionService.IntensityDistribution intensityDistribution = new ReactionService.IntensityDistribution(
                4, 10L, 40.0
        );
        
        ReactionService.GenerationReactionCount generationReactionCount = new ReactionService.GenerationReactionCount(
                2, "Generation 2", 8L, 32.0
        );
        
        ReactionService.ReactionBreakdown breakdown = new ReactionService.ReactionBreakdown(
                25L, List.of(reactionTypeCount), List.of(intensityDistribution), List.of(generationReactionCount)
        );
        
        when(reactionService.getReactionBreakdown(eq(testContentId), eq(testUserId)))
                .thenReturn(breakdown);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}/breakdown", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReactions").value(25))
                .andExpect(jsonPath("$.reactionTypeCounts").isArray())
                .andExpect(jsonPath("$.reactionTypeCounts[0].reactionType").value("LOVE"))
                .andExpect(jsonPath("$.reactionTypeCounts[0].count").value(15))
                .andExpect(jsonPath("$.reactionTypeCounts[0].percentage").value(60.0))
                .andExpect(jsonPath("$.intensityDistribution").isArray())
                .andExpect(jsonPath("$.intensityDistribution[0].intensity").value(4))
                .andExpect(jsonPath("$.intensityDistribution[0].count").value(10))
                .andExpect(jsonPath("$.generationReactionCounts").isArray())
                .andExpect(jsonPath("$.generationReactionCounts[0].generationLevel").value(2))
                .andExpect(jsonPath("$.generationReactionCounts[0].reactionCount").value(8));
        
        verify(reactionService).getReactionBreakdown(eq(testContentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get user reaction successfully")
    void shouldGetUserReactionSuccessfully() throws Exception {
        // Given
        when(reactionService.getUserReaction(eq(testContentId), eq(testUserId)))
                .thenReturn(testReactionResponse);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}/user", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testReactionId))
                .andExpect(jsonPath("$.contentId").value(testContentId.toString()))
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.reactionType").value("LOVE"))
                .andExpect(jsonPath("$.intensity").value(4));
        
        verify(reactionService).getUserReaction(eq(testContentId), eq(testUserId));
    }
    
    // =============================================================================
    // Family Context Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get reactions by family member successfully")
    void shouldGetReactionsByFamilyMemberSuccessfully() throws Exception {
        // Given
        UUID familyMemberId = UUID.randomUUID();
        Page<ReactionResponse> reactionPage = new PageImpl<>(List.of(testReactionResponse), PageRequest.of(0, 20), 1);
        
        when(reactionService.getReactionsByFamilyMember(eq(familyMemberId), any(Pageable.class), eq(testUserId)))
                .thenReturn(reactionPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/family-member/{familyMemberId}", familyMemberId)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testReactionId))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(reactionService).getReactionsByFamilyMember(eq(familyMemberId), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get reactions by generation successfully")
    void shouldGetReactionsByGenerationSuccessfully() throws Exception {
        // Given
        Integer generationLevel = 2;
        Page<ReactionResponse> reactionPage = new PageImpl<>(List.of(testReactionResponse), PageRequest.of(0, 20), 1);
        
        when(reactionService.getReactionsByGeneration(eq(generationLevel), any(Pageable.class), eq(testUserId)))
                .thenReturn(reactionPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/generation/{generationLevel}", generationLevel)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testReactionId))
                .andExpect(jsonPath("$.content[0].generationLevel").value(2))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(reactionService).getReactionsByGeneration(eq(generationLevel), any(Pageable.class), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get reactions by cultural context successfully")
    void shouldGetReactionsByCulturalContextSuccessfully() throws Exception {
        // Given
        String culturalTag = "tradition";
        Page<ReactionResponse> reactionPage = new PageImpl<>(List.of(testReactionResponse), PageRequest.of(0, 20), 1);
        
        when(reactionService.getReactionsByCulturalContext(eq(culturalTag), any(Pageable.class), eq(testUserId)))
                .thenReturn(reactionPage);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/cultural/{culturalTag}", culturalTag)
                        .header("X-User-ID", testUserId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testReactionId))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        verify(reactionService).getReactionsByCulturalContext(eq(culturalTag), any(Pageable.class), eq(testUserId));
    }
    
    // =============================================================================
    // Reaction Type Operations Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get available reaction types successfully")
    void shouldGetAvailableReactionTypesSuccessfully() throws Exception {
        // Given
        ReactionService.ReactionTypeInfo reactionTypeInfo = new ReactionService.ReactionTypeInfo(
                "LOVE", "Love", "❤️", "#E91E63", "CORE", "Deep emotional connection",
                false, false, false
        );
        
        List<ReactionService.ReactionTypeInfo> reactionTypes = List.of(reactionTypeInfo);
        
        when(reactionService.getAvailableReactionTypes(eq(testUserId)))
                .thenReturn(reactionTypes);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/types")
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].typeName").value("LOVE"))
                .andExpect(jsonPath("$[0].displayName").value("Love"))
                .andExpect(jsonPath("$[0].icon").value("❤️"))
                .andExpect(jsonPath("$[0].colorCode").value("#E91E63"))
                .andExpect(jsonPath("$[0].category").value("CORE"))
                .andExpect(jsonPath("$[0].description").value("Deep emotional connection"));
        
        verify(reactionService).getAvailableReactionTypes(eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get family reaction types successfully")
    void shouldGetFamilyReactionTypesSuccessfully() throws Exception {
        // Given
        ReactionService.ReactionTypeInfo familyReactionType = new ReactionService.ReactionTypeInfo(
                "BLESSING", "Blessing", "🙏", "#8BC34A", "FAMILY", "Traditional family blessing",
                true, false, false
        );
        
        List<ReactionService.ReactionTypeInfo> familyReactionTypes = List.of(familyReactionType);
        
        when(reactionService.getFamilyReactionTypes(eq(testUserId)))
                .thenReturn(familyReactionTypes);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/types/family")
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].typeName").value("BLESSING"))
                .andExpect(jsonPath("$[0].displayName").value("Blessing"))
                .andExpect(jsonPath("$[0].icon").value("🙏"))
                .andExpect(jsonPath("$[0].isFamilySpecific").value(true));
        
        verify(reactionService).getFamilyReactionTypes(eq(testUserId));
    }
    
    // =============================================================================
    // Analytics and Insights Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should get reaction statistics successfully")
    void shouldGetReactionStatisticsSuccessfully() throws Exception {
        // Given
        ReactionService.ReactionTypeCount reactionTypeCount = new ReactionService.ReactionTypeCount(
                "LOVE", "Love", "❤️", 15L, 60.0
        );
        
        ReactionService.IntensityDistribution intensityDistribution = new ReactionService.IntensityDistribution(
                4, 10L, 40.0
        );
        
        ReactionService.ReactionStatistics statistics = new ReactionService.ReactionStatistics(
                25L, 15L, 3.5, List.of(reactionTypeCount), List.of(intensityDistribution)
        );
        
        when(reactionService.getReactionStatistics(eq(testContentId), eq(testUserId)))
                .thenReturn(statistics);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/content/{contentId}/statistics", testContentId)
                        .header("X-User-ID", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReactions").value(25))
                .andExpect(jsonPath("$.uniqueReactors").value(15))
                .andExpect(jsonPath("$.averageIntensity").value(3.5))
                .andExpect(jsonPath("$.reactionTypeCounts").isArray())
                .andExpect(jsonPath("$.reactionTypeCounts[0].reactionType").value("LOVE"))
                .andExpect(jsonPath("$.reactionTypeCounts[0].count").value(15))
                .andExpect(jsonPath("$.intensityDistribution").isArray())
                .andExpect(jsonPath("$.intensityDistribution[0].intensity").value(4));
        
        verify(reactionService).getReactionStatistics(eq(testContentId), eq(testUserId));
    }
    
    @Test
    @DisplayName("Should get trending reactions successfully")
    void shouldGetTrendingReactionsSuccessfully() throws Exception {
        // Given
        ReactionService.TrendingReaction trendingReaction = new ReactionService.TrendingReaction(
                "LOVE", "Love", "❤️", 25L, 15.5
        );
        
        List<ReactionService.TrendingReaction> trendingReactions = List.of(trendingReaction);
        
        when(reactionService.getTrendingReactions(eq(testFamilyId), eq(10), eq(testUserId)))
                .thenReturn(trendingReactions);
        
        // When & Then
        mockMvc.perform(get("/api/v1/reactions/family/{familyId}/trending", testFamilyId)
                        .header("X-User-ID", testUserId.toString())
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reactionType").value("LOVE"))
                .andExpect(jsonPath("$[0].displayName").value("Love"))
                .andExpect(jsonPath("$[0].icon").value("❤️"))
                .andExpect(jsonPath("$[0].count").value(25))
                .andExpect(jsonPath("$[0].trendPercentage").value(15.5));
        
        verify(reactionService).getTrendingReactions(eq(testFamilyId), eq(10), eq(testUserId));
    }
    
    // =============================================================================
    // Validation Tests
    // =============================================================================
    
    @Test
    @DisplayName("Should return validation error for invalid intensity")
    void shouldReturnValidationErrorForInvalidIntensity() throws Exception {
        // Given
        CreateReactionRequest request = CreateReactionRequest.builder()
                .contentId(testContentId)
                .reactionType(Reaction.ReactionType.LOVE)
                .intensity(6) // Invalid intensity (should be 1-5)
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.intensity").exists());
    }
    
    @Test
    @DisplayName("Should return validation error for missing content ID")
    void shouldReturnValidationErrorForMissingContentId() throws Exception {
        // Given
        CreateReactionRequest request = CreateReactionRequest.builder()
                .reactionType(Reaction.ReactionType.LOVE)
                .intensity(4)
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/reactions")
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
    @DisplayName("Should return validation error for missing reaction type")
    void shouldReturnValidationErrorForMissingReactionType() throws Exception {
        // Given
        CreateReactionRequest request = CreateReactionRequest.builder()
                .contentId(testContentId)
                .intensity(4)
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/v1/reactions")
                        .header("X-User-ID", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.validationErrors.reactionType").exists());
    }
}
