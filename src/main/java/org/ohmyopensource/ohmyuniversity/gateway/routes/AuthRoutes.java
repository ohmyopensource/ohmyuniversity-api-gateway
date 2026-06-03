package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for authentication-related endpoints.
 *
 * <p>These routes are publicly accessible and do not require JWT authentication.
 * Requests are directly forwarded to the Core service.
 *
 * <p>Exposed authentication endpoints:
 * - POST /v1/auth/login
 * - POST /v1/auth/refresh
 * - POST /v1/auth/logout
 */
@Component
public class AuthRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code AuthRoutes} instance.
   *
   * @param coreServiceUrl base URL of the Core service used to forward authentication requests
   */
  public AuthRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers authentication routes into the given Gateway route builder.
   *
   * <p>All incoming requests matching {@code /v1/auth/**} are rewritten to
   * {@code /api/v1/auth/**} and forwarded to the Core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated route builder containing authentication routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("auth-login", r -> r
            .path("/v1/auth/**")
            .filters(f -> f.rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}