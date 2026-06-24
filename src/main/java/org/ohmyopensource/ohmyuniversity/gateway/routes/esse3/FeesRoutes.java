package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for fee-related endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/fees/status}   — payment status (semaforo) + charges</li>
 *   <li>{@code GET /v1/fees/invoices} — issued invoices</li>
 *   <li>{@code GET /v1/fees/refunds}  — refund records</li>
 *   <li>{@code GET /v1/fees/payments} — PagoPA transaction history</li>
 * </ul>
 */
@Component
public class FeesRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  public FeesRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers fee routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/fees/**} are rewritten to
   * {@code /api/v1/fees/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing fee routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("fees", r -> r
            .path("/v1/fees/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}