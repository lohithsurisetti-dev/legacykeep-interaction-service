# 🗄️ Interaction Service Database Schema

## 📋 **Schema Overview**

The Interaction Service database schema is designed to handle Instagram-scale interactions while maintaining data integrity, performance, and family privacy. The schema follows PostgreSQL best practices with proper indexing, constraints, and partitioning strategies.

## 🏗️ **Database Design Principles**

### **Core Principles**
- **Normalization** - Properly normalized schema for data integrity
- **Performance** - Optimized indexes and query patterns
- **Scalability** - Partitioning strategy for large datasets
- **Flexibility** - JSONB columns for extensible metadata
- **Audit Trail** - Complete audit logging for all interactions
- **Family Privacy** - Privacy controls built into the schema

### **Naming Conventions**
- **Tables**: `snake_case` with descriptive names
- **Columns**: `snake_case` with clear, descriptive names
- **Indexes**: `idx_<table>_<columns>` for performance indexes
- **Constraints**: `uk_<table>_<columns>` for unique constraints
- **Foreign Keys**: `fk_<table>_<referenced_table>`

## 📊 **Core Tables**

### **1. interaction_types**

Defines all available interaction types with metadata and configuration.

```sql
CREATE TABLE interaction_types (
    id BIGSERIAL PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL,
    category VARCHAR(30) NOT NULL CHECK (category IN ('ENGAGEMENT', 'SOCIAL', 'CONTENT', 'ANALYTICS')),
    display_name VARCHAR(100) NOT NULL,
    icon_name VARCHAR(50),
    color_code VARCHAR(7), -- Hex color code
    is_active BOOLEAN DEFAULT true,
    requires_content BOOLEAN DEFAULT true,
    requires_user BOOLEAN DEFAULT true,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_interaction_types_category ON interaction_types(category);
CREATE INDEX idx_interaction_types_active ON interaction_types(is_active);

-- Comments
COMMENT ON TABLE interaction_types IS 'Defines all available interaction types with metadata';
COMMENT ON COLUMN interaction_types.type_name IS 'Unique identifier for interaction type (LIKE, COMMENT, SHARE, etc.)';
COMMENT ON COLUMN interaction_types.category IS 'Category classification for grouping similar interactions';
COMMENT ON COLUMN interaction_types.metadata IS 'Additional configuration and metadata for the interaction type';
```

### **2. content_interactions**

Main interaction tracking table that links users to content with specific interaction types.

```sql
CREATE TABLE content_interactions (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    interaction_type_id BIGINT NOT NULL,
    interaction_data JSONB, -- Flexible data storage for interaction-specific data
    metadata JSONB, -- Additional metadata for the interaction
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'DELETED', 'MODERATED', 'HIDDEN')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_content_interactions_type FOREIGN KEY (interaction_type_id) REFERENCES interaction_types(id),
    
    -- Unique constraint to prevent duplicate interactions
    CONSTRAINT uk_content_interactions_unique UNIQUE (content_id, user_id, interaction_type_id)
);

-- Indexes
CREATE INDEX idx_content_interactions_content_id ON content_interactions(content_id);
CREATE INDEX idx_content_interactions_user_id ON content_interactions(user_id);
CREATE INDEX idx_content_interactions_type_id ON content_interactions(interaction_type_id);
CREATE INDEX idx_content_interactions_status ON content_interactions(status);
CREATE INDEX idx_content_interactions_created_at ON content_interactions(created_at);
CREATE INDEX idx_content_interactions_content_user ON content_interactions(content_id, user_id);

-- Comments
COMMENT ON TABLE content_interactions IS 'Main interaction tracking table linking users to content';
COMMENT ON COLUMN content_interactions.interaction_data IS 'Flexible JSONB storage for interaction-specific data';
COMMENT ON COLUMN content_interactions.metadata IS 'Additional metadata for the interaction';
COMMENT ON COLUMN content_interactions.status IS 'Current status of the interaction';
```

### **3. comments**

Comprehensive comment system with nested replies, mentions, and hashtags.

