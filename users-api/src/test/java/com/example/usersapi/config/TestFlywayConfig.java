package com.example.usersapi.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestFlywayConfig {
  @Bean
  public FlywayMigrationStrategy clean() {
    return flyway -> {
      flyway.clean();
      flyway.migrate();
    };
  }
}
