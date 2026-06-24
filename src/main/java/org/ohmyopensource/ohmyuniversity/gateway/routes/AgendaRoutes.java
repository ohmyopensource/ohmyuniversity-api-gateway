package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for agenda-related endpoints.
 *
 * <p>Covers both personal OhMyU calendar events and university events
 * imported from Cineca. All routes are protected and require a valid OhMyUniversity JWT, enforced
 * by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET    /v1/agenda/events}                   — personal events</li>
 *   <li>{@code POST   /v1/agenda/events}                   — create event</li>
 *   <li>{@code PUT    /v1/agenda/events/{id}}              — update event</li>
 *   <li>{@code DELETE /v1/agenda/events/{id}}              — delete event</li>
 *   <li>{@code GET    /v1/agenda/university-events}        — university events</li>
 *   <li>{@code POST   /v1/agenda/university-events/{id}/import} — import event</li>
 * </ul>
 */
@Component
public class AgendaRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public AgendaRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers agenda routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/agenda/**} are rewritten to
   * {@code /api/v1/agenda/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing agenda routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("agenda", r -> r
            .path("/v1/agenda/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}