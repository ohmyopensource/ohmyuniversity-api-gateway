package org.ohmyopensource.ohmyuniversity.gateway.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Properties;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * Loads environment variables from a local .env file into Spring's {@link ConfigurableEnvironment}.
 *
 * <p>This processor runs before the Spring application context is fully initialized in order to
 * ensure that placeholders (e.g. ${VAR}) inside application.yml or application.properties can be
 * correctly resolved.
 *
 * <p>Only a predefined set of environment variables is loaded from the .env file, explicitly
 * ignoring system environment variables to avoid unexpected overrides.
 *
 */
public class DotenvLoader implements EnvironmentPostProcessor, Ordered {

  private static final String DOTENV_PROPERTY_SOURCE_NAME = "dotenvProperties";

  /**
   * List of environment variable keys that will be loaded from the .env file.
   */
  private static final String[] ENV_KEYS = {
      "SPRING_PROFILES_ACTIVE",
      "JWT_SECRET",
      "REDIS_HOST",
      "REDIS_PORT",
      "REDIS_PASSWORD",
      "CORE_SERVICE_URL",
      "CHAT_SERVICE_URL",
      "BILLING_SERVICE_URL",
      "FETCHER_SERVICE_URL"
  };

  // ============ Override Methods ============

  /**
   * Defines the loading order of this post-processor within the Spring environment
   * initialization chain.
   *
   * @return an integer representing the execution priority, where lower values have higher
   *     priority
   */
  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 10;
  }

  /**
   * Loads environment variables from the .env file and injects them into
   * the Spring {@link ConfigurableEnvironment}.
   *
   * <p>The method:
   * - Loads the .env file from the current working directory
   * - Extracts only the variables defined in {@link #ENV_KEYS}
   * - Registers them as a high-priority property source in Spring
   *
   * @param environment the Spring environment into which properties will be injected
   * @param application the running Spring application instance
   */
  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment,
      SpringApplication application) {

    try {
      Dotenv dotenv = Dotenv.configure()
          .directory(System.getProperty("user.dir"))
          .ignoreIfMissing()
          .ignoreIfMalformed()
          .load();

      Properties props = new Properties();

      for (String key : ENV_KEYS) {
        try {
          String value = dotenv.get(key);
          props.put(key, value);
        } catch (Exception ignored) {
        }
      }

      if (!props.isEmpty()) {
        environment.getPropertySources()
            .addFirst(new PropertiesPropertySource(DOTENV_PROPERTY_SOURCE_NAME, props));
      }

    } catch (Exception e) {
    }
  }
}