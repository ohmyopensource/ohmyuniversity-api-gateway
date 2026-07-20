package org.ohmyopensource.ohmyuniversity.gateway.routes.esse3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for Cineca Course Catalogue enrichment endpoints.
 *
 * <p>All routes are protected and require a valid OhMyUniversity JWT,
 * enforced by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed endpoints:
 * <ul>
 *   <li>{@code GET /v1/course-catalogue/plan}     — full multi-year course plan for the
 *   student's cohort</li>
 *   <li>{@code GET /v1/course-catalogue/syllabus} — prerequisites and CFU breakdown for one
 *   activity</li>
 * </ul>
 */
@Component
public class CourseCatalogueRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code CourseCatalogueRoutes} instance.
   *
   * @param coreServiceUrl base URL of the core service used to forward course catalogue requests
   */
  public CourseCatalogueRoutes(
      @Value("${core.service.url:http://localhost:8082}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers course catalogue routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/course-catalogue/**} are rewritten
   * to {@code /api/v1/course-catalogue/**} and forwarded to the core service.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing course catalogue routes
   */
  public RouteLocatorBuilder.Builder register(RouteLocatorBuilder.Builder builder) {
    return builder
        .route("course-catalogue", r -> r
            .path("/v1/course-catalogue/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}