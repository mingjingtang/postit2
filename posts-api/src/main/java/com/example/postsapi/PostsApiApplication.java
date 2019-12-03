package com.example.postsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@RestController
public class PostsApiApplication {

  @RequestMapping("/test")
  public String home() {
    return "some posts";
  }

  public static void main(String[] args) {
    SpringApplication.run(PostsApiApplication.class, args);
  }
}
