# 🛠️ Interaction Service Development Guide

## 📋 **Getting Started**

This guide provides comprehensive instructions for setting up, developing, and testing the Interaction Service. Follow these steps to get your development environment up and running.

## 🔧 **Prerequisites**

### **Required Software**
- **Java 17+** - OpenJDK or Oracle JDK
- **Maven 3.8+** - Build and dependency management
- **PostgreSQL 15+** - Primary database
- **Redis 7+** - Caching and session management
- **Apache Kafka 3.5+** - Message queue for events
- **Docker & Docker Compose** - Containerization (optional)

### **Development Tools**
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code
- **Git** - Version control
- **Postman/Insomnia** - API testing
- **pgAdmin** - PostgreSQL administration (optional)

## 🚀 **Quick Start**

### **1. Clone Repository**
```bash
git clone https://github.com/lohithsurisetti-dev/legacykeep-interaction-service.git
cd legacykeep-interaction-service
```

### **2. Database Setup**
```bash
# Create database
createdb interaction_db

# Run migrations
mvn flyway:migrate
```

### **3. Start Dependencies**
```bash
# Start Redis
redis-server

# Start Kafka (with Zookeeper)
kafka-server-start.sh config/server.properties
```

### **4. Run Application**
```bash
mvn spring-boot:run
```

### **5. Verify Setup**
```bash
# Health check
curl http://localhost:8086/api/v1/interactions/health

# Expected response
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2025-01-09T10:30:00Z",
    "version": "1.0.0"
  }
}
```

## 🏗️ **Project Structure**

```
interaction-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/legacykeep/interaction/
│   │   │       ├── InteractionServiceApplication.java
│   │   │       ├── config/                 # Configuration classes
│   │   │       ├── controller/             # REST controllers
│   │   │       ├── service/                # Business logic
│   │   │       ├── repository/             # Data access layer
│   │   │       ├── entity/                 # JPA entities
│   │   │       ├── dto/                    # Data transfer objects
│   │   │       ├── exception/              # Custom exceptions
│   │   │       ├── event/                  # Kafka events
│   │   │       ├── security/               # Security configuration
│   │   │       └── util/                   # Utility classes
│   │   └── resources/
│   │       ├── application.properties      # Application configuration
│   │       ├── db/migration/               # Flyway migrations
│   │       └── static/                     # Static resources
│   └── test/
│       ├── java/                           # Test classes
│       └── resources/                      # Test resources
├── docs/                                   # Documentation
├── pom.xml                                # Maven configuration
└── README.md                              # Project documentation
```

## ⚙️ **Configuration**

### **Application Properties**
```properties
# Server Configuration
server.port=8086
server.servlet.context-path=/api/v1

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/interaction_db
spring.datasource.username=legacykeep
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=2000ms
spring.data.redis.lettuce.pool.max-active=8
spring.data.redis.lettuce.pool.max-idle=8
spring.data.redis.lettuce.pool.min-idle=0

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
spring.kafka.producer.batch-size=16384
spring.kafka.producer.linger-ms=5
spring.kafka.producer.buffer-memory=33554432

# JWT Configuration
jwt.secret=your-jwt-secret-key
jwt.expiration=900000
jwt.header=Authorization
jwt.prefix=Bearer

# Service URLs
legacy.service.url=http://localhost:8085/api/v1
relationship.service.url=http://localhost:8084/api/v1
notification.service.url=http://localhost:8083/api/v1
auth.service.url=http://localhost:8081/api/v1

# Logging Configuration
logging.level.com.legacykeep.interaction=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Management and Monitoring
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

### **Environment-specific Configuration**

#### **Development (application-dev.properties)**
```properties
# Development-specific settings
spring.jpa.show-sql=true
logging.level.com.legacykeep.interaction=DEBUG
spring.flyway.clean-disabled=false
```

#### **Testing (application-test.properties)**
```properties
# Test-specific settings
spring.datasource.url=jdbc:postgresql://localhost:5432/interaction_db_test
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false
```

#### **Production (application-prod.properties)**
```properties
# Production-specific settings
spring.jpa.show-sql=false
logging.level.com.legacykeep.interaction=INFO
management.endpoints.web.exposure.include=health,info
```

## 🗄️ **Database Setup**

### **1. Create Database**
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE interaction_db;

-- Create user (optional)
CREATE USER legacykeep WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE interaction_db TO legacykeep;
```

