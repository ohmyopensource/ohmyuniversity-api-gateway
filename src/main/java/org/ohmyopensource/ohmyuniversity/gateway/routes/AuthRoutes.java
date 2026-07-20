package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for authentication-related endpoints.
 *
 * <p>These routes are publicly accessible and do not require JWT authentication.
 * Requests are forwarded directly to the Auth service — authentication and identity/session
 * management were extracted out of Core into their own microservice;
 *
 * <p>Exposed authentication endpoints:
 * <ul>
 *   <li>POST /v1/auth/login</li>
 *   <li>POST /v1/auth/refresh</li>
 *   <li>POST /v1/auth/logout</li>
 *   <li>POST /v1/auth/switch-carriera</li>
 *   <li>POST /v1/auth/switch-university</li>
 *   <li>GET  /v1/auth/sessions</li>
 *   <li>DELETE /v1/auth/sessions/{sessionId}</li>
 * </ul>
 *
 */
@Component
public class AuthRoutes {

  private final String authServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code AuthRoutes} instance.
   *
   * @param authServiceUrl base URL of the Auth service used to forward authentication requests
   */
  public AuthRoutes(
      @Value("${auth.service.url:http://localhost:8081}") String authServiceUrl) {
    this.authServiceUrl = authServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers authentication routes into the given Gateway route builder.
   *
   * <p>All incoming requests matching {@code /v1/auth/**} are rewritten to
   * {@code /api/v1/auth/**} and forwarded to the Auth service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated route builder containing authentication routes
   */
  public RouteLocatorBuilder.Builder register(RouteLocatorBuilder.Builder builder) {
    return builder
        .route("auth-login", r -> r
            .path("/v1/auth/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(authServiceUrl));
  }
}