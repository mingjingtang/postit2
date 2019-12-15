package com.example.postsapi.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
@Profile("pcf")
public class RabbitMqConfig {

  @Value("${rabbitmq.host}")
  private String hostname;

  @Value("${rabbitmq.username}")
  private String username;

  @Value("${rabbitmq.password}")
  private String password;

  @Bean
  public ConnectionFactory rabbitConnectionFactory() {
    int port = 5672;
    String vhost = username;
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setPort(port);
    connectionFactory.setVirtualHost(vhost);
    return connectionFactory;
  }
}