# 🎯 LegacyKeep Interaction Service

## 📖 **Overview**

The Interaction Service is a comprehensive social engagement platform for LegacyKeep that handles all user interactions with legacy content at Instagram-scale. It provides a rich set of interaction features specifically designed for family legacy content while maintaining privacy and cultural sensitivity.

## 🎯 **Core Mission**

To provide a comprehensive social interaction platform that enables families to engage deeply with their legacy content through:
- **Rich Interactions** - Comments, reactions, ratings, shares, and bookmarks
- **Real-time Engagement** - Live updates and notifications
- **Family Context** - Interactions that respect family relationships and privacy
- **Cultural Sensitivity** - Features designed for family legacy preservation
- **High Performance** - Instagram-scale interaction handling

## 🏗️ **Architecture**

### **Service Components**
- **Comment System** - Nested comments with replies and mentions
- **Reaction Engine** - Multiple reaction types with intensity levels
- **Rating System** - 5-star ratings with detailed reviews
- **Share Management** - Content sharing within family circles
- **Bookmark Collections** - Personal content organization
- **Analytics Engine** - Comprehensive engagement tracking
- **Real-time Updates** - WebSocket integration for live interactions
- **Content Moderation** - Family-friendly interaction filtering

### **Technology Stack**
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL with Redis caching
- **Real-time**: WebSocket + STOMP for live updates
- **Message Queue**: Apache Kafka for event-driven communication
- **Security**: JWT authentication with role-based access
- **Caching**: Redis for high-performance interaction queries

## 📊 **Database Schema**

### **Core Tables**

#### **interaction_types**
Defines all available interaction types with metadata
- Type definitions (LIKE, COMMENT, SHARE, BOOKMARK, RATING, REACTION)
- Display information and icons
- Category classification and metadata

#### **content_interactions**
Main interaction tracking table
- Links users to content with specific interaction types
- Flexible JSONB data storage for interaction metadata
- Status tracking and audit trail

#### **comments**
Comprehensive comment system
- Nested comment threads with parent-child relationships
- Rich text with mentions and hashtags
- Edit history and moderation status

#### **reactions**
Multi-type reaction system
- Multiple reaction types (Heart, Love, Laugh, Wow, Sad, Angry)
- Intensity levels (1-5 scale)
- Unique constraints per user-content-reaction type

#### **ratings**
5-star rating and review system
- Rating values with optional review text
- Rating categories for different aspects
- Public/private and anonymous options

#### **shares**
Content sharing management
- Share types (FAMILY, PRIVATE, STORY, HIGHLIGHT)
- Target audience specification
- Expiration for temporary shares

#### **bookmarks**
Personal content organization
- Bookmark collections for organization
- Personal notes and tags
- Private bookmark management

#### **content_views**
Detailed view tracking
- View duration and percentage
- Device and location information
- View source tracking

#### **mentions**
User mention system
- Content and comment mentions
- Mention status tracking
- Integration with Relationship Service

#### **polls**
Interactive poll system
- Single and multiple choice polls
- Anonymous and public options
- Expiration and comment settings

#### **poll_responses**
Poll response tracking
- User responses to polls
- Multiple option selection support
- Response analytics

#### **stories**
Story and highlight system
- Temporary and permanent stories
- Rich story content with media
- Expiration and highlight management

#### **interaction_analytics**
Comprehensive analytics
- Daily engagement metrics
- Reaction type breakdowns
- Engagement rate calculations

## 🔗 **API Endpoints**

### **Comments & Replies**
- `POST /api/v1/interactions/comments` - Create comment
- `GET /api/v1/interactions/comments/{contentId}` - Get comments with pagination
- `PUT /api/v1/interactions/comments/{commentId}` - Update comment
- `DELETE /api/v1/interactions/comments/{commentId}` - Delete comment
- `POST /api/v1/interactions/comments/{commentId}/replies` - Reply to comment
- `GET /api/v1/interactions/comments/{commentId}/replies` - Get comment replies
- `POST /api/v1/interactions/comments/{commentId}/like` - Like comment
- `DELETE /api/v1/interactions/comments/{commentId}/like` - Unlike comment

