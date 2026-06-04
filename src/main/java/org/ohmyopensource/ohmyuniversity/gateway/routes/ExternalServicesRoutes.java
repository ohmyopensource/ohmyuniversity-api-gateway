package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Gateway route definitions for university external services.
 *
 * <p>This component defines routing rules for endpoints that expose university
 * external integrations through the API Gateway.
 *
 * <p>All routes defined in this class are protected and require a valid
 * OhMyUniversity JWT, enforced by
 * {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed routes:
 * - GET /v1/university/external-services — provides external system URLs
 *       (e.g., Moodle, Library services)
 */
@Component
public class ExternalServicesRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new route configuration instance.
   *
   * @param coreServiceUrl base URL of the core backend service used as target
   *                       for routed requests
   */
  public ExternalServicesRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers university-related routes on the provided route builder.
   *
   * <p>Requests matching {@code /v1/university/**} are forwarded to the core
   * service after path rewriting to {@code /api/v1/university/**}.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing the configured routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("external-services", r -> r
            .path("/v1/university/**")
            .filters(f -> f.rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}