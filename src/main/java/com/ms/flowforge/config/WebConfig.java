package com.ms.flowforge.config;

import org.springframework.context.annotation.Configuration;

/**
 * Web MVC configuration.
 *
 * CORS is handled entirely by SecurityConfig's CorsConfigurationSource bean,
 * which integrates with the Spring Security filter chain. Defining CORS here
 * as well would create a duplicate configuration conflict.
 *
 * Reserved for future MVC customizations (e.g., message converters, interceptors).
 */
@Configuration
public class WebConfig {
    // Intentionally empty — CORS is managed by SecurityConfig
}