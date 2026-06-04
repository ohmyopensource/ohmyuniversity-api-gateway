package org.ohmyopensource.ohmyuniversity.gateway.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GatewaySecurityConfig}.
 *
 * <p>Verifies the classification of request paths into public and protected
 * endpoints as defined by the gateway security configuration.
 *
 * <p>Test coverage includes validation of known public routes, verification
 * that protected routes are correctly identified, and checks for edge cases
 * such as empty or similar-looking paths.
 *
 * <p>The configuration is tested in isolation without loading a Spring
 * application context.
 */
class GatewaySecurityConfigTest {

  private GatewaySecurityConfig securityConfig;

  /**
   * Initializes the test fixture before each test execution.
   *
   * <p>Creates a new instance of {@link GatewaySecurityConfig} used to verify
   * path classification rules.
   */
  @BeforeEach
  void setUp() {
    securityConfig = new GatewaySecurityConfig();
  }

  /**
   * Groups test cases for endpoints classified as publicly accessible.
   *
   * <p>Verifies that all configured public paths are correctly identified by
   * {@link GatewaySecurityConfig#isPublic(String)} and are eligible to bypass
   * authentication.
   */
  @Nested
  @DisplayName("Public paths — isPublic() must return true")
  class PublicPaths {

    /**
     * Verifies that the login endpoint is classified as publicly accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the authentication login route, allowing it to bypass authentication.
     */
    @Test
    @DisplayName("/v1/auth/login is public")
    void authLoginIsPublic() {
      assertThat(securityConfig.isPublic("/v1/auth/login")).isTrue();
    }

    /**
     * Verifies that the refresh endpoint is classified as publicly accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the authentication refresh route, allowing token renewal without prior
     * authentication.
     */
    @Test
    @DisplayName("/v1/auth/refresh is public")
    void authRefreshIsPublic() {
      assertThat(securityConfig.isPublic("/v1/auth/refresh")).isTrue();
    }

    /**
     * Verifies that the logout endpoint is classified as publicly accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the authentication logout route, allowing it to be invoked without
     * prior authentication.
     */
    @Test
    @DisplayName("/v1/auth/logout is public")
    void authLogoutIsPublic() {
      assertThat(securityConfig.isPublic("/v1/auth/logout")).isTrue();
    }

    /**
     * Verifies that the fetcher statistics endpoint is classified as publicly
     * accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the fetcher stats route, allowing access without authentication.
     */
    @Test
    @DisplayName("/v1/fetcher/stats is public")
    void fetcherIsPublic() {
      assertThat(securityConfig.isPublic("/v1/fetcher/stats")).isTrue();
    }

    /**
     * Verifies that the actuator health endpoint is classified as publicly
     * accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the health check endpoint, allowing external monitoring without
     * authentication.
     */
    @Test
    @DisplayName("/actuator/health is public")
    void actuatorHealthIsPublic() {
      assertThat(securityConfig.isPublic("/actuator/health")).isTrue();
    }

    /**
     * Verifies that the actuator Prometheus metrics endpoint is classified as
     * publicly accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the metrics scraping endpoint, allowing Prometheus to collect metrics
     * without authentication.
     */
    @Test
    @DisplayName("/actuator/prometheus is public")
    void actuatorPrometheusIsPublic() {
      assertThat(securityConfig.isPublic("/actuator/prometheus")).isTrue();
    }

    /**
     * Verifies that the Swagger UI endpoint is classified as publicly accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the API documentation UI, allowing developers to access it without
     * authentication.
     */
    @Test
    @DisplayName("/swagger-ui/index.html is public")
    void swaggerUiIsPublic() {
      assertThat(securityConfig.isPublic("/swagger-ui/index.html")).isTrue();
    }

    /**
     * Verifies that the OpenAPI specification endpoint is classified as publicly
     * accessible.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns true
     * for the API documentation endpoint, allowing tools and clients to retrieve
     * the OpenAPI definition without authentication.
     */
    @Test
    @DisplayName("/v3/api-docs is public")
    void apiDocsIsPublic() {
      assertThat(securityConfig.isPublic("/v3/api-docs")).isTrue();
    }
  }

  /**
   * Groups test cases for endpoints that must not be classified as public.
   *
   * <p>Verifies that all non-allowlisted paths are correctly identified as
   * protected by {@link GatewaySecurityConfig#isPublic(String)}, ensuring they
   * require authentication.
   */
  @Nested
  @DisplayName("Protected paths — isPublic() must return false")
  class ProtectedPaths {

    /**
     * Verifies that the student transcript endpoint is classified as protected.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns false
     * for the carriera libretto route, requiring authentication for access.
     */
    @Test
    @DisplayName("/v1/carriera/libretto is protected")
    void carrieraLibrettoIsProtected() {
      assertThat(securityConfig.isPublic("/v1/carriera/libretto")).isFalse();
    }

    /**
     * Verifies that the student fees endpoint is classified as protected.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns false
     * for the carriera tasse route, requiring authentication for access.
     */
    @Test
    @DisplayName("/v1/carriera/tasse is protected")
    void carrieraTasseIsProtected() {
      assertThat(securityConfig.isPublic("/v1/carriera/tasse")).isFalse();
    }

    /**
     * Verifies that the canteen menu endpoint is classified as protected.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns false
     * for the canteen menu route, requiring authentication for access.
     */
    @Test
    @DisplayName("/v1/canteen/menu is protected")
    void canteenMenuIsProtected() {
      assertThat(securityConfig.isPublic("/v1/canteen/menu")).isFalse();
    }

    /**
     * Verifies that the chat messages endpoint is classified as protected.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns false
     * for the chat messages route, requiring authentication for access.
     */
    @Test
    @DisplayName("/v1/chat/messages is protected")
    void chatMessagesIsProtected() {
      assertThat(securityConfig.isPublic("/v1/chat/messages")).isFalse();
    }

    /**
     * Verifies that an empty path is not classified as public.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} returns false
     * for empty input, treating it as a non-public (protected) path by default.
     */
    @Test
    @DisplayName("empty string is protected")
    void emptyStringIsProtected() {
      assertThat(securityConfig.isPublic("")).isFalse();
    }

    /**
     * Verifies that similar-looking paths are not incorrectly classified as public.
     *
     * <p>Ensures that {@link GatewaySecurityConfig#isPublic(String)} performs
     * exact or correctly bounded matching and does not treat
     * "/v1/authorization" as part of the "/v1/auth/" public prefix.
     */
    @Test
    @DisplayName("path similar to public prefix but different is protected")
    void similarPathIsProtected() {
      assertThat(securityConfig.isPublic("/v1/authorization")).isFalse();
    }
  }
}