### **2. Run Migrations**
```bash
# Using Maven
mvn flyway:migrate

# Using Flyway CLI
flyway -url=jdbc:postgresql://localhost:5432/interaction_db -user=legacykeep -password=password migrate
```

### **3. Verify Schema**
```sql
-- Connect to database
psql -U legacykeep -d interaction_db

-- List tables
\dt

-- Check table structure
\d interaction_types
\d content_interactions
\d comments
```

## 🔧 **Development Workflow**

### **1. Feature Development**
```bash
# Create feature branch
git checkout -b feature/comment-system

# Make changes
# ... implement feature ...

# Run tests
mvn test

# Commit changes
git add .
git commit -m "feat: implement comment system with replies and mentions"

# Push branch
git push origin feature/comment-system
```

### **2. Database Changes**
```bash
# Create migration file
touch src/main/resources/db/migration/V2__Add_comment_replies.sql

# Add migration content
# ... SQL migration ...

# Test migration
mvn flyway:migrate

# Verify changes
psql -U legacykeep -d interaction_db -c "\d comments"
```

### **3. API Development**
```bash
# Start application
mvn spring-boot:run

# Test API endpoints
curl -X POST http://localhost:8086/api/v1/interactions/comments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "contentId": "uuid",
    "commentText": "Great story!",
    "mentions": ["user-uuid-1"]
  }'
```

## 🧪 **Testing**

### **Unit Tests**
```java
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    
    @Mock
    private CommentRepository commentRepository;
    
    @InjectMocks
    private CommentServiceImpl commentService;
    
    @Test
    void shouldCreateCommentSuccessfully() {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
            .contentId("uuid")
            .commentText("Great story!")
            .build();
        
        Comment comment = Comment.builder()
            .id(1L)
            .contentId("uuid")
            .commentText("Great story!")
            .build();
        
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        
        // When
        CommentResponse response = commentService.createComment(request, "user-uuid");
        
        // Then
        assertThat(response.getCommentText()).isEqualTo("Great story!");
        verify(commentRepository).save(any(Comment.class));
    }
}
```

### **Integration Tests**
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CommentControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("interaction_db_test")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateCommentViaApi() {
        // Given
        CreateCommentRequest request = CreateCommentRequest.builder()
            .contentId("uuid")
            .commentText("Great story!")
            .build();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("valid-jwt-token");
        HttpEntity<CreateCommentRequest> entity = new HttpEntity<>(request, headers);
        
        // When
        ResponseEntity<ApiResponse<CommentResponse>> response = restTemplate.exchange(
            "/interactions/comments",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<ApiResponse<CommentResponse>>() {}
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().getCommentText()).isEqualTo("Great story!");
    }
}
```

### **Running Tests**
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CommentServiceTest

# Run tests with coverage
mvn test jacoco:report

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

## 🔍 **Debugging**

### **Application Logs**
```bash
# View application logs
tail -f logs/application.log

# Filter logs by level
grep "ERROR" logs/application.log

# Filter logs by component
grep "CommentService" logs/application.log
```

### **Database Debugging**
```sql
-- Check active connections
SELECT * FROM pg_stat_activity WHERE datname = 'interaction_db';

-- Check table sizes
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables WHERE schemaname = 'public' ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Check slow queries
SELECT query, mean_time, calls FROM pg_stat_statements ORDER BY mean_time DESC LIMIT 10;
```

### **Kafka Debugging**
```bash
# List topics
kafka-topics.sh --bootstrap-server localhost:9092 --list

# Consume messages
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic interaction-events --from-beginning

# Check consumer groups
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

## 📊 **Performance Monitoring**

### **Application Metrics**
```bash
# Health check
curl http://localhost:8086/actuator/health

# Metrics
curl http://localhost:8086/actuator/metrics

# Prometheus metrics
curl http://localhost:8086/actuator/prometheus
```

