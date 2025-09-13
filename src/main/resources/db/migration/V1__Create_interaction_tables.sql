-- =============================================================================
-- Interaction Service Database Migration
-- =============================================================================
-- This migration creates the core tables for the Interaction Service
-- which handles comments, reactions, and other interactions for family legacy content.
-- 
-- Author: LegacyKeep Team
-- Version: 1.0.0
-- =============================================================================

-- =============================================================================
-- Interaction Types Table
-- =============================================================================
-- Stores the configuration and metadata for different types of interactions
CREATE TABLE interaction_types (
    id BIGSERIAL PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(30) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    icon_name VARCHAR(50),
    color_code VARCHAR(7),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    requires_content BOOLEAN NOT NULL DEFAULT TRUE,
    requires_user BOOLEAN NOT NULL DEFAULT TRUE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Comments Table
-- =============================================================================
-- Stores comments and replies with family context and cultural sensitivity
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    parent_comment_id BIGINT REFERENCES comments(id),
    comment_text TEXT NOT NULL,
    mentions JSONB,
    hashtags JSONB,
    media_urls JSONB,
    is_edited BOOLEAN NOT NULL DEFAULT FALSE,
    edit_count INTEGER NOT NULL DEFAULT 0,
    edit_history JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    moderation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    family_context JSONB,
    cultural_tags JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    sentiment_score DOUBLE PRECISION,
    language_code VARCHAR(5),
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    reply_count INTEGER NOT NULL DEFAULT 0,
    like_count INTEGER NOT NULL DEFAULT 0,
    reaction_count INTEGER NOT NULL DEFAULT 0,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Reactions Table
-- =============================================================================
-- Stores user reactions to content with intensity levels and family context
CREATE TABLE reactions (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,
    intensity INTEGER NOT NULL DEFAULT 1 CHECK (intensity >= 1 AND intensity <= 5),
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    cultural_context JSONB,
    emotional_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Ratings Table
-- =============================================================================
-- Stores user ratings for content with family context
CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    rating_value INTEGER NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5),
    rating_text TEXT,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    cultural_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Shares Table
-- =============================================================================
-- Stores content sharing information with family context
CREATE TABLE shares (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    share_type VARCHAR(20) NOT NULL DEFAULT 'SHARE',
    target_user_id UUID,
    target_family_id UUID,
    share_message TEXT,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Bookmarks Table
-- =============================================================================
-- Stores user bookmarks for content with family context
CREATE TABLE bookmarks (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    bookmark_name VARCHAR(100),
    bookmark_description TEXT,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    cultural_context JSONB,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Content Views Table
-- =============================================================================
-- Stores content view tracking for analytics
CREATE TABLE content_views (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    view_duration INTEGER, -- in seconds
    view_completion_percentage DOUBLE PRECISION,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Mentions Table
-- =============================================================================
-- Stores user mentions in content with family context
CREATE TABLE mentions (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    mentioned_user_id UUID NOT NULL,
    mention_type VARCHAR(20) NOT NULL DEFAULT 'MENTION',
    mention_context JSONB,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Polls Table
-- =============================================================================
-- Stores family polls and questions
CREATE TABLE polls (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    poll_question TEXT NOT NULL,
    poll_options JSONB NOT NULL,
    poll_type VARCHAR(20) NOT NULL DEFAULT 'SINGLE_CHOICE',
    poll_duration INTEGER, -- in hours
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    cultural_context JSONB,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Poll Responses Table
-- =============================================================================
-- Stores poll responses with family context
CREATE TABLE poll_responses (
    id BIGSERIAL PRIMARY KEY,
    poll_id BIGINT NOT NULL REFERENCES polls(id),
    user_id UUID NOT NULL,
    response_data JSONB NOT NULL,
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Stories Table
-- =============================================================================
-- Stores temporary family stories and updates
CREATE TABLE stories (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    story_content TEXT,
    story_media_urls JSONB,
    story_type VARCHAR(20) NOT NULL DEFAULT 'STORY',
    story_duration INTEGER NOT NULL DEFAULT 24, -- in hours
    family_context JSONB,
    generation_level INTEGER,
    relationship_context JSONB,
    cultural_context JSONB,
    is_anonymous BOOLEAN NOT NULL DEFAULT FALSE,
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL
);

-- =============================================================================
-- Interaction Analytics Table
-- =============================================================================
-- Stores aggregated interaction analytics for performance
CREATE TABLE interaction_analytics (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    interaction_type VARCHAR(20) NOT NULL,
    total_count BIGINT NOT NULL DEFAULT 0,
    unique_users BIGINT NOT NULL DEFAULT 0,
    generation_breakdown JSONB,
    cultural_breakdown JSONB,
    family_breakdown JSONB,
    time_breakdown JSONB,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- Indexes for Performance
-- =============================================================================

-- Comments indexes
CREATE INDEX idx_comments_content_id ON comments(content_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_parent_comment_id ON comments(parent_comment_id);
CREATE INDEX idx_comments_status ON comments(status);
CREATE INDEX idx_comments_moderation_status ON comments(moderation_status);
CREATE INDEX idx_comments_generation_level ON comments(generation_level);
CREATE INDEX idx_comments_created_at ON comments(created_at);
CREATE INDEX idx_comments_cultural_tags ON comments USING GIN(cultural_tags);
CREATE INDEX idx_comments_hashtags ON comments USING GIN(hashtags);
CREATE INDEX idx_comments_mentions ON comments USING GIN(mentions);

-- Reactions indexes
CREATE INDEX idx_reactions_content_id ON reactions(content_id);
CREATE INDEX idx_reactions_user_id ON reactions(user_id);
CREATE INDEX idx_reactions_reaction_type ON reactions(reaction_type);
CREATE INDEX idx_reactions_generation_level ON reactions(generation_level);
CREATE INDEX idx_reactions_created_at ON reactions(created_at);
CREATE INDEX idx_reactions_cultural_context ON reactions USING GIN(cultural_context);
CREATE INDEX idx_reactions_family_context ON reactions USING GIN(family_context);

-- Ratings indexes
CREATE INDEX idx_ratings_content_id ON ratings(content_id);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);
CREATE INDEX idx_ratings_generation_level ON ratings(generation_level);
CREATE INDEX idx_ratings_created_at ON ratings(created_at);

-- Shares indexes
CREATE INDEX idx_shares_content_id ON shares(content_id);
CREATE INDEX idx_shares_user_id ON shares(user_id);
CREATE INDEX idx_shares_target_user_id ON shares(target_user_id);
CREATE INDEX idx_shares_target_family_id ON shares(target_family_id);
CREATE INDEX idx_shares_created_at ON shares(created_at);

-- Bookmarks indexes
CREATE INDEX idx_bookmarks_content_id ON bookmarks(content_id);
CREATE INDEX idx_bookmarks_user_id ON bookmarks(user_id);
CREATE INDEX idx_bookmarks_generation_level ON bookmarks(generation_level);
CREATE INDEX idx_bookmarks_created_at ON bookmarks(created_at);

-- Content views indexes
CREATE INDEX idx_content_views_content_id ON content_views(content_id);
CREATE INDEX idx_content_views_user_id ON content_views(user_id);
CREATE INDEX idx_content_views_generation_level ON content_views(generation_level);
CREATE INDEX idx_content_views_created_at ON content_views(created_at);

-- Mentions indexes
CREATE INDEX idx_mentions_content_id ON mentions(content_id);
CREATE INDEX idx_mentions_user_id ON mentions(user_id);
CREATE INDEX idx_mentions_mentioned_user_id ON mentions(mentioned_user_id);
CREATE INDEX idx_mentions_created_at ON mentions(created_at);

-- Polls indexes
CREATE INDEX idx_polls_content_id ON polls(content_id);
CREATE INDEX idx_polls_user_id ON polls(user_id);
CREATE INDEX idx_polls_generation_level ON polls(generation_level);
CREATE INDEX idx_polls_created_at ON polls(created_at);

-- Poll responses indexes
CREATE INDEX idx_poll_responses_poll_id ON poll_responses(poll_id);
CREATE INDEX idx_poll_responses_user_id ON poll_responses(user_id);
CREATE INDEX idx_poll_responses_created_at ON poll_responses(created_at);

-- Stories indexes
CREATE INDEX idx_stories_user_id ON stories(user_id);
CREATE INDEX idx_stories_generation_level ON stories(generation_level);
CREATE INDEX idx_stories_created_at ON stories(created_at);
CREATE INDEX idx_stories_expires_at ON stories(expires_at);

-- Interaction analytics indexes
CREATE INDEX idx_interaction_analytics_content_id ON interaction_analytics(content_id);
CREATE INDEX idx_interaction_analytics_interaction_type ON interaction_analytics(interaction_type);
CREATE INDEX idx_interaction_analytics_created_at ON interaction_analytics(created_at);

-- =============================================================================
-- Unique Constraints
-- =============================================================================

-- Ensure one reaction per user per content
CREATE UNIQUE INDEX idx_reactions_user_content_unique ON reactions(content_id, user_id);

-- Ensure one rating per user per content
CREATE UNIQUE INDEX idx_ratings_user_content_unique ON ratings(content_id, user_id);

-- Ensure one bookmark per user per content
CREATE UNIQUE INDEX idx_bookmarks_user_content_unique ON bookmarks(content_id, user_id);

-- Ensure one poll response per user per poll
CREATE UNIQUE INDEX idx_poll_responses_user_poll_unique ON poll_responses(poll_id, user_id);

-- =============================================================================
-- Insert Default Interaction Types
-- =============================================================================

INSERT INTO interaction_types (type_name, category, display_name, icon_name, color_code, is_active, requires_content, requires_user) VALUES
-- Core Engagement Types
('LIKE', 'ENGAGEMENT', 'Like', '👍', '#4CAF50', TRUE, TRUE, TRUE),
('LOVE', 'ENGAGEMENT', 'Love', '❤️', '#E91E63', TRUE, TRUE, TRUE),
('HEART', 'ENGAGEMENT', 'Heart', '💖', '#F06292', TRUE, TRUE, TRUE),
('LAUGH', 'ENGAGEMENT', 'Laugh', '😂', '#FF9800', TRUE, TRUE, TRUE),
('WOW', 'ENGAGEMENT', 'Wow', '😮', '#9C27B0', TRUE, TRUE, TRUE),
('SAD', 'ENGAGEMENT', 'Sad', '😢', '#607D8B', TRUE, TRUE, TRUE),
('ANGRY', 'ENGAGEMENT', 'Angry', '😠', '#F44336', TRUE, TRUE, TRUE),

-- Family-Specific Types
('BLESSING', 'FAMILY', 'Blessing', '🙏', '#8BC34A', TRUE, TRUE, TRUE),
('PRIDE', 'FAMILY', 'Pride', '🏆', '#FFC107', TRUE, TRUE, TRUE),
('GRATITUDE', 'FAMILY', 'Gratitude', '🙏', '#4CAF50', TRUE, TRUE, TRUE),
('MEMORY', 'FAMILY', 'Memory', '🧠', '#9E9E9E', TRUE, TRUE, TRUE),
('WISDOM', 'FAMILY', 'Wisdom', '🧙‍♂️', '#795548', TRUE, TRUE, TRUE),
('TRADITION', 'FAMILY', 'Tradition', '🏛️', '#3F51B5', TRUE, TRUE, TRUE),

-- Content Interaction Types
('COMMENT', 'CONTENT', 'Comment', '💬', '#2196F3', TRUE, TRUE, TRUE),
('REPLY', 'CONTENT', 'Reply', '↩️', '#2196F3', TRUE, TRUE, TRUE),
('SHARE', 'CONTENT', 'Share', '📤', '#00BCD4', TRUE, TRUE, TRUE),
('BOOKMARK', 'CONTENT', 'Bookmark', '🔖', '#FF5722', TRUE, TRUE, TRUE),
('RATING', 'CONTENT', 'Rating', '⭐', '#FF9800', TRUE, TRUE, TRUE),
('VIEW', 'CONTENT', 'View', '👁️', '#607D8B', TRUE, TRUE, TRUE),

-- Social Interaction Types
('MENTION', 'SOCIAL', 'Mention', '@', '#9C27B0', TRUE, TRUE, TRUE),
('TAG', 'SOCIAL', 'Tag', '#', '#607D8B', TRUE, TRUE, TRUE),
('POLL', 'SOCIAL', 'Poll', '📊', '#4CAF50', TRUE, TRUE, TRUE),
('STORY', 'SOCIAL', 'Story', '📱', '#E91E63', TRUE, FALSE, TRUE),
('HIGHLIGHT', 'SOCIAL', 'Highlight', '⭐', '#FFC107', TRUE, FALSE, TRUE);

-- =============================================================================
-- Comments
-- =============================================================================
COMMENT ON TABLE interaction_types IS 'Configuration and metadata for different types of interactions';
COMMENT ON TABLE comments IS 'Comments and replies with family context and cultural sensitivity';
COMMENT ON TABLE reactions IS 'User reactions to content with intensity levels and family context';
COMMENT ON TABLE ratings IS 'User ratings for content with family context';
COMMENT ON TABLE shares IS 'Content sharing information with family context';
COMMENT ON TABLE bookmarks IS 'User bookmarks for content with family context';
COMMENT ON TABLE content_views IS 'Content view tracking for analytics';
COMMENT ON TABLE mentions IS 'User mentions in content with family context';
COMMENT ON TABLE polls IS 'Family polls and questions';
COMMENT ON TABLE poll_responses IS 'Poll responses with family context';
COMMENT ON TABLE stories IS 'Temporary family stories and updates';
COMMENT ON TABLE interaction_analytics IS 'Aggregated interaction analytics for performance';

-- =============================================================================
-- Column Comments
-- =============================================================================

-- Comments table columns
COMMENT ON COLUMN comments.content_id IS 'ID of the content being commented on';
COMMENT ON COLUMN comments.user_id IS 'ID of the user making the comment';
COMMENT ON COLUMN comments.parent_comment_id IS 'ID of the parent comment for replies';
COMMENT ON COLUMN comments.comment_text IS 'The actual comment text';
COMMENT ON COLUMN comments.mentions IS 'JSON array of mentioned user IDs';
COMMENT ON COLUMN comments.hashtags IS 'JSON array of hashtags';
COMMENT ON COLUMN comments.media_urls IS 'JSON array of media URLs in comment';
COMMENT ON COLUMN comments.is_edited IS 'Whether the comment has been edited';
COMMENT ON COLUMN comments.edit_count IS 'Number of times the comment has been edited';
COMMENT ON COLUMN comments.edit_history IS 'JSON array storing edit history';
COMMENT ON COLUMN comments.status IS 'Current status of the comment';
COMMENT ON COLUMN comments.moderation_status IS 'Moderation status of the comment';
COMMENT ON COLUMN comments.family_context IS 'JSON object with family-specific context';
COMMENT ON COLUMN comments.cultural_tags IS 'JSON array of cultural tags';
COMMENT ON COLUMN comments.generation_level IS 'Generation level of the commenter';
COMMENT ON COLUMN comments.relationship_context IS 'JSON object with relationship context';
COMMENT ON COLUMN comments.sentiment_score IS 'AI-calculated sentiment score';
COMMENT ON COLUMN comments.language_code IS 'Language of the comment';
COMMENT ON COLUMN comments.is_anonymous IS 'Whether the comment is anonymous';
COMMENT ON COLUMN comments.is_private IS 'Whether the comment is private';
COMMENT ON COLUMN comments.reply_count IS 'Number of replies to this comment';
COMMENT ON COLUMN comments.like_count IS 'Number of likes on this comment';
COMMENT ON COLUMN comments.reaction_count IS 'Number of reactions on this comment';

-- Reactions table columns
COMMENT ON COLUMN reactions.content_id IS 'ID of the content being reacted to';
COMMENT ON COLUMN reactions.user_id IS 'ID of the user making the reaction';
COMMENT ON COLUMN reactions.reaction_type IS 'Type of reaction (LIKE, LOVE, etc.)';
COMMENT ON COLUMN reactions.intensity IS 'Intensity level of the reaction (1-5)';
COMMENT ON COLUMN reactions.family_context IS 'JSON object with family-specific context';
COMMENT ON COLUMN reactions.generation_level IS 'Generation level of the reactor';
COMMENT ON COLUMN reactions.relationship_context IS 'JSON object with relationship context';
COMMENT ON COLUMN reactions.cultural_context IS 'JSON object with cultural context';
COMMENT ON COLUMN reactions.emotional_context IS 'JSON object with emotional context';
COMMENT ON COLUMN reactions.is_anonymous IS 'Whether the reaction is anonymous';
COMMENT ON COLUMN reactions.is_private IS 'Whether the reaction is private';

-- =============================================================================
-- End of Migration
-- =============================================================================
