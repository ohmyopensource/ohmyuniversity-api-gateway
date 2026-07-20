package org.ohmyopensource.ohmyuniversity.gateway.config;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Configuration component that defines publicly accessible API Gateway paths.
 *
 * <p>These paths bypass JWT authentication and are evaluated by {@link GatewayJwtFilter}
 * before request processing continues through the gateway filter chain.
 *
 * <p>Public endpoints typically include authentication, documentation, and health check routes.
 */
@Component
public class GatewaySecurityConfig {

  /**
   * List of URI prefixes that are excluded from JWT authentication.
   *
   * <p>These endpoints are accessible without authentication:
   * <ul>
   *   <li>/v1/auth/ -> authentication endpoints</li>
   *   <li>/v1/email/auth/callback -> OAuth2 callback from Microsoft</li>
   *   <li>/v1/fetcher/ -> public analytics and statistics</li>
   *   <li>/actuator/ -> health checks and monitoring endpoints</li>
   *   <li>/swagger-ui -> API documentation UI</li>
   *   <li>/v3/api-docs -> OpenAPI specification</li>
   * </ul>
   *
   */
  private static final List<String> PUBLIC_PREFIXES = List.of(
      "/v1/auth/",
      "/v1/email/auth/callback",
      "/v1/fetcher/",
      "/actuator/",
      "/swagger-ui",
      "/v3/api-docs"
  );

  // ============ Getters | Setters | Bool ============

  /**
   * Determines whether the given request path is publicly accessible.
   *
   * <p>A path is considered public if it matches any configured public prefix.
   * Matching is performed using prefix comparison (startsWith).
   *
   * @param path the HTTP request path to evaluate
   * @return {@code true} if the path does not require JWT authentication, {@code false} otherwise
   */
  public boolean isPublic(String path) {
    return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
  }
}