### **Database Performance**
```sql
-- Check query performance
EXPLAIN ANALYZE SELECT * FROM comments WHERE content_id = 'uuid';

-- Check index usage
SELECT schemaname, tablename, indexname, idx_scan, idx_tup_read, idx_tup_fetch
FROM pg_stat_user_indexes ORDER BY idx_scan DESC;

-- Check table statistics
SELECT schemaname, tablename, n_tup_ins, n_tup_upd, n_tup_del, n_live_tup, n_dead_tup
FROM pg_stat_user_tables ORDER BY n_live_tup DESC;
```

## 🚀 **Deployment**

### **Docker Deployment**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/interaction-service-1.0.0.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# Build Docker image
docker build -t legacykeep-interaction-service .

# Run container
docker run -p 8086:8086 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/interaction_db \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  legacykeep-interaction-service
```

### **Production Configuration**
```properties
# Production settings
spring.profiles.active=prod
server.port=8086
management.endpoints.web.exposure.include=health,info,metrics

# Database connection pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# Redis connection pool
spring.data.redis.lettuce.pool.max-active=20
spring.data.redis.lettuce.pool.max-idle=10
spring.data.redis.lettuce.pool.min-idle=5

# Kafka producer settings
spring.kafka.producer.retries=3
spring.kafka.producer.batch-size=16384
spring.kafka.producer.linger-ms=5
```

## 🔧 **Troubleshooting**

### **Common Issues**

#### **Database Connection Issues**
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Check connection
psql -U legacykeep -d interaction_db -c "SELECT 1;"

# Check logs
tail -f /var/log/postgresql/postgresql-15-main.log
```

#### **Redis Connection Issues**
```bash
# Check Redis status
redis-cli ping

# Check Redis logs
tail -f /var/log/redis/redis-server.log

# Check Redis memory usage
redis-cli info memory
```

#### **Kafka Connection Issues**
```bash
# Check Kafka status
kafka-topics.sh --bootstrap-server localhost:9092 --list

# Check Kafka logs
tail -f logs/server.log

# Check Zookeeper status
echo "ruok" | nc localhost 2181
```

#### **Application Startup Issues**
```bash
# Check application logs
tail -f logs/application.log

# Check JVM memory
jps -v

# Check port usage
netstat -tlnp | grep 8086
```

### **Performance Issues**

#### **Slow Database Queries**
```sql
-- Enable query logging
ALTER SYSTEM SET log_statement = 'all';
ALTER SYSTEM SET log_min_duration_statement = 1000;
SELECT pg_reload_conf();

-- Check slow queries
SELECT query, mean_time, calls FROM pg_stat_statements ORDER BY mean_time DESC LIMIT 10;
```

#### **High Memory Usage**
```bash
# Check JVM memory usage
jstat -gc <pid> 1s

# Check application memory
curl http://localhost:8086/actuator/metrics/jvm.memory.used

# Check Redis memory
redis-cli info memory
```

## 📚 **Best Practices**

### **Code Quality**
- **Follow Java Conventions** - Use standard Java naming and formatting
- **Write Tests** - Maintain high test coverage
- **Document Code** - Add JavaDoc for public methods
- **Use Constants** - Define constants for magic numbers and strings
- **Handle Exceptions** - Proper exception handling and logging

### **Database Best Practices**
- **Use Transactions** - Wrap related operations in transactions
- **Optimize Queries** - Use proper indexes and query optimization
- **Handle Concurrency** - Use optimistic locking for concurrent updates
- **Monitor Performance** - Regular performance monitoring and optimization

### **API Design**
- **RESTful Design** - Follow REST principles
- **Consistent Responses** - Use consistent response formats
- **Proper HTTP Status Codes** - Use appropriate status codes
- **Input Validation** - Validate all input parameters
- **Rate Limiting** - Implement rate limiting for API endpoints

### **Security**
- **JWT Validation** - Validate JWT tokens for all requests
- **Input Sanitization** - Sanitize all user inputs
- **SQL Injection Prevention** - Use parameterized queries
- **Audit Logging** - Log all security-relevant events

---

*This development guide provides comprehensive instructions for developing and maintaining the Interaction Service.*
