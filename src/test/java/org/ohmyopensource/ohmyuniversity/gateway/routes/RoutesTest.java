package org.ohmyopensource.ohmyuniversity.gateway.routes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.CareerRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.CourseCatalogueRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ExamsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.FeesRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.InternshipsRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.ProfileRoutes;
import org.ohmyopensource.ohmyuniversity.gateway.routes.esse3.StrutturaRoutes;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

/**
 * Unit tests for all gateway route registrations.
 *
 * <p>Each nested class verifies that the corresponding {@code *Routes} component correctly
 * delegates to the {@link RouteLocatorBuilder.Builder} and returns the updated builder instance.
 * Route predicate and filter correctness is covered by integration tests; these unit tests
 * focus on the registration contract.
 */
class RoutesTest {

  // ============ Helpers ============

  /**
   * Creates a stubbed {@link RouteLocatorBuilder} whose {@code routes()} method returns a
   * mock {@link RouteLocatorBuilder.Builder} that returns itself on every {@code route()} call
   * and returns a mock {@link RouteLocator} on {@code build()}.
   *
   * @return a stubbed {@link RouteLocatorBuilder} ready for use in route registration tests
   */
  private RouteLocatorBuilder stubBuilder() {
    RouteLocatorBuilder rlb = mock(RouteLocatorBuilder.class);
    RouteLocatorBuilder.Builder builder = mock(RouteLocatorBuilder.Builder.class);
    when(rlb.routes()).thenReturn(builder);
    when(builder.route(any(), any())).thenReturn(builder);
    when(builder.build()).thenReturn(mock(RouteLocator.class));
    return rlb;
  }

  /**
   * Verifies {@link AuthRoutes} registration.
   *
   * <p>Covers the public authentication endpoints forwarded to the auth service.
   */
  @Nested
  @DisplayName("AuthRoutes")
  class AuthRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      AuthRoutes routes = new AuthRoutes("http://localhost:8081");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts auth service URL")
    void constructorAcceptsUrl() {
      AuthRoutes routes = new AuthRoutes("http://auth:8081");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link AgendaRoutes} registration.
   *
   * <p>Covers the calendar/agenda endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("AgendaRoutes")
  class AgendaRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      AgendaRoutes routes = new AgendaRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      AgendaRoutes routes = new AgendaRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link CanteenRoutes} registration.
   *
   * <p>Covers the canteen endpoints forwarded to the canteen service.
   */
  @Nested
  @DisplayName("CanteenRoutes")
  class CanteenRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CanteenRoutes routes = new CanteenRoutes("http://localhost:8085");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts canteen service URL")
    void constructorAcceptsUrl() {
      CanteenRoutes routes = new CanteenRoutes("http://canteen:8085");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link CareerRoutes} registration.
   *
   * <p>Covers the career endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("CareerRoutes")
  class CareerRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CareerRoutes routes = new CareerRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      CareerRoutes routes = new CareerRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link ChatRoutes} registration.
   *
   * <p>Covers the chat endpoints forwarded to the chat service.
   */
  @Nested
  @DisplayName("ChatRoutes")
  class ChatRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ChatRoutes routes = new ChatRoutes("http://localhost:8084");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts chat service URL")
    void constructorAcceptsUrl() {
      ChatRoutes routes = new ChatRoutes("http://chat:8084");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link CourseCatalogueRoutes} registration.
   *
   * <p>Covers the Cineca Course Catalogue enrichment endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("CourseCatalogueRoutes")
  class CourseCatalogueRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CourseCatalogueRoutes routes = new CourseCatalogueRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      CourseCatalogueRoutes routes = new CourseCatalogueRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link EmailRoutes} registration.
   *
   * <p>Covers the email endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("EmailRoutes")
  class EmailRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      EmailRoutes routes = new EmailRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      EmailRoutes routes = new EmailRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link ExamsRoutes} registration.
   *
   * <p>Covers the exam-related endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("ExamsRoutes")
  class ExamsRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ExamsRoutes routes = new ExamsRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      ExamsRoutes routes = new ExamsRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link ExternalServicesRoutes} registration.
   *
   * <p>Covers the university external services endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("ExternalServicesRoutes")
  class ExternalServicesRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ExternalServicesRoutes routes = new ExternalServicesRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      ExternalServicesRoutes routes = new ExternalServicesRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link FeesRoutes} registration.
   *
   * <p>Covers the fee-related endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("FeesRoutes")
  class FeesRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      FeesRoutes routes = new FeesRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      FeesRoutes routes = new FeesRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link FetcherRoutes} registration.
   *
   * <p>Covers the public fetcher endpoints forwarded to the fetcher service.
   */
  @Nested
  @DisplayName("FetcherRoutes")
  class FetcherRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      FetcherRoutes routes = new FetcherRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts fetcher service URL")
    void constructorAcceptsUrl() {
      FetcherRoutes routes = new FetcherRoutes("http://fetcher:8083");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link InternshipsRoutes} registration.
   *
   * <p>Covers the internship-related endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("InternshipsRoutes")
  class InternshipsRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      InternshipsRoutes routes = new InternshipsRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      InternshipsRoutes routes = new InternshipsRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link ProfileRoutes} registration.
   *
   * <p>Covers the profile-related endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("ProfileRoutes")
  class ProfileRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ProfileRoutes routes = new ProfileRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      ProfileRoutes routes = new ProfileRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link StrutturaRoutes} registration.
   *
   * <p>Covers the faculty/department/location endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("StrutturaRoutes")
  class StrutturaRoutesTest {

    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      StrutturaRoutes routes = new StrutturaRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      StrutturaRoutes routes = new StrutturaRoutes("http://core:8082");
      assertThat(routes).isNotNull();
    }
  }
}