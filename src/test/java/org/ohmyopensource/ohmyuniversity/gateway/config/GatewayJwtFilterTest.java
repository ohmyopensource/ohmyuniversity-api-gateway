package org.ohmyopensource.ohmyuniversity.gateway.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit tests for {@link GatewayJwtFilter}.
 *
 * <p>Verifies the behavior of the gateway authentication filter for both public
 * and protected routes.
 *
 * <p>Test coverage includes:
 * - bypass of JWT validation for publicly accessible endpoints;
 * - successful validation of correctly signed and non-expired JWTs;
 * - extraction of the authenticated user identifier from JWT claims;
 * - propagation of the authenticated user identifier through the
 * {@code X-User-Id} request header;
 * - rejection of requests containing missing, malformed, expired, or
 * invalid JWTs;
 * - verification of the configured filter execution order.
 *
 * <p>The filter is instantiated directly using a test signing secret and a
 * dedicated {@link GatewaySecurityConfig} instance, without loading a Spring
 * application context.
 */
class GatewayJwtFilterTest {

  private static final String SECRET =
      "omu_test_jwt_secret_at_least_32_chars_ok";
  private static final String OMU_USER_ID = "a2da4d61-b161-405f-b04e-a4122fe791aa";

  private GatewayJwtFilter filter;
  private GatewaySecurityConfig securityConfig;
  private SecretKey signingKey;

