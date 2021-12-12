package ru.svgogin.service.spark.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public GroupedOpenApi publicUserApi() {
    return GroupedOpenApi.builder()
        .group("Spark")
        .pathsToMatch("/spark/**")
        .build();
  }

  @Bean
  public OpenAPI customOpenApi(@Value("Companies management API") String appDescription,
                               @Value("v1") String appVersion) {
    return new OpenAPI().info(new Info().title("Spark API")
        .version(appVersion)
        .description(appDescription)
        .contact(new Contact().name("Gogin Sergei")
            .email("goginsergei@mail.com")))
        .servers(List.of(new Server().url("http://localhost:8080")
                .description("Dev service")));
  }
}
