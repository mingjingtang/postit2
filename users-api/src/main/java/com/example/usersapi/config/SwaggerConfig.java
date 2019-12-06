package com.example.usersapi.config;

import java.util.Arrays;
import javax.servlet.ServletContext;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.FileReader;
import java.io.IOException;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

@Configuration
public class SwaggerConfig {

  @Value("${api-gateway.host}")
  private String apiGatewayHost;

  @Bean
  @Profile("dev")
  public Docket api(ServletContext servletContext) throws IOException, XmlPullParserException {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = reader.read(new FileReader("pom.xml"));
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.example.usersapi.controller"))
        .paths(PathSelectors.any()).build().apiInfo(
            new ApiInfo("Users-Api Documentation", "Documentation automatically generated",
                model.getParent().getVersion(), null,
                new Contact("mingjing", "", "tmjcsh@gmail.com"), null, null))
        .host(apiGatewayHost+"/user")
        .securitySchemes(Arrays.asList(apiKey()));
  }

  @Bean
  public SecurityConfiguration securityInfo() {
    return new SecurityConfiguration(null, null, null, null, "", ApiKeyVehicle.HEADER,
        "Authorization", "");
  }

  private ApiKey apiKey() {
    return new ApiKey("Authorization", "Authorization", "header");
  }
}