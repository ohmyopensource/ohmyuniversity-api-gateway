package org.ohmyopensource.ohmyuniversity.gateway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * CORS configuration for the API Gateway.
 *
 * <p>Defines allowed origins, methods, and headers for cross-origin requests.
 * Applied globally to all gateway routes via {@link SecurityConfig}.
 *
 * <p>Allowed origins cover:
 * - Angular dev server (localhost:4200)
 * - Alternative local dev port (localhost:3000)
 * - Production and staging web clients (ohmyuniversity.it subdomains)
 */
@Configuration
public class CorsConfig {

    /**
     * Defines CORS rules applied to all gateway routes.
     *
     * @return the reactive CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:3000",
                "https://ohmyuniversity.it",
                "https://www.ohmyuniversity.it",
                "https://staging.ohmyuniversity.it"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}