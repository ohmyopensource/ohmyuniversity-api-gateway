package org.ohmyopensource.ohmyuniversity.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global Spring Cloud Gateway filter responsible for validating JWT tokens for all
 * incoming requests.
 *
 * <p>This filter ensures that every non-public request is authenticated using a valid JWT
 * issued by the system.
 * Public routes are defined in {@link GatewaySecurityConfig} and are excluded from authentication.
 *
 * <p>Processing flow:
 * - Extract the Bearer token from the Authorization header
 * - Validate the JWT signature and expiration using the configured secret key
 * - Extract the user identifier from the token claims
 * - Inject the user identifier into the X-User-Id header for downstream services
 *
 * <p>Downstream microservices rely on the X-User-Id header as a trusted identity source and
 * do not re-validate the JWT.
 * The Core service performs its own validation since it is both issuer and consumer of tokens.
 */
@Component
public class GatewayJwtFilter implements GlobalFilter, Ordered {

  private static final Logger log = LoggerFactory.getLogger(GatewayJwtFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String X_USER_ID = "X-User-Id";

  private final SecretKey signingKey;
  private final GatewaySecurityConfig securityConfig;

  // ============ Constructor ============

  /**
   * Creates a new {@code GatewayJwtFilter} instance.
   *
   * <p>The JWT signing key is derived from the configured secret and used to validate all
   * incoming tokens.
   *
   * @param secret the JWT signing secret used to validate token signatures
   * @param securityConfig configuration component defining public and protected routes
   */
  public GatewayJwtFilter(
      @Value("${JWT_SECRET:omu_dev_jwt_secret_please_change_in_prod_ok}") String secret,
      GatewaySecurityConfig securityConfig) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.securityConfig = securityConfig;
    log.info("GatewayJwtFilter: initialized with secret length={}", secret.length());
  }

  // ============ Override Methods ============

  /**
   * Defines the execution priority of this global filter within the Gateway filter chain.
   *
   * @return an integer representing filter precedence (lower values are executed first)
   */
  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  /**
   * Applies JWT authentication to incoming requests.
   *
   * <p>If the request path is public, the filter is bypassed. Otherwise:
   *
   * <p>Steps performed:
   * - Validate presence and format of Authorization header
   * - Parse and validate JWT token
   * - Extract user identifier from token claims
   * - Propagate identity via X-User-Id header
   *
   * @param exchange the current server web exchange containing request and response
   * @param chain the Gateway filter chain used to forward the request
   * @return a {@link Mono} completing when request processing is done
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();

    if (securityConfig.isPublic(path)) {
      log.debug("GatewayJwtFilter: public path {} — skipping auth", path);
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
      log.warn("GatewayJwtFilter: missing or invalid Authorization header for path {}", path);
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    String token = authHeader.substring(BEARER_PREFIX.length());

    try {
      Claims claims = Jwts.parser()
          .verifyWith(signingKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      String omuUserId = claims.getSubject();

      ServerWebExchange mutated = exchange.mutate()
          .request(r -> r.header(X_USER_ID, omuUserId))
          .build();

      log.debug("GatewayJwtFilter: authenticated user={} path={}", omuUserId, path);
      return chain.filter(mutated);

    } catch (JwtException e) {
      log.warn("GatewayJwtFilter: invalid JWT for path {} — {}", path, e.getMessage());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }
}