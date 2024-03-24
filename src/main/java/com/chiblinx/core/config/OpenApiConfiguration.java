package com.chiblinx.core.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringDocDataRestConfiguration.class)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfiguration {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(new Info()
            .title("Property Market Place API")
            .description("""
                Backend API for Property Market Place
                """)
            .version("1.0")
            .contact(new Contact().name("Lekan Adetunmbi").url("https://github.com/greazleay")
                .email("lothbroch@gmail.com"))
            .license(new License().name("GNU").url("https://opensource.org/license/mit/"))
        ).servers(List.of(
            new Server().description("Public Server").url("https://propertymp.onrender.com/api"),
            new Server().description("Local Server").url("http://localhost:4000/api"))
        );
  }
}
