package org.ohmyopensource.ohmyuniversity.gateway.config;

import org.ohmyopensource.ohmyuniversity.gateway.routes.AgendaRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.AuthRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.CanteenRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.ChatRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.EmailRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.ExternalServicesRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.FetcherRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.CareerRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.CourseCatalogueRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ExamsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.FeesRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.InternshipsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ProfileRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.StrutturaRoutes;
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
 *   POST /v1/auth/**               → auth:8081
 *
 * Profile (ESSE3 — anagrafica-service, carriere-service, badge-service)
 *   GET  /v1/profile/persona       → core:8082
 *   GET  /v1/profile/info          → core:8082
 *   GET  /v1/profile/avatar        → core:8082
 *   GET  /v1/profile/badge         → core:8082
 *
 * Career (ESSE3 — libretto-service, piani-service)
 *   GET  /v1/career/transcript     → core:8082
 *   GET  /v1/career/grades         → core:8082
 *   GET  /v1/career/study-plan     → core:8082
 *   GET  /v1/career/exam-history   → core:8082
 *   GET  /v1/career/recommendations→ core:8082
 *
 * Exams (ESSE3 — calesa-service, libretto-service, questionari-service)
 *   GET  /v1/exams/sessions        → core:8082
 *   GET  /v1/exams/bookable        → core:8082
 *   GET  /v1/exams/bookings        → core:8082
 *   POST /v1/exams/bookings/legacy → core:8082
 *   GET  /v1/exams/surveys         → core:8082
 *   POST /v1/exams/bookings          → core:8082
 *   POST /v1/exams/bookings/cancel   → core:8082
 *
 * Fees (ESSE3 — tasse-service)
 *   GET  /v1/fees/status           → core:8082
 *   GET  /v1/fees/invoices         → core:8082
 *   GET  /v1/fees/refunds          → core:8082
 *   GET  /v1/fees/payments         → core:8082
 *
 * Internships (ESSE3 — tirocini-service)
 *   GET  /v1/internships/applications → core:8082
 *
 * Struttura (ESSE3 — struttura-service, public data)
 *   GET  /v1/struttura/facolta         → core:8082
 *   GET  /v1/struttura/sedi/{sedeId}   → core:8082
 *
 * Course Catalogue (Cineca Course Catalogue — separate product from ESSE3)
 *   GET  /v1/course-catalogue/plan      → core:8082
 *   GET  /v1/course-catalogue/syllabus  → core:8082
 *
 * Agenda (OhMyU native + university events)
 *   GET|POST|PUT|DELETE /v1/agenda/events/**            → core:8082
 *   GET|POST            /v1/agenda/university-events/** → core:8082
 *
 * Email
 *   /v1/email/**                   → core:8082
 *
 * External services
 *   /v1/university/**              → core:8082
 *
 * Canteen
 *   /v1/canteen/**                 → canteen:8085
 *
 * Chat
 *   /v1/chat/**                    → chat:8084
 *
 * Fetcher (public)
 *   /v1/fetcher/**                 → fetcher:8083
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
  private final CourseCatalogueRoutes courseCatalogueRoutes;
  private final StrutturaRoutes strutturaRoutes;

  // OhMyU-native routes
  private final AuthRoutes authRoutes;
  private final AgendaRoutes agendaRoutes;
  private final ExternalServicesRoutes externalServicesRoutes;
  private final EmailRoutes emailRoutes;
  private final CanteenRoutes canteenRoutes;
  private final ChatRoutes chatRoutes;
  private final FetcherRoutes fetcherRoutes;

  // ============ Constructor ============

  /**
   * Creates the gateway routes configuration and injects every domain-specific {@code *Routes}
   * component. All routes are aggregated and registered together by
   * {@link #routes(RouteLocatorBuilder)}.
   *
   * @param profileRoutes          profile-related ESSE3 routes
   * @param careerRoutes           career-related ESSE3 routes
   * @param examsRoutes            exam-related ESSE3 routes
   * @param feesRoutes             fee-related ESSE3 routes
   * @param internshipsRoutes      internship-related ESSE3 routes
   * @param courseCatalogueRoutes  Cineca Course Catalogue enrichment routes
   * @param strutturaRoutes        faculty/department/location ESSE3 routes
   * @param authRoutes             authentication routes (public, forwarded to the auth service)
   * @param agendaRoutes           personal and university calendar routes
   * @param externalServicesRoutes university external services routes
   * @param emailRoutes            institutional email integration routes
   * @param canteenRoutes          canteen service routes
   * @param chatRoutes             chat service routes
   * @param fetcherRoutes          public fetcher service routes
   */
  public GatewayRoutesConfig(
      ProfileRoutes profileRoutes,
      CareerRoutes careerRoutes,
      ExamsRoutes examsRoutes,
      FeesRoutes feesRoutes,
      InternshipsRoutes internshipsRoutes,
      CourseCatalogueRoutes courseCatalogueRoutes,
      StrutturaRoutes strutturaRoutes,
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
    this.courseCatalogueRoutes = courseCatalogueRoutes;
    this.strutturaRoutes = strutturaRoutes;
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
   * <p>Registration order:
   * <ol>
   *   <li>auth first (public)</li>
   *   <li>then ESSE3 routes</li>
   *   <li>then OhMyU-native routes</li>
   *   <li>then infrastructure routes (fetcher)</li>
   * </ol>
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
    b = courseCatalogueRoutes.register(b);
    b = strutturaRoutes.register(b);

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