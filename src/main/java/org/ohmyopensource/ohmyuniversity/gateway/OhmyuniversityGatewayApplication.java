package org.ohmyopensource.ohmyuniversity.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the OhMyUniversity API Gateway application.
 *
 * <p>This class bootstraps the Spring Boot application context and starts the reactive
 * API Gateway responsible for routing, security filtering, and request orchestration.
 */
@SpringBootApplication
public class OhmyuniversityGatewayApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
  public static void main(String[] args) {
    SpringApplication.run(OhmyuniversityGatewayApplication.class, args);
  }

}
