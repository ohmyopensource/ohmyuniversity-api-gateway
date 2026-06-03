package org.ohmyopensource.ohmyuniversity.gateway.config;

import org.ohmyopensource.ohmyuniversity.gateway.routes.AuthRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.CanteenRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.CarrieraRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.ChatRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.FetcherRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration class for API Gateway route registration.
 *
 * <p>This component aggregates all domain-specific route definitions and exposes them as a single
 * {@link RouteLocator} bean used by Spring Cloud Gateway.
 *
 * <p>Each domain (auth, carriera, canteen, chat, fetcher) is implemented in a dedicated route
 * class under the {@code routes} package. This class is responsible only for orchestration
 * and registration.
 *
 * <p>Exposed API structure:
 * - POST /v1/auth/login → core:8083/api/v1/auth/login
 * - GET /v1/carriera/libretto → core:8083/api/v1/carriera/libretto
 * - GET /v1/canteen/menu → canteen:8082/api/v1/canteen/menu
 * - GET /v1/chat/messages → chat:8081/api/v1/chat/messages
 * - GET /v1/fetcher/stats → fetcher:8080/api/v1/fetcher/stats
 */
@Configuration
public class GatewayRoutesConfig {

  private static final Logger log = LoggerFactory.getLogger(GatewayRoutesConfig.class);

  private final AuthRoutes authRoutes;
  private final CarrieraRoutes carrieraRoutes;
  private final CanteenRoutes canteenRoutes;
  private final ChatRoutes chatRoutes;
  private final FetcherRoutes fetcherRoutes;

  // ============ Constructor ============

  /**
   * Creates a new {@code GatewayRoutesConfig} instance with all domain route registries.
   *
   * @param authRoutes route definitions for authentication endpoints
   * @param carrieraRoutes route definitions for academic/career-related endpoints
   * @param canteenRoutes route definitions for canteen service endpoints
   * @param chatRoutes route definitions for chat service endpoints
   * @param fetcherRoutes route definitions for external data fetching endpoints
   */
  public GatewayRoutesConfig(
      AuthRoutes authRoutes,
      CarrieraRoutes carrieraRoutes,
      CanteenRoutes canteenRoutes,
      ChatRoutes chatRoutes,
      FetcherRoutes fetcherRoutes) {
    this.authRoutes = authRoutes;
    this.carrieraRoutes = carrieraRoutes;
    this.canteenRoutes = canteenRoutes;
    this.chatRoutes = chatRoutes;
    this.fetcherRoutes = fetcherRoutes;
  }

  // ============ Class Methods ============

  /**
   * Builds and registers all API Gateway routes.
   *
   * <p> Each domain-specific route registry contributes its own route definitions to the global
   * {@link RouteLocatorBuilder.Builder}.
   *
   * <p>Registration order:
   * - Authentication routes
   * - Academic/career routes
   * - Canteen routes
   * - Chat routes
   * - Fetcher routes
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return a fully built {@link RouteLocator} containing all registered routes
   */
  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    log.info("GatewayRoutesConfig: registering all routes");

    var b = builder.routes();

    b = authRoutes.register(b);
    b = carrieraRoutes.register(b);
    b = canteenRoutes.register(b);
    b = chatRoutes.register(b);
    b = fetcherRoutes.register(b);

    return b.build();
  }
}