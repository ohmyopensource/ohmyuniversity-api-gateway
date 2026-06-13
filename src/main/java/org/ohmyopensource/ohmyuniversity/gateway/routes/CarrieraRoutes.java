package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for student career-related endpoints.
 *
 * <p>These routes are protected and require a valid OhMyUniversity JWT.
 * Authentication is enforced
 * by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed career endpoints:
 * - GET /v1/carriera/libretto
 * - GET /v1/carriera/medie
 * - GET /v1/carriera/piano
 * - GET /v1/carriera/appelli
 * - POST /v1/carriera/prenotazioni
 * - GET /v1/carriera/tasse
 * - GET /v1/carriera/badge
 */
@Component
public class CarrieraRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code CarrieraRoutes} instance.
   *
   * @param coreServiceUrl base URL of the core service used to forward career-related requests
   */
  public CarrieraRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers career routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/carriera/**} are rewritten to
   * {@code /api/v1/carriera/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing career routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("carriera", r -> r
            .path("/v1/carriera/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}