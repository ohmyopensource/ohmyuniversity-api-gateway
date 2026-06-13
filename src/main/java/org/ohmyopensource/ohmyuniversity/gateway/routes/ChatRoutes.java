package org.ohmyopensource.ohmyuniversity.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.stereotype.Component;

/**
 * Route configuration for chat-related endpoints.
 *
 * <p>These routes are protected and require a valid OhMyUniversity JWT.
 * Authentication is enforced
 * by {@link org.ohmyopensource.ohmyuniversity.gateway.config.GatewayJwtFilter}.
 *
 * <p>Exposed chat endpoints:
 * - GET /v1/chat/channels
 * - GET /v1/chat/messages
 * - POST /v1/chat/messages
 * - GET /v1/chat/ws (WebSocket upgrade)
 */
@Component
public class ChatRoutes {

  private final String chatServiceUrl;

  // ============ Constructor ============

  /**
   * Creates a new {@code ChatRoutes} instance.
   *
   * @param chatServiceUrl base URL of the chat service used to forward chat-related requests
   */
  public ChatRoutes(
      @Value("${CHAT_SERVICE_URL:http://localhost:8081}") String chatServiceUrl) {
    this.chatServiceUrl = chatServiceUrl;
  }

  // ============ Class Methods ============

  /**
   * Registers chat routes into the provided Gateway route builder.
   *
   * <p>All requests matching {@code /v1/chat/**} are rewritten to
   * {@code /api/v1/chat/**} and forwarded to the chat service.
   *
   * <p>WebSocket connections use the same routing rule. Spring Cloud Gateway automatically
   * handles the upgrade from HTTP to WebSocket transparently.
   *
   * @param builder the Spring Cloud Gateway route builder
   * @return the updated builder containing chat routes
   */
  public Builder register(Builder builder) {
    return builder
        .route("chat", r -> r
            .path("/v1/chat/**")
            .filters(f -> f.rewritePath(
                "/v1/(?<segment>.*)", "/api/v1/${segment}"))
            .uri(chatServiceUrl));
  }
}