package com.example.apigateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @RequestMapping("/unittest")
  public String unittest() {
    return "helloworld";
  }

  @PreAuthorize("authenticated()")
  @RequestMapping("/auth")
  public String auth() {
    return "helloauth";
  }
}
