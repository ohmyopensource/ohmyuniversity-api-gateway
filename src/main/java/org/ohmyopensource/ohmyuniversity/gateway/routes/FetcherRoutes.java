package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for fetcher-related endpoints.
 *
 * <p>These routes are publicly accessible and do not require authentication.
 * They expose orientation and analytics data used by clients.
 *
 * <p>Exposed endpoints:
 * - GET /v1/fetcher/**
 */
@Component
public class FetcherRoutes {

  private final String fetcherServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code FetcherRoutes} instance.
   *
   * @param fetcherServiceUrl base URL of the fetcher service used to forward requests
   */
  public FetcherRoutes(
      @Value("${FETCHER_SERVICE_URL:http://localhost:8080}") String fetcherServiceUrl) {
    this.fetcherServiceUrl = fetcherServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers fetcher routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/fetcher/**} are rewritten to
   * {@code /api/v1/fetcher/**} and forwarded to the fetcher service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing fetcher routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("fetcher", r -> r
            .path("/v1/fetcher/**")
            .filters(f -> f.rewritePath("/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(fetcherServiceUrl));
  }
}