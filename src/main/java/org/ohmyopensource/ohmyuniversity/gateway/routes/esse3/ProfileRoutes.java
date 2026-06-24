package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for profile-related endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/profile/persona} — full personal data</li>
 *   <li>{@code GET /v1/profile/info}    — career metadata</li>
 *   <li>{@code GET /v1/profile/avatar}  — profile photo (JPEG)</li>
 *   <li>{@code GET /v1/profile/badge}   — university badge</li>
 * </ul>
 */
@Component
public class ProfileRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public ProfileRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers profile routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/profile/**} are rewritten to
   * {@code /api/v1/profile/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing profile routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("profile", r -> r
            .path("/v1/profile/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}