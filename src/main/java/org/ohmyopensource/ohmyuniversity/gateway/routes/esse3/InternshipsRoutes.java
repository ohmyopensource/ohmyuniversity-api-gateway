package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for internship-related endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/internships/applications} — list of internship applications</li>
 * </ul>
 */
@Component
public class InternshipsRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code InternshipsRoutes} instance.
   *
   * @param coreServiceUrl base URL of the core service used to forward internship requests
   */
  public InternshipsRoutes(
      @Value("${core.service.url:http://localhost:8082}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers internship routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/internships/**} are rewritten to
   * {@code /api/v1/internships/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing internship routes
   */
  public RouteLocatorBuilder.Builder register(RouteLocatorBuilder.Builder builder) {
    return builder
        .route("internships", r -> r
            .path("/v1/internships/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}