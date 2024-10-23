package com.manko.book.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import java.util.Collections;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Fedor",
            email = "contact@gmail.com",
            url = "https://foka.com/course"
        ),
        description = "OpenApi documentation for Spring Security",
        title = "OpenApi specification - Manko",
        version = "1.0",
        license = @License(
            name = "Licence name",
            url = "https://some-url.com"
        ),
        termsOfService = "Terms of service"
    ),
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8088/api/v1"
        ),
        @Server(
            description = "PROD ENV",
            url = "https://manko.com/course"
        )
    },
    security = {
        @SecurityRequirement(
            name = "bearerAuth"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

  @Bean
  public OpenApiCustomizer customOpenApi() {
    return openApi -> openApi.getPaths().forEach((path, value) -> {
      if (path.startsWith("/auth/register") || path.startsWith("/auth/authenticate")) {
        value.readOperations().forEach(operation -> operation.setSecurity(Collections.emptyList()));
      }
    });
  }

}
