package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for exam-related endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET  /v1/exams/sessions}        — available sessions (calesa)</li>
 *   <li>{@code GET  /v1/exams/bookable}         — bookable sessions (libretto)</li>
 *   <li>{@code GET  /v1/exams/bookings}         — active upcoming bookings</li>
 *   <li>{@code POST /v1/exams/bookings/legacy}  — full history (Basic Auth)</li>
 *   <li>{@code GET  /v1/exams/surveys}          — teaching evaluation surveys</li>
 *   <li>{@code POST /v1/exams/bookings}         — book an exam (Basic Auth)</li>
 *   <li>{@code POST /v1/exams/bookings/cancel}  — cancel a booking (Basic Auth)</li>
 * </ul>
 */
@Component
public class ExamsRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public ExamsRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers exam routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/exams/**} are rewritten to
   * {@code /api/v1/exams/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing exam routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("exams", r -> r
            .path("/v1/exams/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}