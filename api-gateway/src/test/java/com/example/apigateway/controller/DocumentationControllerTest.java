package com.example.apigateway.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import springfox.documentation.swagger.web.SwaggerResource;

public class DocumentationControllerTest {

  private DocumentationController documentationController;

  @Test
  public void getResourcesList_Success() {
    documentationController = new DocumentationController();
    List<SwaggerResource> resourceList = documentationController.get();
    assertEquals("users-api", resourceList.get(0).getName());
  }
}
