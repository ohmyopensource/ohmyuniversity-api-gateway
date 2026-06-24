package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for career academic data endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/career/transcript}     — exam records with grades</li>
 *   <li>{@code GET /v1/career/grades}         — grade averages and CFU statistics</li>
 *   <li>{@code GET /v1/career/study-plan}     — planned academic activities</li>
 *   <li>{@code GET /v1/career/exam-history}   — all attempts grouped by activity</li>
 *   <li>{@code GET /v1/career/recommendations}— prioritised pending exams</li>
 * </ul>
 */
@Component
public class CareerRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public CareerRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers career routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/career/**} are rewritten to
   * {@code /api/v1/career/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing career routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("career", r -> r
            .path("/v1/career/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}