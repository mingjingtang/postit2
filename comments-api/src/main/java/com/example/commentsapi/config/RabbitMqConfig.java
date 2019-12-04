package com.example.commentsapi.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RabbitMqConfig {

    @Bean
    @Profile("pcf")
    public ConnectionFactory rabbitConnectionFactory() {
        String hostname = "zebra.rmq.cloudamqp.com";
        int port = 5672;
        String username = "agxlgedy";
        String password = "Ca3yRu7NUgZvyaBFqPNMJ8M7im0KacWA";
        String vhost = username;
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
    }
}