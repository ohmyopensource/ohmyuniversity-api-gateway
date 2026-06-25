package org.ohmyopensource.ohmyuniversity.gateway.config;

import org.ohmyopensource.ohmyuniversity.gateway.routes.AgendaRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.AuthRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.CanteenRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.ChatRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.EmailRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.ExternalServicesRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.FetcherRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.CareerRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ExamsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.FeesRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.InternshipsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ProfileRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration class for API Gateway route registration.
 *
 * <p>Aggregates all domain-specific route definitions and exposes them as a single
 * {@link RouteLocator} bean used by Spring Cloud Gateway.
 *
 * <p>ESSE3-backed routes live under {@code routes/esse3/} and map to the core service.
 * OhMyU-native routes live directly under {@code routes/}.
 *
 * <p>Exposed API structure:
 * <pre>
 * Auth
 *   POST /v1/auth/**               → core:8083
 *
 * Profile (ESSE3 — anagrafica-service, carriere-service, badge-service)
 *   GET  /v1/profile/persona       → core:8083
 *   GET  /v1/profile/info          → core:8083
 *   GET  /v1/profile/avatar        → core:8083
 *   GET  /v1/profile/badge         → core:8083
 *
 * Career (ESSE3 — libretto-service, piani-service)
 *   GET  /v1/career/transcript     → core:8083
 *   GET  /v1/career/grades         → core:8083
 *   GET  /v1/career/study-plan     → core:8083
 *   GET  /v1/career/exam-history   → core:8083
 *   GET  /v1/career/recommendations→ core:8083
 *
 * Exams (ESSE3 — calesa-service, libretto-service, questionari-service)
 *   GET  /v1/exams/sessions        → core:8083
 *   GET  /v1/exams/bookable        → core:8083
 *   GET  /v1/exams/bookings        → core:8083
 *   POST /v1/exams/bookings/legacy → core:8083
 *   GET  /v1/exams/surveys         → core:8083
 *
 * Fees (ESSE3 — tasse-service)
 *   GET  /v1/fees/status           → core:8083
 *   GET  /v1/fees/invoices         → core:8083
 *   GET  /v1/fees/refunds          → core:8083
 *   GET  /v1/fees/payments         → core:8083
 *
 * Internships (ESSE3 — tirocini-service)
 *   GET  /v1/internships/applications → core:8083
 *
 * Agenda (OhMyU native + university events)
 *   GET|POST|PUT|DELETE /v1/agenda/events/**            → core:8083
 *   GET|POST            /v1/agenda/university-events/** → core:8083
 *
 * Email
 *   /v1/email/**                   → core:8083
 *
 * External services
 *   /v1/university/**              → core:8083
 *
 * Canteen
 *   /v1/canteen/**                 → canteen:8082
 *
 * Chat
 *   /v1/chat/**                    → chat:8081
 *
 * Fetcher (public)
 *   /v1/fetcher/**                 → fetcher:8084
 * </pre>
 */
@Configuration
public class GatewayRoutesConfig {

  private static final Logger log = LoggerFactory.getLogger(GatewayRoutesConfig.class);

  // ESSE3-backed routes
  private final ProfileRoutes profileRoutes;
  private final CareerRoutes careerRoutes;
  private final ExamsRoutes examsRoutes;
  private final FeesRoutes feesRoutes;
  private final InternshipsRoutes internshipsRoutes;

  // OhMyU-native routes
  private final AuthRoutes authRoutes;
  private final AgendaRoutes agendaRoutes;
  private final ExternalServicesRoutes externalServicesRoutes;
  private final EmailRoutes emailRoutes;
  private final CanteenRoutes canteenRoutes;
  private final ChatRoutes chatRoutes;
  private final FetcherRoutes fetcherRoutes;

  // ============ Constructor ============

  public GatewayRoutesConfig(
      ProfileRoutes profileRoutes,
      CareerRoutes careerRoutes,
      ExamsRoutes examsRoutes,
      FeesRoutes feesRoutes,
      InternshipsRoutes internshipsRoutes,
      AuthRoutes authRoutes,
      AgendaRoutes agendaRoutes,
      ExternalServicesRoutes externalServicesRoutes,
      EmailRoutes emailRoutes,
      CanteenRoutes canteenRoutes,
      ChatRoutes chatRoutes,
      FetcherRoutes fetcherRoutes) {
    this.profileRoutes = profileRoutes;
    this.careerRoutes = careerRoutes;
    this.examsRoutes = examsRoutes;
    this.feesRoutes = feesRoutes;
    this.internshipsRoutes = internshipsRoutes;
    this.authRoutes = authRoutes;
    this.agendaRoutes = agendaRoutes;
    this.externalServicesRoutes = externalServicesRoutes;
    this.emailRoutes = emailRoutes;
    this.canteenRoutes = canteenRoutes;
    this.chatRoutes = chatRoutes;
    this.fetcherRoutes = fetcherRoutes;
  }

  // ============ Class Methods ============

  /**
   * Builds and registers all API Gateway routes.
   *
   * <p>Registration order: auth first (public), then ESSE3 routes,
   * then OhMyU-native routes, then infrastructure routes (fetcher).
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return a fully built {@link RouteLocator} containing all registered routes
   */
  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    log.info("GatewayRoutesConfig: registering all routes");

    var b = builder.routes();

    // Auth (public endpoints)
    b = authRoutes.register(b);

    // ESSE3-backed routes
    b = profileRoutes.register(b);
    b = careerRoutes.register(b);
    b = examsRoutes.register(b);
    b = feesRoutes.register(b);
    b = internshipsRoutes.register(b);

    // OhMyU-native routes
    b = agendaRoutes.register(b);
    b = externalServicesRoutes.register(b);
    b = emailRoutes.register(b);

    // Other microservices
    b = canteenRoutes.register(b);
    b = chatRoutes.register(b);
    b = fetcherRoutes.register(b);

    return b.build();
  }
}