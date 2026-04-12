package com.bootcamp.onlineschool.config;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.TeacherRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig demonstrates Spring Boot configuration
 * 
 * Demonstrates:
 * - @Configuration annotation
 * - @Bean annotation for bean creation
 * - Dependency injection configuration
 * - Application setup
 */
@Configuration
public class AppConfig {
    
    /**
     * Create StudentRegistry bean
     * This bean will be automatically injected into services that need it
     */
    @Bean
    public StudentRegistry studentRegistry() {
        return new StudentRegistry();
    }

    /**
     * Create TeacherRegistry bean
     * This bean will be automatically injected into services that need it
     */
    @Bean
    public TeacherRegistry teacherRegistry() {
        return new TeacherRegistry();
    }
}
