package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Gateway route configuration for the email domain.
 *
 * <p>All requests matching {@code /v1/email/**} are forwarded to the
 * core service and rewritten to the internal path structure
 * {@code /api/v1/email/**}.
 *
 * <p>Authentication and authorization are enforced by the gateway
 * security chain. The OAuth2 callback endpoint remains publicly
 * accessible because it is invoked directly by Microsoft's
 * authorization servers after user consent.
 *
 * <p>Exposed routes:
 * - GET    /v1/email/auth/url - Generates the OAuth2 authorization URL.
 * - GET    /v1/email/auth/callback - Handles the OAuth2 authorization callback.
 * - DELETE /v1/email/auth/disconnect - Disconnects the linked email account.
 * - GET    /v1/email/inbox - Retrieves inbox messages.
 * - GET    /v1/email/{messageId} - Retrieves a specific email message.
 * - POST   /v1/email/send - Sends a new email message.
 *
 * <p>This class centralizes all gateway routing rules related to the
 * institutional email integration.
 */
@Component
public class EmailRoutes {

  private final String coreServiceUrl;

  // ============ Constructor ============

  /**
   * Creates the route component and injects the core service URL.
   *
   * @param coreServiceUrl base URL of the core microservice
   */
  public EmailRoutes(
      @Value("${CORE_SERVICE_URL:http://localhost:8083}") String coreServiceUrl) {
    this.coreServiceUrl = coreServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers the email route definition.
   *
   * <p>Incoming requests matching {@code /v1/email/**} are forwarded
   * to the core service after rewriting the path prefix from
   * {@code /v1} to {@code /api/v1}.
   *
   * @param builder route builder used to register gateway routes
   * @return the same builder instance with the email route attached
   */
  public Builder register(Builder builder) {
    return builder
        .route("email", r -> r
            .path("/v1/email/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(coreServiceUrl));
  }
}