# 📚 Interaction Service API Documentation

## 📋 **API Overview**

The Interaction Service provides a comprehensive REST API for managing all user interactions with legacy content. The API follows RESTful principles and provides consistent response formats across all endpoints.

## 🔗 **Base URL**
```
http://localhost:8086/api/v1
```

## 🔐 **Authentication**

All API endpoints require JWT authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

## 📊 **Response Format**

All API responses follow a consistent format:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    // Response data
  },
  "timestamp": "2025-01-09T10:30:00Z",
  "correlationId": "uuid"
}
```

### **Error Response Format**
```json
{
  "success": false,
  "message": "Error description",
  "error": {
    "code": "ERROR_CODE",
    "details": "Detailed error information"
  },
  "timestamp": "2025-01-09T10:30:00Z",
  "correlationId": "uuid"
}
```

## 📝 **Comments & Replies API**

### **Create Comment**
```http
POST /api/v1/interactions/comments
Content-Type: application/json

{
  "contentId": "uuid",
  "commentText": "This is a great story!",
  "parentCommentId": null,
  "mentions": ["user-uuid-1", "user-uuid-2"],
  "hashtags": ["#family", "#memories"]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Comment created successfully",
  "data": {
    "commentId": 123,
    "contentId": "uuid",
    "userId": "uuid",
    "commentText": "This is a great story!",
    "mentions": ["user-uuid-1", "user-uuid-2"],
    "hashtags": ["#family", "#memories"],
    "isEdited": false,
    "editCount": 0,
    "status": "ACTIVE",
    "moderationStatus": "PENDING",
    "createdAt": "2025-01-09T10:30:00Z",
    "updatedAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Get Comments for Content**
```http
GET /api/v1/interactions/comments/{contentId}?page=0&size=20&sort=createdAt,desc
```

**Response:**
```json
{
  "success": true,
  "message": "Comments retrieved successfully",
  "data": {
    "content": [
      {
        "commentId": 123,
        "contentId": "uuid",
        "userId": "uuid",
        "userName": "John Doe",
        "commentText": "This is a great story!",
        "mentions": ["user-uuid-1"],
        "hashtags": ["#family"],
        "isEdited": false,
        "editCount": 0,
        "status": "ACTIVE",
        "moderationStatus": "APPROVED",
        "createdAt": "2025-01-09T10:30:00Z",
        "updatedAt": "2025-01-09T10:30:00Z",
        "replyCount": 2,
        "likeCount": 5
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 50,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

### **Update Comment**
```http
PUT /api/v1/interactions/comments/{commentId}
Content-Type: application/json

{
  "commentText": "Updated comment text",
  "mentions": ["user-uuid-1"],
  "hashtags": ["#family", "#updated"]
}
```

### **Delete Comment**
```http
DELETE /api/v1/interactions/comments/{commentId}
```

### **Reply to Comment**
```http
POST /api/v1/interactions/comments/{commentId}/replies
Content-Type: application/json

{
  "commentText": "This is a reply to the comment",
  "mentions": ["user-uuid-1"]
}
```

### **Get Comment Replies**
```http
GET /api/v1/interactions/comments/{commentId}/replies?page=0&size=10
```

### **Like Comment**
```http
POST /api/v1/interactions/comments/{commentId}/like
```

### **Unlike Comment**
```http
DELETE /api/v1/interactions/comments/{commentId}/like
```

## ❤️ **Reactions & Likes API**

### **Add Reaction**
```http
POST /api/v1/interactions/reactions
Content-Type: application/json

{
  "contentId": "uuid",
  "reactionType": "LOVE",
  "intensity": 3
}
```

**Response:**
```json
{
  "success": true,
  "message": "Reaction added successfully",
  "data": {
    "reactionId": 456,
    "contentId": "uuid",
    "userId": "uuid",
    "reactionType": "LOVE",
    "intensity": 3,
    "createdAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Update Reaction**
```http
PUT /api/v1/interactions/reactions/{reactionId}
Content-Type: application/json

{
  "reactionType": "HEART",
  "intensity": 5
}
```

### **Remove Reaction**
```http
DELETE /api/v1/interactions/reactions/{reactionId}
```

### **Get Reactions for Content**
```http
GET /api/v1/interactions/reactions/{contentId}
```

**Response:**
```json
{
  "success": true,
  "message": "Reactions retrieved successfully",
  "data": [
    {
      "reactionId": 456,
      "contentId": "uuid",
      "userId": "uuid",
      "userName": "John Doe",
      "reactionType": "LOVE",
      "intensity": 3,
      "createdAt": "2025-01-09T10:30:00Z"
    }
  ]
}
```

### **Get Reaction Summary**
```http
GET /api/v1/interactions/reactions/{contentId}/summary
```

**Response:**
```json
{
  "success": true,
  "message": "Reaction summary retrieved successfully",
  "data": {
    "contentId": "uuid",
    "totalReactions": 25,
    "reactionBreakdown": {
      "LIKE": 10,
      "LOVE": 8,
      "HEART": 5,
      "LAUGH": 2
    },
    "userReaction": {
      "reactionType": "LOVE",
      "intensity": 3
    }
  }
}
```

## ⭐ **Ratings & Reviews API**

### **Add Rating**
```http
POST /api/v1/interactions/ratings
Content-Type: application/json

{
  "contentId": "uuid",
  "ratingValue": 5,
  "ratingText": "This is an amazing family story!",
  "ratingCategories": {
    "storytelling": 5,
    "emotional_impact": 5,
    "historical_value": 4
  },
  "isPublic": true,
  "isAnonymous": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Rating added successfully",
  "data": {
    "ratingId": 789,
    "contentId": "uuid",
    "userId": "uuid",
    "ratingValue": 5,
    "ratingText": "This is an amazing family story!",
    "ratingCategories": {
      "storytelling": 5,
      "emotional_impact": 5,
      "historical_value": 4
    },
    "isPublic": true,
    "isAnonymous": false,
    "createdAt": "2025-01-09T10:30:00Z",
    "updatedAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Update Rating**
```http
PUT /api/v1/interactions/ratings/{ratingId}
Content-Type: application/json

{
  "ratingValue": 4,
  "ratingText": "Updated review text",
  "ratingCategories": {
    "storytelling": 4,
    "emotional_impact": 5,
    "historical_value": 4
  }
}
```

### **Delete Rating**
```http
DELETE /api/v1/interactions/ratings/{ratingId}
```

### **Get Ratings for Content**
```http
GET /api/v1/interactions/ratings/{contentId}?page=0&size=10&sort=createdAt,desc
```

### **Get Rating Summary**
```http
GET /api/v1/interactions/ratings/{contentId}/summary
```

**Response:**
```json
{
  "success": true,
  "message": "Rating summary retrieved successfully",
  "data": {
    "contentId": "uuid",
    "averageRating": 4.5,
    "totalRatings": 20,
    "ratingBreakdown": {
      "5": 12,
      "4": 6,
      "3": 2,
      "2": 0,
      "1": 0
    },
    "categoryAverages": {
      "storytelling": 4.3,
      "emotional_impact": 4.7,
      "historical_value": 4.1
    },
    "userRating": {
      "ratingValue": 5,
      "ratingText": "This is an amazing family story!"
    }
  }
}
```

## 📤 **Shares & Reposts API**

### **Share Content**
```http
POST /api/v1/interactions/shares
Content-Type: application/json

{
  "contentId": "uuid",
  "shareType": "FAMILY",
  "shareMessage": "Check out this amazing family story!",
  "targetAudience": ["user-uuid-1", "user-uuid-2"],
  "isPublic": false,
  "expiresAt": "2025-01-16T10:30:00Z"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Content shared successfully",
  "data": {
    "shareId": 101,
    "contentId": "uuid",
    "userId": "uuid",
    "shareType": "FAMILY",
    "shareMessage": "Check out this amazing family story!",
    "targetAudience": ["user-uuid-1", "user-uuid-2"],
    "isPublic": false,
    "expiresAt": "2025-01-16T10:30:00Z",
    "createdAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Get Shares for Content**
```http
GET /api/v1/interactions/shares/{contentId}?page=0&size=10
```

### **Delete Share**
```http
DELETE /api/v1/interactions/shares/{shareId}
```

### **Like Share**
```http
POST /api/v1/interactions/shares/{shareId}/like
```

## 🔖 **Bookmarks & Collections API**

### **Bookmark Content**
```http
POST /api/v1/interactions/bookmarks
Content-Type: application/json

{
  "contentId": "uuid",
  "bookmarkCollectionId": 1,
  "notes": "This is a great family story to share with kids",
  "tags": ["family", "kids", "storytelling"],
  "isPrivate": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Content bookmarked successfully",
  "data": {
    "bookmarkId": 202,
    "contentId": "uuid",
    "userId": "uuid",
    "bookmarkCollectionId": 1,
    "notes": "This is a great family story to share with kids",
    "tags": ["family", "kids", "storytelling"],
    "isPrivate": true,
    "createdAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Get User's Bookmarks**
```http
GET /api/v1/interactions/bookmarks?page=0&size=20&collectionId=1&tags=family
```

### **Update Bookmark**
```http
PUT /api/v1/interactions/bookmarks/{bookmarkId}
Content-Type: application/json

{
  "notes": "Updated notes about this bookmark",
  "tags": ["family", "kids", "storytelling", "updated"],
  "bookmarkCollectionId": 2
}
```

### **Remove Bookmark**
```http
DELETE /api/v1/interactions/bookmarks/{bookmarkId}
```

### **Create Bookmark Collection**
```http
POST /api/v1/interactions/bookmark-collections
Content-Type: application/json

{
  "name": "Family Stories for Kids",
  "description": "Stories suitable for children",
  "isPrivate": true,
  "color": "#FF5733"
}
```

### **Get Bookmark Collections**
```http
GET /api/v1/interactions/bookmark-collections?page=0&size=10
```

## 👁️ **Views & Analytics API**

### **Record Content View**
```http
POST /api/v1/interactions/views
Content-Type: application/json

{
  "contentId": "uuid",
  "viewDuration": 120,
  "viewPercentage": 85.5,
  "viewSource": "MOBILE",
  "deviceInfo": {
    "deviceType": "mobile",
    "browser": "Chrome",
    "os": "iOS"
  }
}
```

### **Get View Analytics**
```http
GET /api/v1/interactions/views/{contentId}?startDate=2025-01-01&endDate=2025-01-31
```

### **Get Comprehensive Analytics**
```http
GET /api/v1/interactions/analytics/{contentId}?period=30d
```

**Response:**
```json
{
  "success": true,
  "message": "Analytics retrieved successfully",
  "data": {
    "contentId": "uuid",
    "period": "30d",
    "totalViews": 150,
    "uniqueViews": 120,
    "averageViewDuration": 95.5,
    "averageViewPercentage": 78.2,
    "totalLikes": 45,
    "totalComments": 23,
    "totalShares": 12,
    "totalBookmarks": 8,
    "totalRatings": 15,
    "averageRating": 4.3,
    "engagementRate": 65.5,
    "reactionBreakdown": {
      "LIKE": 20,
      "LOVE": 15,
      "HEART": 10
    },
    "viewSourceBreakdown": {
      "MOBILE": 80,
      "WEB": 20
    },
    "dailyMetrics": [
      {
        "date": "2025-01-09",
        "views": 5,
        "likes": 2,
        "comments": 1
      }
    ]
  }
}
```

### **Get Trending Content**
```http
GET /api/v1/interactions/trending?period=7d&limit=20&category=family
```

## 🏷️ **Mentions & Tags API**

### **Mention User**
```http
POST /api/v1/interactions/mentions
Content-Type: application/json

{
  "contentId": "uuid",
  "commentId": 123,
  "mentionedUserId": "user-uuid-1",
  "mentionType": "COMMENT"
}
```

### **Get User Mentions**
```http
GET /api/v1/interactions/mentions?page=0&size=20&status=PENDING
```

### **Update Mention Status**
```http
PUT /api/v1/interactions/mentions/{mentionId}/status
Content-Type: application/json

{
  "status": "ACCEPTED"
}
```

## 📊 **Polls & Questions API**

### **Create Poll**
```http
POST /api/v1/interactions/polls
Content-Type: application/json

{
  "contentId": "uuid",
  "question": "What's your favorite family tradition?",
  "options": [
    "Holiday gatherings",
    "Family recipes",
    "Storytelling sessions",
    "Photo albums"
  ],
  "pollType": "SINGLE",
  "expiresAt": "2025-01-16T10:30:00Z",
  "isAnonymous": false,
  "allowComments": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Poll created successfully",
  "data": {
    "pollId": 303,
    "contentId": "uuid",
    "userId": "uuid",
    "question": "What's your favorite family tradition?",
    "options": [
      {
        "id": 1,
        "text": "Holiday gatherings",
        "voteCount": 0
      },
      {
        "id": 2,
        "text": "Family recipes",
        "voteCount": 0
      },
      {
        "id": 3,
        "text": "Storytelling sessions",
        "voteCount": 0
      },
      {
        "id": 4,
        "text": "Photo albums",
        "voteCount": 0
      }
    ],
    "pollType": "SINGLE",
    "expiresAt": "2025-01-16T10:30:00Z",
    "isAnonymous": false,
    "allowComments": true,
    "status": "ACTIVE",
    "createdAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Get Poll Details**
```http
GET /api/v1/interactions/polls/{pollId}
```

### **Respond to Poll**
```http
POST /api/v1/interactions/polls/{pollId}/responses
Content-Type: application/json

{
  "selectedOptions": [1]
}
```

### **Get Poll Responses**
```http
GET /api/v1/interactions/polls/{pollId}/responses?page=0&size=20
```

### **Update Poll**
```http
PUT /api/v1/interactions/polls/{pollId}
Content-Type: application/json

{
  "question": "Updated poll question",
  "expiresAt": "2025-01-20T10:30:00Z"
}
```

## 📱 **Stories & Highlights API**

### **Create Story**
```http
POST /api/v1/interactions/stories
Content-Type: application/json

{
  "contentId": "uuid",
  "storyType": "TEXT",
  "storyContent": {
    "text": "Check out this amazing family memory!",
    "fontSize": "large",
    "textAlign": "center"
  },
  "backgroundColor": "#FF5733",
  "textColor": "#FFFFFF",
  "fontStyle": "bold",
  "expiresAt": "2025-01-10T10:30:00Z",
  "isHighlight": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Story created successfully",
  "data": {
    "storyId": 404,
    "userId": "uuid",
    "contentId": "uuid",
    "storyType": "TEXT",
    "storyContent": {
      "text": "Check out this amazing family memory!",
      "fontSize": "large",
      "textAlign": "center"
    },
    "backgroundColor": "#FF5733",
    "textColor": "#FFFFFF",
    "fontStyle": "bold",
    "expiresAt": "2025-01-10T10:30:00Z",
    "isHighlight": false,
    "highlightCollectionId": null,
    "status": "ACTIVE",
    "createdAt": "2025-01-09T10:30:00Z"
  }
}
```

### **Get User Stories**
```http
GET /api/v1/interactions/stories?page=0&size=20&includeExpired=false
```

### **Get Story Details**
```http
GET /api/v1/interactions/stories/{storyId}
```

### **Record Story View**
```http
POST /api/v1/interactions/stories/{storyId}/views
Content-Type: application/json

{
  "viewDuration": 15
}
```

### **Add to Highlights**
```http
POST /api/v1/interactions/stories/{storyId}/highlights
Content-Type: application/json

{
  "highlightCollectionId": 1
}
```

### **Get User Highlights**
```http
GET /api/v1/interactions/highlights?page=0&size=20
```

## 🏥 **Health Check API**

### **Service Health**
```http
GET /api/v1/interactions/health
```

**Response:**
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2025-01-09T10:30:00Z",
    "version": "1.0.0",
    "database": "UP",
    "cache": "UP",
    "kafka": "UP"
  }
}
```

## 📊 **Error Codes**

| Code | Description |
|------|-------------|
| `INTERACTION_NOT_FOUND` | Interaction not found |
| `CONTENT_NOT_FOUND` | Content not found |
| `USER_NOT_FOUND` | User not found |
| `INVALID_INTERACTION_TYPE` | Invalid interaction type |
| `DUPLICATE_INTERACTION` | Duplicate interaction |
| `INSUFFICIENT_PERMISSIONS` | Insufficient permissions |
| `CONTENT_PRIVATE` | Content is private |
| `INTERACTION_EXPIRED` | Interaction has expired |
| `RATE_LIMIT_EXCEEDED` | Rate limit exceeded |
| `VALIDATION_ERROR` | Input validation error |

## 🔄 **Rate Limiting**

The API implements rate limiting to ensure fair usage:

- **Comments**: 100 per hour per user
- **Reactions**: 500 per hour per user
- **Ratings**: 50 per hour per user
- **Shares**: 200 per hour per user
- **Bookmarks**: 300 per hour per user
- **Views**: 1000 per hour per user

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1641744000
```

## 📱 **SDK Examples**

### **JavaScript/TypeScript**
```typescript
import { InteractionServiceClient } from '@legacykeep/interaction-service-sdk';

const client = new InteractionServiceClient({
  baseUrl: 'http://localhost:8086/api/v1',
  token: 'your-jwt-token'
});

// Create a comment
const comment = await client.comments.create({
  contentId: 'uuid',
  commentText: 'Great story!',
  mentions: ['user-uuid-1']
});

// Add a reaction
const reaction = await client.reactions.add({
  contentId: 'uuid',
  reactionType: 'LOVE',
  intensity: 5
});
```

### **Java**
```java
@Autowired
private InteractionServiceClient interactionServiceClient;

// Create a comment
CreateCommentRequest request = CreateCommentRequest.builder()
    .contentId("uuid")
    .commentText("Great story!")
    .mentions(Arrays.asList("user-uuid-1"))
    .build();

CommentResponse response = interactionServiceClient.comments().create(request);
```

---

*This API documentation provides comprehensive coverage of all Interaction Service endpoints with examples and response formats.*