### **Reactions & Likes**
- `POST /api/v1/interactions/reactions` - Add reaction
- `PUT /api/v1/interactions/reactions/{reactionId}` - Update reaction
- `DELETE /api/v1/interactions/reactions/{reactionId}` - Remove reaction
- `GET /api/v1/interactions/reactions/{contentId}` - Get reactions for content
- `GET /api/v1/interactions/reactions/{contentId}/summary` - Get reaction summary

### **Ratings & Reviews**
- `POST /api/v1/interactions/ratings` - Add rating
- `PUT /api/v1/interactions/ratings/{ratingId}` - Update rating
- `DELETE /api/v1/interactions/ratings/{ratingId}` - Delete rating
- `GET /api/v1/interactions/ratings/{contentId}` - Get ratings for content
- `GET /api/v1/interactions/ratings/{contentId}/summary` - Get rating summary

### **Shares & Reposts**
- `POST /api/v1/interactions/shares` - Share content
- `GET /api/v1/interactions/shares/{contentId}` - Get shares for content
- `DELETE /api/v1/interactions/shares/{shareId}` - Delete share
- `POST /api/v1/interactions/shares/{shareId}/like` - Like share

### **Bookmarks & Collections**
- `POST /api/v1/interactions/bookmarks` - Bookmark content
- `GET /api/v1/interactions/bookmarks` - Get user's bookmarks
- `PUT /api/v1/interactions/bookmarks/{bookmarkId}` - Update bookmark
- `DELETE /api/v1/interactions/bookmarks/{bookmarkId}` - Remove bookmark
- `POST /api/v1/interactions/bookmark-collections` - Create bookmark collection
- `GET /api/v1/interactions/bookmark-collections` - Get bookmark collections

### **Views & Analytics**
- `POST /api/v1/interactions/views` - Record content view
- `GET /api/v1/interactions/views/{contentId}` - Get view analytics
- `GET /api/v1/interactions/analytics/{contentId}` - Get comprehensive analytics
- `GET /api/v1/interactions/trending` - Get trending content

### **Mentions & Tags**
- `POST /api/v1/interactions/mentions` - Mention user
- `GET /api/v1/interactions/mentions` - Get user mentions
- `PUT /api/v1/interactions/mentions/{mentionId}/status` - Update mention status

### **Polls & Questions**
- `POST /api/v1/interactions/polls` - Create poll
- `GET /api/v1/interactions/polls/{pollId}` - Get poll details
- `POST /api/v1/interactions/polls/{pollId}/responses` - Respond to poll
- `GET /api/v1/interactions/polls/{pollId}/responses` - Get poll responses
- `PUT /api/v1/interactions/polls/{pollId}` - Update poll

### **Stories & Highlights**
- `POST /api/v1/interactions/stories` - Create story
- `GET /api/v1/interactions/stories` - Get user stories
- `GET /api/v1/interactions/stories/{storyId}` - Get story details
- `POST /api/v1/interactions/stories/{storyId}/views` - Record story view
- `POST /api/v1/interactions/stories/{storyId}/highlights` - Add to highlights
- `GET /api/v1/interactions/highlights` - Get user highlights

## 🔄 **Service Integration**

### **Legacy Service Integration**
- **Content Validation** - Verify content exists before allowing interactions
- **Permission Checks** - Respect content privacy settings
- **Real-time Updates** - Notify Legacy Service of interaction changes
- **Analytics Integration** - Provide interaction data for content analytics

### **Relationship Service Integration**
- **Mention Validation** - Validate mentioned users are family members
- **Share Targeting** - Validate share targets are family members
- **Permission Checks** - Use relationship data for interaction permissions
- **Family Context** - Get family relationships for interaction context

### **Notification Service Integration**
- **Kafka Events** - Send interaction events for notifications
- **Real-time Alerts** - Notify users of new interactions
- **Family Notifications** - Notify family members of interactions
- **Moderation Alerts** - Alert moderators of flagged content

## 🔒 **Security & Privacy**

### **Access Control**
- **Family-only Interactions** - Interactions limited to family members
- **Role-based Permissions** - Different interaction rights for different roles
- **Content Privacy** - Respect content privacy settings
- **Anonymous Options** - Support for anonymous ratings and polls

