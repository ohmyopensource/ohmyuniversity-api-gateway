package org.ohmyopensource.ohmyuniversity.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Spring Security configuration for the API Gateway (WebFlux-based).
 *
 * <p>Security is intentionally kept minimal because authentication is delegated to
 * {@link GatewayJwtFilter}, which performs JWT validation as a global reactive filter.
 *
 * <p>This configuration ensures that Spring Security does not block requests,
 * allowing the gateway filter layer to handle authorization logic consistently.
 *
 * <p>WebFlux is used instead of Spring MVC, therefore {@code @EnableWebFluxSecurity}
 * is required instead of {@code @EnableWebSecurity}.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  // ============ Class Methods ============

  /**
   * Configures the Spring Security filter chain for the reactive API Gateway.
   *
   * <p>Security rules applied:
   * - CSRF protection is disabled (stateless API gateway)
   * - All incoming requests are permitted at Spring Security level
   * - Authentication is delegated to {@link GatewayJwtFilter}
   *
   * @param http the reactive HTTP security configuration
   * @return the configured {@link SecurityWebFilterChain}
   */
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
        .csrf(CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
    return http.build();
  }
}