```sql
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    parent_comment_id BIGINT, -- For nested replies
    comment_text TEXT NOT NULL,
    mentions JSONB, -- Array of mentioned user IDs
    hashtags JSONB, -- Array of hashtags
    media_urls JSONB, -- Array of media URLs in comment
    is_edited BOOLEAN DEFAULT false,
    edit_count INTEGER DEFAULT 0,
    edit_history JSONB, -- Store edit history for audit trail
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'DELETED', 'MODERATED', 'HIDDEN')),
    moderation_status VARCHAR(20) DEFAULT 'PENDING' CHECK (moderation_status IN ('PENDING', 'APPROVED', 'REJECTED', 'FLAGGED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_comment_id) REFERENCES comments(id),
    
    -- Check constraints
    CONSTRAINT chk_comments_text_length CHECK (LENGTH(comment_text) >= 1 AND LENGTH(comment_text) <= 2000),
    CONSTRAINT chk_comments_edit_count CHECK (edit_count >= 0)
);

-- Indexes
CREATE INDEX idx_comments_content_id ON comments(content_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_parent_id ON comments(parent_comment_id);
CREATE INDEX idx_comments_status ON comments(status);
CREATE INDEX idx_comments_moderation_status ON comments(moderation_status);
CREATE INDEX idx_comments_created_at ON comments(created_at);
CREATE INDEX idx_comments_content_status ON comments(content_id, status);

-- Comments
COMMENT ON TABLE comments IS 'Comprehensive comment system with nested replies and rich content';
COMMENT ON COLUMN comments.parent_comment_id IS 'Reference to parent comment for nested replies';
COMMENT ON COLUMN comments.mentions IS 'Array of mentioned user IDs for notifications';
COMMENT ON COLUMN comments.hashtags IS 'Array of hashtags for content discovery';
COMMENT ON COLUMN comments.edit_history IS 'JSONB array storing edit history for audit trail';
```

### **4. reactions**

Multi-type reaction system with intensity levels.

```sql
CREATE TABLE reactions (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    reaction_type VARCHAR(20) NOT NULL CHECK (reaction_type IN ('LIKE', 'LOVE', 'HEART', 'LAUGH', 'WOW', 'SAD', 'ANGRY')),
    intensity INTEGER DEFAULT 1 CHECK (intensity >= 1 AND intensity <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Unique constraint to prevent duplicate reactions
    CONSTRAINT uk_reactions_unique UNIQUE (content_id, user_id, reaction_type)
);

-- Indexes
CREATE INDEX idx_reactions_content_id ON reactions(content_id);
CREATE INDEX idx_reactions_user_id ON reactions(user_id);
CREATE INDEX idx_reactions_type ON reactions(reaction_type);
CREATE INDEX idx_reactions_created_at ON reactions(created_at);
CREATE INDEX idx_reactions_content_type ON reactions(content_id, reaction_type);

-- Comments
COMMENT ON TABLE reactions IS 'Multi-type reaction system with intensity levels';
COMMENT ON COLUMN reactions.reaction_type IS 'Type of reaction (LIKE, LOVE, HEART, etc.)';
COMMENT ON COLUMN reactions.intensity IS 'Intensity level of the reaction (1-5 scale)';
```

### **5. ratings**

5-star rating and review system with detailed categories.

```sql
CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    rating_value INTEGER NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5),
    rating_text TEXT,
    rating_categories JSONB, -- Different rating aspects (storytelling, emotional_impact, etc.)
    is_public BOOLEAN DEFAULT true,
    is_anonymous BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Unique constraint to prevent duplicate ratings
    CONSTRAINT uk_ratings_unique UNIQUE (content_id, user_id),
    
    -- Check constraints
    CONSTRAINT chk_ratings_text_length CHECK (rating_text IS NULL OR LENGTH(rating_text) <= 1000)
);

-- Indexes
CREATE INDEX idx_ratings_content_id ON ratings(content_id);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);
CREATE INDEX idx_ratings_value ON ratings(rating_value);
CREATE INDEX idx_ratings_public ON ratings(is_public);
CREATE INDEX idx_ratings_created_at ON ratings(created_at);
CREATE INDEX idx_ratings_content_value ON ratings(content_id, rating_value);

-- Comments
COMMENT ON TABLE ratings IS '5-star rating and review system with detailed categories';
COMMENT ON COLUMN ratings.rating_categories IS 'JSONB object with different rating aspects';
COMMENT ON COLUMN ratings.is_anonymous IS 'Whether the rating is anonymous';
```