  /**
   * Initializes the test fixture before each test execution.
   *
   * <p>Creates a new {@link GatewaySecurityConfig}, instantiates the
   * {@link GatewayJwtFilter} under test, and prepares the signing key used to
   * generate deterministic JWTs for authentication scenarios.
   */
  @BeforeEach
  void setUp() {
    securityConfig = new GatewaySecurityConfig();
    filter = new GatewayJwtFilter(SECRET, securityConfig);
    signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Generates a valid JWT for authenticated request scenarios.
   *
   * <p>The token contains the test user identifier as subject, includes a
   * sample university claim, is signed with the configured test signing key,
   * and remains valid for the duration of the test execution.
   *
   * @return a valid JWT accepted by the filter under test
   */
  private String validToken() {
    return Jwts.builder()
        .subject(OMU_USER_ID)
        .claim("uni", "UNIMOL")
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusSeconds(900)))
        .signWith(signingKey)
        .compact();
  }

  /**
   * Generates an expired authentication token used to verify JWT expiration
   * handling.
   *
   * <p>The generated token is correctly signed but contains an expiration time
   * in the past, allowing validation of the filter behavior when processing
   * expired credentials.
   *
   * @return an expired JWT representing the test user
   */
  private String expiredToken() {
    return Jwts.builder()
        .subject(OMU_USER_ID)
        .issuedAt(Date.from(Instant.now().minusSeconds(3600)))
        .expiration(Date.from(Instant.now().minusSeconds(1)))
        .signWith(signingKey)
        .compact();
  }

  /**
   * Groups test cases covering endpoints that are excluded from JWT
   * authentication requirements.
   *
   * <p>Verifies that requests targeting publicly accessible routes bypass token
   * validation and continue through the gateway filter chain without requiring
   * authentication credentials.
   */
  @Nested
  @DisplayName("Public paths — filter must pass through without JWT validation")
  class PublicPaths {

    /**
     * Verifies that requests targeting the authentication login endpoint bypass
     * JWT validation.
     *
     * <p>Ensures that the request is forwarded through the gateway filter chain
     * without requiring an Authorization header and without modifying the
     * response status.
     */
    @Test
    @DisplayName("/v1/auth/login bypasses JWT validation")
    void authLoginBypasses() {
      MockServerHttpRequest request = MockServerHttpRequest
          .post("/v1/auth/login")
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);
      when(chain.filter(any())).thenReturn(Mono.empty());

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      verify(chain).filter(exchange);
      assertThat(exchange.getResponse().getStatusCode()).isNull();
    }

    /**
     * Verifies that actuator health endpoints remain publicly accessible and
     * bypass gateway authentication checks.
     *
     * <p>Ensures that requests targeting the health endpoint are forwarded to
     * downstream handlers without JWT validation.
     */
    @Test
    @DisplayName("/actuator/health bypasses JWT validation")
    void actuatorHealthBypasses() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/actuator/health")
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);
      when(chain.filter(any())).thenReturn(Mono.empty());

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      verify(chain).filter(exchange);
    }
  }

  /**
   * Groups test cases covering authenticated requests containing a valid JWT.
   *
   * <p>Verifies successful token validation and propagation of the authenticated
   * user identifier to downstream services through gateway-managed request
   * headers.
   */
  @Nested
  @DisplayName("Protected paths — valid JWT must pass and inject X-User-Id")
  class ValidJwt {

    /**
     * Verifies that requests containing a valid JWT are successfully processed.
     *
     * <p>Ensures that the authenticated user identifier extracted from the JWT
     * subject is propagated through the {@code X-User-Id} request header and
     * made available to downstream components.
     */
    @Test
    @DisplayName("valid JWT — request passes through with X-User-Id injected")
    void validJwtPassesThrough() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken())
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      AtomicReference<String> injectedUserId = new AtomicReference<>();

      GatewayFilterChain chain = mutatedExchange -> {
        injectedUserId.set(
            mutatedExchange.getRequest().getHeaders().getFirst("X-User-Id"));
        return Mono.empty();
      };

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(injectedUserId.get()).isEqualTo(OMU_USER_ID);
      assertThat(exchange.getResponse().getStatusCode()).isNull();
    }
  }

  /**
   * Groups test cases covering authentication failures on protected endpoints.
   *
   * <p>Verifies that requests without a valid JWT are rejected with an HTTP
   * 401 Unauthorized response and are not forwarded to downstream handlers.
   */
  @Nested
  @DisplayName("Protected paths — invalid or missing JWT must return 401")
  class InvalidJwt {

    /**
     * Verifies that requests without an Authorization header are rejected.
     *
     * <p>Ensures that the filter responds with HTTP 401 Unauthorized and that
     * the request is not propagated to the downstream filter chain.
     */
    @Test
    @DisplayName("missing Authorization header returns 401")
    void missingAuthHeaderReturns401() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(exchange.getResponse().getStatusCode())
          .isEqualTo(HttpStatus.UNAUTHORIZED);
      verify(chain, never()).filter(any());
    }

    /**
     * Verifies that requests with an Authorization header not using the Bearer
     * scheme are rejected.
     *
     * <p>Ensures that authentication is enforced strictly on Bearer token
     * format, resulting in HTTP 401 Unauthorized and no downstream propagation.
     */
    @Test
    @DisplayName("Authorization header without Bearer prefix returns 401")
    void noBearerPrefixReturns401() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNz")
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(exchange.getResponse().getStatusCode())
          .isEqualTo(HttpStatus.UNAUTHORIZED);
      verify(chain, never()).filter(any());
    }

    /**
     * Verifies that requests containing an expired JWT are rejected.
     *
     * <p>Ensures that token expiration is enforced, resulting in HTTP 401
     * Unauthorized and preventing propagation to downstream services.
     */
    @Test
    @DisplayName("expired JWT returns 401")
    void expiredJwtReturns401() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken())
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(exchange.getResponse().getStatusCode())
          .isEqualTo(HttpStatus.UNAUTHORIZED);
      verify(chain, never()).filter(any());
    }

    /**
     * Verifies that JWTs signed with an incorrect secret key are rejected.
     *
     * <p>Ensures signature validation is enforced, resulting in HTTP 401
     * Unauthorized and preventing propagation to downstream services.
     */
    @Test
    @DisplayName("JWT signed with wrong secret returns 401")
    void wrongSecretReturns401() {
      SecretKey wrongKey = Keys.hmacShaKeyFor(
          "completely_different_secret_32_chars_xx".getBytes(StandardCharsets.UTF_8));
      String token = Jwts.builder()
          .subject(OMU_USER_ID)
          .expiration(Date.from(Instant.now().plusSeconds(900)))
          .signWith(wrongKey)
          .compact();

      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(exchange.getResponse().getStatusCode())
          .isEqualTo(HttpStatus.UNAUTHORIZED);
      verify(chain, never()).filter(any());
    }

    /**
     * Verifies that malformed JWT tokens are rejected.
     *
     * <p>Ensures that tokens not conforming to the JWT structure are not
     * processed, resulting in HTTP 401 Unauthorized and no propagation to
     * downstream services.
     */
    @Test
    @DisplayName("malformed JWT returns 401")
    void malformedJwtReturns401() {
      MockServerHttpRequest request = MockServerHttpRequest
          .get("/v1/carriera/libretto")
          .header(HttpHeaders.AUTHORIZATION, "Bearer not.a.jwt")
          .build();
      MockServerWebExchange exchange = MockServerWebExchange.from(request);

      GatewayFilterChain chain = mock(GatewayFilterChain.class);

      StepVerifier.create(filter.filter(exchange, chain))
          .verifyComplete();

      assertThat(exchange.getResponse().getStatusCode())
          .isEqualTo(HttpStatus.UNAUTHORIZED);
      verify(chain, never()).filter(any());
    }
  }

  /**
   * Verifies that the gateway filter is executed with highest precedence.
   *
   * <p>Ensures that the filter order is set to {@code Integer.MIN_VALUE}, so it
   * runs before all other gateway filters in the processing chain.
   */
  @Test
  @DisplayName("filter order is HIGHEST_PRECEDENCE")
  void filterOrderIsHighestPrecedence() {
    assertThat(filter.getOrder()).isEqualTo(Integer.MIN_VALUE);
  }
}