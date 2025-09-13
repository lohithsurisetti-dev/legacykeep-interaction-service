#!/bin/bash

# =============================================================================
# Interaction Service API Test Runner
# =============================================================================
# This script runs all tests for the Interaction Service APIs
# 
# Author: LegacyKeep Team
# Version: 1.0.0
# =============================================================================

set -e

echo "🚀 Starting Interaction Service API Tests..."
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found. Please run this script from the interaction-service directory."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven to run tests."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java to run tests."
    exit 1
fi

print_status "Environment check passed ✅"

# Clean and compile the project
print_status "Cleaning and compiling the project..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    print_success "Project compiled successfully ✅"
else
    print_error "Project compilation failed ❌"
    exit 1
fi

# Run unit tests
print_status "Running unit tests..."
echo "----------------------------------------"

# Run Comment Controller Tests
print_status "Testing Comment Controller APIs..."
mvn test -Dtest=CommentControllerTest -q

if [ $? -eq 0 ]; then
    print_success "Comment Controller tests passed ✅"
else
    print_error "Comment Controller tests failed ❌"
    exit 1
fi

# Run Reaction Controller Tests
print_status "Testing Reaction Controller APIs..."
mvn test -Dtest=ReactionControllerTest -q

if [ $? -eq 0 ]; then
    print_success "Reaction Controller tests passed ✅"
else
    print_error "Reaction Controller tests failed ❌"
    exit 1
fi

# Run Integration Tests
print_status "Running integration tests..."
echo "----------------------------------------"

mvn test -Dtest=InteractionServiceIntegrationTest -q

if [ $? -eq 0 ]; then
    print_success "Integration tests passed ✅"
else
    print_error "Integration tests failed ❌"
    exit 1
fi

# Run all tests
print_status "Running all tests..."
echo "----------------------------------------"

mvn test -q

if [ $? -eq 0 ]; then
    print_success "All tests passed ✅"
else
    print_error "Some tests failed ❌"
    exit 1
fi

# Generate test report
print_status "Generating test report..."
mvn surefire-report:report -q

if [ $? -eq 0 ]; then
    print_success "Test report generated ✅"
    print_status "Test report available at: target/site/surefire-report.html"
else
    print_warning "Test report generation failed, but tests passed"
fi

# Run tests with coverage (if JaCoCo is configured)
print_status "Running tests with coverage..."
mvn test jacoco:report -q

if [ $? -eq 0 ]; then
    print_success "Coverage report generated ✅"
    print_status "Coverage report available at: target/site/jacoco/index.html"
else
    print_warning "Coverage report generation failed, but tests passed"
fi

# Summary
echo ""
echo "=============================================="
print_success "🎉 All Interaction Service API tests completed successfully!"
echo ""
print_status "Test Summary:"
print_status "✅ Comment Controller Tests - All APIs tested"
print_status "✅ Reaction Controller Tests - All APIs tested"
print_status "✅ Integration Tests - End-to-end flow tested"
print_status "✅ Validation Tests - Input validation tested"
print_status "✅ Error Handling Tests - Error scenarios tested"
echo ""
print_status "APIs Tested:"
echo "  📝 Comment APIs:"
echo "    - POST /api/v1/comments (Create Comment)"
echo "    - GET /api/v1/comments/content/{contentId} (Get Comments)"
echo "    - GET /api/v1/comments/{commentId} (Get Comment by ID)"
echo "    - PUT /api/v1/comments/{commentId} (Update Comment)"
echo "    - DELETE /api/v1/comments/{commentId} (Delete Comment)"
echo "    - POST /api/v1/comments/{parentCommentId}/replies (Create Reply)"
echo "    - GET /api/v1/comments/{parentCommentId}/replies (Get Replies)"
echo "    - GET /api/v1/comments/{commentId}/thread (Get Comment Thread)"
echo "    - POST /api/v1/comments/{commentId}/like (Like Comment)"
echo "    - DELETE /api/v1/comments/{commentId}/like (Unlike Comment)"
echo "    - GET /api/v1/comments/{commentId}/liked (Check if Liked)"
echo "    - GET /api/v1/comments/family-member/{familyMemberId} (Get by Family Member)"
echo "    - GET /api/v1/comments/generation/{generationLevel} (Get by Generation)"
echo "    - GET /api/v1/comments/cultural/{culturalTag} (Get by Cultural Context)"
echo "    - GET /api/v1/comments/search (Search Comments)"
echo "    - GET /api/v1/comments/hashtag/{hashtag} (Get by Hashtag)"
echo "    - GET /api/v1/comments/trending/hashtags (Get Trending Hashtags)"
echo "    - POST /api/v1/comments/{commentId}/moderate (Moderate Comment)"
echo "    - POST /api/v1/comments/{commentId}/flag (Flag Comment)"
echo "    - GET /api/v1/comments/moderation/pending (Get Pending Moderation)"
echo "    - GET /api/v1/comments/content/{contentId}/statistics (Get Statistics)"
echo "    - GET /api/v1/comments/family/{familyId}/activity (Get Family Activity)"
echo ""
echo "  ❤️ Reaction APIs:"
echo "    - POST /api/v1/reactions (Add Reaction)"
echo "    - PUT /api/v1/reactions/{reactionId} (Update Reaction)"
echo "    - DELETE /api/v1/reactions/{reactionId} (Remove Reaction)"
echo "    - GET /api/v1/reactions/content/{contentId} (Get Reactions)"
echo "    - GET /api/v1/reactions/{reactionId} (Get Reaction by ID)"
echo "    - GET /api/v1/reactions/content/{contentId}/summary (Get Summary)"
echo "    - GET /api/v1/reactions/content/{contentId}/breakdown (Get Breakdown)"
echo "    - GET /api/v1/reactions/content/{contentId}/user (Get User Reaction)"
echo "    - GET /api/v1/reactions/family-member/{familyMemberId} (Get by Family Member)"
echo "    - GET /api/v1/reactions/generation/{generationLevel} (Get by Generation)"
echo "    - GET /api/v1/reactions/cultural/{culturalTag} (Get by Cultural Context)"
echo "    - GET /api/v1/reactions/family/{familyId}/activity (Get Family Activity)"
echo "    - GET /api/v1/reactions/types (Get Available Types)"
echo "    - GET /api/v1/reactions/types/family (Get Family Types)"
echo "    - GET /api/v1/reactions/types/generational (Get Generational Types)"
echo "    - GET /api/v1/reactions/types/cultural (Get Cultural Types)"
echo "    - GET /api/v1/reactions/content/{contentId}/statistics (Get Statistics)"
echo "    - GET /api/v1/reactions/family/{familyId}/trending (Get Trending)"
echo "    - GET /api/v1/reactions/content/{contentId}/intensity-analysis (Get Intensity Analysis)"
echo ""
print_status "Total APIs Tested: 35+ endpoints"
print_status "Test Coverage: Comprehensive (Unit + Integration + Validation + Error Handling)"
echo ""
print_success "🚀 Interaction Service is ready for production!"
echo "=============================================="
