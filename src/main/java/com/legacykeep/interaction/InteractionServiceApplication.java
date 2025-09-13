package com.legacykeep.interaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * LegacyKeep Interaction Service Application
 * 
 * Main application class for the Interaction Service that handles all user interactions
 * with legacy content including comments, reactions, ratings, shares, bookmarks, and more.
 * 
 * Features:
 * - Instagram-scale interaction handling
 * - Real-time WebSocket updates
 * - Kafka event publishing
 * - Redis caching for performance
 * - JWT authentication
 * - Comprehensive analytics
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 * @since 2025-01-09
 */
@SpringBootApplication(scanBasePackages = "com.legacykeep.interaction")
@EnableCaching
@EnableKafka
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.legacykeep.interaction.repository")
public class InteractionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteractionServiceApplication.class, args);
    }
}
