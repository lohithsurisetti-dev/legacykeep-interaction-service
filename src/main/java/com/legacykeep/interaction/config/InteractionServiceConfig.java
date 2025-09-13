package com.legacykeep.interaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Interaction Service Configuration
 * 
 * Configuration class for the Interaction Service.
 * Sets up JPA repositories, transaction management, and other service configurations.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Configuration
@EnableTransactionManagement
public class InteractionServiceConfig {
    
    /**
     * Default pageable configuration for pagination
     */
    @Bean
    public Pageable defaultPageable() {
        return PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    /**
     * Small pageable configuration for small result sets
     */
    @Bean
    public Pageable smallPageable() {
        return PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    /**
     * Large pageable configuration for large result sets
     */
    @Bean
    public Pageable largePageable() {
        return PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