### **Data Protection**
- **Encryption** - All interaction data encrypted at rest
- **Audit Logging** - Complete audit trail of all interactions
- **GDPR Compliance** - Data deletion and portability support
- **Content Moderation** - AI-powered inappropriate content detection

## 📱 **Mobile Integration**

### **React Native Features**
- **Real-time Updates** - Live interaction feeds via WebSocket
- **Push Notifications** - Interaction alerts via Kafka events
- **Offline Support** - Queue interactions when offline
- **Gesture Support** - Swipe to like, long-press for reactions
- **Haptic Feedback** - Physical feedback for interactions

### **Elder-Friendly Design**
- **Large Touch Targets** - Easy interaction for all ages
- **Simple Gestures** - Intuitive interaction patterns
- **Voice Interactions** - Voice-to-text for comments
- **Accessibility Support** - Screen reader and accessibility features

## 🚀 **Performance & Scalability**

### **Database Optimization**
- **Partitioning** - Partition large tables by date
- **Indexing Strategy** - Optimized indexes for fast queries
- **Caching Layer** - Redis caching for frequently accessed data
- **Read Replicas** - Separate read and write databases

### **API Performance**
- **Pagination** - Efficient pagination for large datasets
- **Rate Limiting** - Prevent abuse and ensure fair usage
- **Caching** - Cache frequently requested data
- **Async Processing** - Background processing for heavy operations

## 🔧 **Configuration**

### **Environment Variables**
```bash
# Database
DB_USERNAME=legacykeep
DB_PASSWORD=password
DATABASE_URL=jdbc:postgresql://localhost:5432/interaction_db

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=900000

# Service URLs
LEGACY_SERVICE_URL=http://localhost:8085/api/v1
RELATIONSHIP_SERVICE_URL=http://localhost:8084/api/v1
NOTIFICATION_SERVICE_URL=http://localhost:8083/api/v1
AUTH_SERVICE_URL=http://localhost:8081/api/v1
```

## 🧪 **Testing**

### **Test Categories**
- **Unit Tests** - Individual component testing
- **Integration Tests** - Service integration testing
- **End-to-End Tests** - Complete user journey testing
- **Performance Tests** - Load and stress testing
- **Security Tests** - Security vulnerability testing

### **Test Data**
- **Mock Data** - Synthetic family data for testing
- **Test Scenarios** - Multi-generational interaction scenarios
- **Performance Data** - Large-scale interaction testing

## 📚 **Development Guide**

### **Getting Started**
1. **Prerequisites**: Java 17, Maven, PostgreSQL, Redis, Kafka
2. **Database Setup**: Run Flyway migrations
3. **Service Dependencies**: Start Legacy, Relationship, Notification, Auth services
4. **Run Application**: `mvn spring-boot:run`

### **Development Workflow**
1. **Feature Development**: Create feature branches
2. **Database Changes**: Add Flyway migrations
3. **Testing**: Write comprehensive tests
4. **Code Review**: Peer review process
5. **Deployment**: Automated deployment pipeline

## 🔮 **Future Enhancements**

### **AI/ML Features**
- **Content Moderation** - AI-powered inappropriate content detection
- **Interaction Recommendations** - Suggest content to interact with
- **Sentiment Analysis** - Analyze interaction sentiment
- **Engagement Prediction** - Predict content engagement levels

### **Advanced Features**
- **Voice Comments** - Voice-to-text comment system
- **AR Interactions** - Augmented reality interaction features
- **Blockchain Verification** - Immutable interaction verification
- **Advanced Analytics** - Machine learning-powered insights

## 📞 **Support & Contact**

- **Documentation**: [Interaction Service Architecture](./docs/ARCHITECTURE.md)
- **API Documentation**: [Swagger UI](http://localhost:8086/api/v1/swagger-ui.html)
- **Health Check**: [Health Endpoint](http://localhost:8086/api/v1/interactions/health)
- **Issues**: [GitHub Issues](https://github.com/lohithsurisetti-dev/legacykeep-interaction-service/issues)

---

*This service is part of the LegacyKeep platform, dedicated to preserving family legacies across generations through rich social interactions.*