### **6. shares**

Content sharing management with target audience and expiration.

```sql
CREATE TABLE shares (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    share_type VARCHAR(20) NOT NULL CHECK (share_type IN ('FAMILY', 'PRIVATE', 'STORY', 'HIGHLIGHT')),
    share_message TEXT,
    target_audience JSONB, -- Specific family members or groups
    is_public BOOLEAN DEFAULT false,
    expires_at TIMESTAMP, -- For temporary shares
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Check constraints
    CONSTRAINT chk_shares_message_length CHECK (share_message IS NULL OR LENGTH(share_message) <= 500)
);

-- Indexes
CREATE INDEX idx_shares_content_id ON shares(content_id);
CREATE INDEX idx_shares_user_id ON shares(user_id);
CREATE INDEX idx_shares_type ON shares(share_type);
CREATE INDEX idx_shares_expires_at ON shares(expires_at);
CREATE INDEX idx_shares_created_at ON shares(created_at);
CREATE INDEX idx_shares_content_type ON shares(content_id, share_type);

-- Comments
COMMENT ON TABLE shares IS 'Content sharing management with target audience and expiration';
COMMENT ON COLUMN shares.target_audience IS 'JSONB array of target user IDs or group IDs';
COMMENT ON COLUMN shares.expires_at IS 'Expiration timestamp for temporary shares';
```

### **7. bookmarks**

Personal content organization with collections and tags.

```sql
CREATE TABLE bookmarks (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    bookmark_collection_id BIGINT, -- Organize bookmarks into collections
    notes TEXT, -- Personal notes about the bookmark
    tags JSONB, -- Personal tags for organization
    is_private BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Unique constraint to prevent duplicate bookmarks
    CONSTRAINT uk_bookmarks_unique UNIQUE (content_id, user_id),
    
    -- Check constraints
    CONSTRAINT chk_bookmarks_notes_length CHECK (notes IS NULL OR LENGTH(notes) <= 1000)
);

-- Indexes
CREATE INDEX idx_bookmarks_content_id ON bookmarks(content_id);
CREATE INDEX idx_bookmarks_user_id ON bookmarks(user_id);
CREATE INDEX idx_bookmarks_collection_id ON bookmarks(bookmark_collection_id);
CREATE INDEX idx_bookmarks_created_at ON bookmarks(created_at);
CREATE INDEX idx_bookmarks_user_collection ON bookmarks(user_id, bookmark_collection_id);

-- Comments
COMMENT ON TABLE bookmarks IS 'Personal content organization with collections and tags';
COMMENT ON COLUMN bookmarks.bookmark_collection_id IS 'Reference to bookmark collection for organization';
COMMENT ON COLUMN bookmarks.tags IS 'JSONB array of personal tags for organization';
```

### **8. content_views**

Detailed view tracking with device and location information.

```sql
CREATE TABLE content_views (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    view_duration INTEGER, -- in seconds
    view_percentage DECIMAL(5,2), -- percentage of content viewed
    view_source VARCHAR(50) CHECK (view_source IN ('MOBILE', 'WEB', 'API', 'PUSH_NOTIFICATION')),
    device_info JSONB, -- Device and browser information
    location_info JSONB, -- Location data (if permitted)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Check constraints
    CONSTRAINT chk_views_duration CHECK (view_duration IS NULL OR view_duration >= 0),
    CONSTRAINT chk_views_percentage CHECK (view_percentage IS NULL OR (view_percentage >= 0 AND view_percentage <= 100))
);

-- Indexes
CREATE INDEX idx_content_views_content_id ON content_views(content_id);
CREATE INDEX idx_content_views_user_id ON content_views(user_id);
CREATE INDEX idx_content_views_source ON content_views(view_source);
CREATE INDEX idx_content_views_created_at ON content_views(created_at);
CREATE INDEX idx_content_views_content_user ON content_views(content_id, user_id);

-- Comments
COMMENT ON TABLE content_views IS 'Detailed view tracking with device and location information';
COMMENT ON COLUMN content_views.view_percentage IS 'Percentage of content viewed (0-100)';
COMMENT ON COLUMN content_views.device_info IS 'JSONB object with device and browser information';
COMMENT ON COLUMN content_views.location_info IS 'JSONB object with location data (if permitted)';
```

