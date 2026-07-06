package com.ms.flowforge.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Infrastructure beans for the AI gateway integration.
 *
 * Key design decisions:
 * - RestTemplate uses configured timeouts to prevent indefinite blocking on slow AI calls
 * - ObjectMapper configured with SNAKE_CASE naming to match Python service contracts
 * - A separate ObjectMapper bean is exposed so services can use it for JSON serialization
 */
@Configuration
public class AiGatewayConfig {

    @Value("${spring.ai.python-service.connect-timeout:30000}")
    private long connectTimeoutMs;

    @Value("${spring.ai.python-service.read-timeout:300000}")
    private long readTimeoutMs;

    /**
     * RestTemplate for Python AI service calls — with configured timeouts.
     * The qualifier "aiRestTemplate" ensures this doesn't conflict with
     * any other RestTemplate beans.
     */
    @Bean("aiRestTemplate")
    public RestTemplate aiRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
                .additionalMessageConverters(
                    new MappingJackson2HttpMessageConverter(snakeCaseObjectMapper())
                )
                .build();
    }

    /**
     * Primary ObjectMapper bean — used across the application.
     * Configured with JavaTimeModule for LocalDateTime serialization.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Snake_case ObjectMapper — used exclusively for Python AI service communication.
     * Python services use snake_case field naming (project_id, not projectId).
     */
    @Bean("snakeCaseObjectMapper")
    public ObjectMapper snakeCaseObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}