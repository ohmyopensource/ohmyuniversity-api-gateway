package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route definitions for integrated calendar endpoints.
 *
 * <p>All calendar routes require a valid OhMyUniversity JWT enforced by
 * {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Routes:
 * - GET    /v1/calendar/events                        — list personal events
 * - POST   /v1/calendar/events                        — create personal event
 * - PUT    /v1/calendar/events/{id}                   — update personal event
 * - DELETE /v1/calendar/events/{id}                   — delete personal event
 * - GET    /v1/calendar/university-events             — list university events
 * - POST   /v1/calendar/university-events/{id}/import — import university event
 */
@Component
public class CalendarRoutes {

  private final String coreServiceUrl;

  /**
   * Creates the route component and injects the core service URL.
   *
   * @param coreServiceUrl base URL of the core microservice
   */
  public CalendarRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  /**
   * Registers calendar routes onto the provided builder.
   *
   * <p>Rewrites {@code /v1/calendar/**} to {@code /api/v1/calendar/**} and forwards to the
   * core service.
   *
   * @param builder the route locator builder to append routes to
   * @return the builder with calendar routes registered
   */
  public Builder register(Builder builder) {
    return builder
        .route("calendar", r -> r
            .path("/v1/calendar/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}