### **9. mentions**

User mention system with status tracking.

```sql
CREATE TABLE mentions (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    comment_id BIGINT, -- If mention is in a comment
    mentioned_user_id UUID NOT NULL,
    mentioned_by_user_id UUID NOT NULL,
    mention_type VARCHAR(20) DEFAULT 'CONTENT' CHECK (mention_type IN ('CONTENT', 'COMMENT', 'STORY')),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED', 'IGNORED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_mentions_comment FOREIGN KEY (comment_id) REFERENCES comments(id)
);

-- Indexes
CREATE INDEX idx_mentions_content_id ON mentions(content_id);
CREATE INDEX idx_mentions_comment_id ON mentions(comment_id);
CREATE INDEX idx_mentions_mentioned_user ON mentions(mentioned_user_id);
CREATE INDEX idx_mentions_mentioned_by ON mentions(mentioned_by_user_id);
CREATE INDEX idx_mentions_type ON mentions(mention_type);
CREATE INDEX idx_mentions_status ON mentions(status);
CREATE INDEX idx_mentions_created_at ON mentions(created_at);

-- Comments
COMMENT ON TABLE mentions IS 'User mention system with status tracking';
COMMENT ON COLUMN mentions.mention_type IS 'Type of mention (CONTENT, COMMENT, STORY)';
COMMENT ON COLUMN mentions.status IS 'Status of the mention (PENDING, ACCEPTED, DECLINED, IGNORED)';
```

### **10. polls**

Interactive poll system with multiple options and settings.

```sql
CREATE TABLE polls (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    user_id UUID NOT NULL,
    question TEXT NOT NULL,
    options JSONB NOT NULL, -- Array of poll options
    poll_type VARCHAR(20) DEFAULT 'SINGLE' CHECK (poll_type IN ('SINGLE', 'MULTIPLE')),
    expires_at TIMESTAMP,
    is_anonymous BOOLEAN DEFAULT false,
    allow_comments BOOLEAN DEFAULT true,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'EXPIRED', 'CLOSED', 'DELETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Check constraints
    CONSTRAINT chk_polls_question_length CHECK (LENGTH(question) >= 1 AND LENGTH(question) <= 500)
);

-- Indexes
CREATE INDEX idx_polls_content_id ON polls(content_id);
CREATE INDEX idx_polls_user_id ON polls(user_id);
CREATE INDEX idx_polls_type ON polls(poll_type);
CREATE INDEX idx_polls_expires_at ON polls(expires_at);
CREATE INDEX idx_polls_status ON polls(status);
CREATE INDEX idx_polls_created_at ON polls(created_at);

-- Comments
COMMENT ON TABLE polls IS 'Interactive poll system with multiple options and settings';
COMMENT ON COLUMN polls.options IS 'JSONB array of poll options with IDs and text';
COMMENT ON COLUMN polls.poll_type IS 'Type of poll (SINGLE or MULTIPLE choice)';
```

### **11. poll_responses**

Poll response tracking with multiple option selection.

