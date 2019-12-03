package com.example.commentsapi.config;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.FileReader;
import java.io.IOException;

@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() throws IOException, XmlPullParserException {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = reader.read(new FileReader("pom.xml"));
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.example.commentsapi.controller"))
        .paths(PathSelectors.any()).build().apiInfo(
            new ApiInfo("Comments-Api Documentation", "Documentation automatically generated",
                model.getParent().getVersion(), null, new Contact("kyle", "", "qc91129@gmail.com"),
                null, null));
  }
}
