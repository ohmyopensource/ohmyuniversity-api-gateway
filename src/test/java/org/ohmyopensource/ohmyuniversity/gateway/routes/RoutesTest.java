package org.ohmyopensource.ohmyuniversity.gateway.routes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
   * <p>Covers the public authentication endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("AuthRoutes")
  class AuthRoutesTest {

    /**
     * Verifies that {@link AuthRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      AuthRoutes routes = new AuthRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link AuthRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      AuthRoutes routes = new AuthRoutes("http://core:8083");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link CalendarRoutes} registration.
   *
   * <p>Covers the calendar endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("CalendarRoutes")
  class CalendarRoutesTest {

    /**
     * Verifies that {@link CalendarRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CalendarRoutes routes = new CalendarRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link CalendarRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      CalendarRoutes routes = new CalendarRoutes("http://core:8083");
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

    /**
     * Verifies that {@link CanteenRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CanteenRoutes routes = new CanteenRoutes("http://localhost:8082");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link CanteenRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts canteen service URL")
    void constructorAcceptsUrl() {
      CanteenRoutes routes = new CanteenRoutes("http://canteen:8082");
      assertThat(routes).isNotNull();
    }
  }

  /**
   * Verifies {@link CarrieraRoutes} registration.
   *
   * <p>Covers the career endpoints forwarded to the core service.
   */
  @Nested
  @DisplayName("CarrieraRoutes")
  class CarrieraRoutesTest {

    /**
     * Verifies that {@link CarrieraRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      CarrieraRoutes routes = new CarrieraRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link CarrieraRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      CarrieraRoutes routes = new CarrieraRoutes("http://core:8083");
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

    /**
     * Verifies that {@link ChatRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ChatRoutes routes = new ChatRoutes("http://localhost:8081");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link ChatRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts chat service URL")
    void constructorAcceptsUrl() {
      ChatRoutes routes = new ChatRoutes("http://chat:8081");
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

    /**
     * Verifies that {@link EmailRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      EmailRoutes routes = new EmailRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link EmailRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      EmailRoutes routes = new EmailRoutes("http://core:8083");
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

    /**
     * Verifies that {@link ExternalServicesRoutes#register} calls {@code route()} on the builder
     * exactly once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      ExternalServicesRoutes routes = new ExternalServicesRoutes("http://localhost:8083");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link ExternalServicesRoutes} is instantiated correctly with the injected
     * URL.
     */
    @Test
    @DisplayName("constructor accepts core service URL")
    void constructorAcceptsUrl() {
      ExternalServicesRoutes routes = new ExternalServicesRoutes("http://core:8083");
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

    /**
     * Verifies that {@link FetcherRoutes#register} calls {@code route()} on the builder exactly
     * once and returns the same builder instance.
     */
    @Test
    @DisplayName("register adds one route and returns builder")
    void registersOneRoute() {
      FetcherRoutes routes = new FetcherRoutes("http://localhost:8080");
      RouteLocatorBuilder.Builder builder = stubBuilder().routes();

      RouteLocatorBuilder.Builder result = routes.register(builder);

      verify(builder).route(any(), any());
      assertThat(result).isSameAs(builder);
    }

    /**
     * Verifies that {@link FetcherRoutes} is instantiated correctly with the injected URL.
     */
    @Test
    @DisplayName("constructor accepts fetcher service URL")
    void constructorAcceptsUrl() {
      FetcherRoutes routes = new FetcherRoutes("http://fetcher:8080");
      assertThat(routes).isNotNull();
    }
  }
}