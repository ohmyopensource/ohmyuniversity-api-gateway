package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for faculty/department/location endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/struttura/facolta}      — all faculties/departments</li>
 *   <li>{@code GET /v1/struttura/sedi/{sedeId}} — single location detail</li>
 * </ul>
 */
@Component
public class StrutturaRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public StrutturaRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers struttura routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/struttura/**} are rewritten to
   * {@code /api/v1/struttura/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing struttura routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("struttura", r -> r
            .path("/v1/struttura/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}