```sql
CREATE TABLE poll_responses (
    id BIGSERIAL PRIMARY KEY,
    poll_id BIGINT NOT NULL,
    user_id UUID NOT NULL,
    selected_options JSONB NOT NULL, -- Array of selected option IDs
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_poll_responses_poll FOREIGN KEY (poll_id) REFERENCES polls(id),
    
    -- Unique constraint to prevent duplicate responses
    CONSTRAINT uk_poll_responses_unique UNIQUE (poll_id, user_id)
);

-- Indexes
CREATE INDEX idx_poll_responses_poll_id ON poll_responses(poll_id);
CREATE INDEX idx_poll_responses_user_id ON poll_responses(user_id);
CREATE INDEX idx_poll_responses_created_at ON poll_responses(created_at);

-- Comments
COMMENT ON TABLE poll_responses IS 'Poll response tracking with multiple option selection';
COMMENT ON COLUMN poll_responses.selected_options IS 'JSONB array of selected option IDs';
```

### **12. stories**

Story and highlight system with rich content and expiration.

```sql
CREATE TABLE stories (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    content_id UUID, -- Reference to legacy content
    story_type VARCHAR(20) NOT NULL CHECK (story_type IN ('TEXT', 'IMAGE', 'VIDEO', 'POLL', 'QUESTION')),
    story_content JSONB NOT NULL, -- Rich story content
    background_color VARCHAR(7), -- Hex color code
    text_color VARCHAR(7), -- Hex color code
    font_style VARCHAR(50),
    expires_at TIMESTAMP NOT NULL,
    is_highlight BOOLEAN DEFAULT false,
    highlight_collection_id BIGINT,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'EXPIRED', 'DELETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_stories_user_id ON stories(user_id);
CREATE INDEX idx_stories_content_id ON stories(content_id);
CREATE INDEX idx_stories_type ON stories(story_type);
CREATE INDEX idx_stories_expires_at ON stories(expires_at);
CREATE INDEX idx_stories_highlight ON stories(is_highlight);
CREATE INDEX idx_stories_collection_id ON stories(highlight_collection_id);
CREATE INDEX idx_stories_status ON stories(status);
CREATE INDEX idx_stories_created_at ON stories(created_at);

-- Comments
COMMENT ON TABLE stories IS 'Story and highlight system with rich content and expiration';
COMMENT ON COLUMN stories.story_content IS 'JSONB object with rich story content';
COMMENT ON COLUMN stories.highlight_collection_id IS 'Reference to highlight collection';
```

### **13. interaction_analytics**

Comprehensive analytics with daily metrics and engagement rates.

```sql
CREATE TABLE interaction_analytics (
    id BIGSERIAL PRIMARY KEY,
    content_id UUID NOT NULL,
    date DATE NOT NULL,
    view_count INTEGER DEFAULT 0,
    unique_view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    share_count INTEGER DEFAULT 0,
    bookmark_count INTEGER DEFAULT 0,
    rating_count INTEGER DEFAULT 0,
    average_rating DECIMAL(3,2),
    reaction_counts JSONB, -- Count of each reaction type
    engagement_rate DECIMAL(5,2), -- Calculated engagement rate
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Unique constraint for daily analytics
    CONSTRAINT uk_interaction_analytics_unique UNIQUE (content_id, date),
    
    -- Check constraints
    CONSTRAINT chk_analytics_counts CHECK (view_count >= 0 AND unique_view_count >= 0 AND like_count >= 0 AND comment_count >= 0 AND share_count >= 0 AND bookmark_count >= 0 AND rating_count >= 0),
    CONSTRAINT chk_analytics_rating CHECK (average_rating IS NULL OR (average_rating >= 1 AND average_rating <= 5)),
    CONSTRAINT chk_analytics_engagement CHECK (engagement_rate IS NULL OR (engagement_rate >= 0 AND engagement_rate <= 100))
);

-- Indexes
CREATE INDEX idx_interaction_analytics_content_id ON interaction_analytics(content_id);
CREATE INDEX idx_interaction_analytics_date ON interaction_analytics(date);
CREATE INDEX idx_interaction_analytics_created_at ON interaction_analytics(created_at);
CREATE INDEX idx_interaction_analytics_content_date ON interaction_analytics(content_id, date);

-- Comments
COMMENT ON TABLE interaction_analytics IS 'Comprehensive analytics with daily metrics and engagement rates';
COMMENT ON COLUMN interaction_analytics.reaction_counts IS 'JSONB object with count of each reaction type';
COMMENT ON COLUMN interaction_analytics.engagement_rate IS 'Calculated engagement rate percentage';
```

