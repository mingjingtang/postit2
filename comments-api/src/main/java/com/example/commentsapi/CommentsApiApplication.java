package com.example.commentsapi;

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
public class CommentsApiApplication {

	@RequestMapping("/test")
	public String home() {
		return "some comments";
	}

	public static void main(String[] args){
		SpringApplication.run(CommentsApiApplication.class, args);
	}

}
