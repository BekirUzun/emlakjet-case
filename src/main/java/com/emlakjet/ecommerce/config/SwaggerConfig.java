package com.emlakjet.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI configure() {
        var info = new Info()
                .title("Emlakjet E-commerce Case")
                .version("1.0")
                .description("Endpoints related to Emlakjet E-commerce Case");

        var securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");

        var securityComponent = new Components()
                .addSecuritySchemes("Bearer Authentication", securityScheme);

        var securityRequirement = new SecurityRequirement().
                addList("Bearer Authentication");

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(securityComponent)
                .info(info);
    }
}
