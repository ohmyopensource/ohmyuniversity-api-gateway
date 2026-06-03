package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for canteen-related endpoints.
 *
 * <p>These routes are protected and require a valid OhMyUniversity JWT.
 * Authentication is enforced
 * by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed canteen endpoints:
 * - GET /v1/canteen/menu
 * - POST /v1/canteen/orders
 * - GET /v1/canteen/orders
 */
@Component
public class CanteenRoutes {

  private final String canteenServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code CanteenRoutes} instance.
   *
   * @param canteenServiceUrl base URL of the canteen service used for routing requests
   */
  public CanteenRoutes(
      @Value("${CANTEEN_SERVICE_URL:http://localhost:8082}") String canteenServiceUrl) {
    this.canteenServiceUrl = canteenServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers canteen routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/canteen/**} are rewritten to
   * {@code /api/v1/canteen/**} and forwarded to the canteen service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing canteen routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("canteen", r -> r
            .path("/v1/canteen/**")
            .filters(f -> f.rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(canteenServiceUrl));
  }
}