## 🔧 **Additional Tables**

### **bookmark_collections**

Bookmark collection management for organizing bookmarks.

```sql
CREATE TABLE bookmark_collections (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7), -- Hex color code
    is_private BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Check constraints
    CONSTRAINT chk_collections_name_length CHECK (LENGTH(name) >= 1 AND LENGTH(name) <= 100),
    CONSTRAINT chk_collections_description_length CHECK (description IS NULL OR LENGTH(description) <= 500)
);

-- Indexes
CREATE INDEX idx_bookmark_collections_user_id ON bookmark_collections(user_id);
CREATE INDEX idx_bookmark_collections_created_at ON bookmark_collections(created_at);

-- Comments
COMMENT ON TABLE bookmark_collections IS 'Bookmark collection management for organizing bookmarks';
```

### **highlight_collections**

Highlight collection management for organizing story highlights.

```sql
CREATE TABLE highlight_collections (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    cover_image_url VARCHAR(500),
    is_private BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Check constraints
    CONSTRAINT chk_highlight_collections_name_length CHECK (LENGTH(name) >= 1 AND LENGTH(name) <= 100),
    CONSTRAINT chk_highlight_collections_description_length CHECK (description IS NULL OR LENGTH(description) <= 500)
);

-- Indexes
CREATE INDEX idx_highlight_collections_user_id ON highlight_collections(user_id);
CREATE INDEX idx_highlight_collections_created_at ON highlight_collections(created_at);

-- Comments
COMMENT ON TABLE highlight_collections IS 'Highlight collection management for organizing story highlights';
```

## 📊 **Indexing Strategy**

### **Primary Indexes**
- **Primary Keys** - All tables have auto-incrementing primary keys
- **Foreign Keys** - Proper foreign key constraints and indexes
- **Unique Constraints** - Prevent duplicate interactions

### **Performance Indexes**
- **Content-based Queries** - Indexes on content_id for fast content lookups
- **User-based Queries** - Indexes on user_id for user interaction queries
- **Time-based Queries** - Indexes on created_at for time-based analytics
- **Status-based Queries** - Indexes on status fields for filtering

### **Composite Indexes**
- **Content-User Interactions** - (content_id, user_id) for user-specific content interactions
- **User-Time Analytics** - (user_id, created_at) for user activity analytics
- **Content-Time Analytics** - (content_id, created_at) for content performance analytics

## 🔄 **Partitioning Strategy**

### **Time-based Partitioning**
- **Daily Partitions** - Partition large tables by date
- **Monthly Partitions** - For analytics and historical data
- **Automatic Cleanup** - Remove old partitions automatically

### **Content-based Partitioning**
- **Content ID Hashing** - Distribute content interactions across partitions
- **User ID Hashing** - Distribute user interactions across partitions

## 🔒 **Security Considerations**

### **Data Protection**
- **Encryption at Rest** - All sensitive data encrypted in database
- **Audit Logging** - Complete audit trail for all interactions
- **Data Anonymization** - Anonymize analytics data
- **Privacy Controls** - Built-in privacy controls for family data

### **Access Control**
- **Row-level Security** - Implement RLS for family data isolation
- **Column-level Security** - Protect sensitive columns
- **Audit Trails** - Track all data access and modifications

## 📈 **Performance Optimization**

### **Query Optimization**
- **Efficient Joins** - Optimized join strategies
- **Query Caching** - Cache frequently executed queries
- **Connection Pooling** - Efficient database connection management
- **Read Replicas** - Separate read and write databases

### **Storage Optimization**
- **Compression** - Compress large tables and indexes
- **Archiving** - Archive old data to reduce table size
- **Cleanup Jobs** - Regular cleanup of expired data

---

*This database schema is designed to handle Instagram-scale interactions while maintaining family privacy and cultural sensitivity.*
