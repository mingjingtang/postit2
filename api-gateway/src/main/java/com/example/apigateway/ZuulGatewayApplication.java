package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableSwagger2
@EnableFeignClients("com.example.apigateway.repository")
public class ZuulGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZuulGatewayApplication.class, args);
  }
}
