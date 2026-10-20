package com.ecommerce.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean // Object creation
    public OpenAPI ecommerceOpenAPI() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title("E-Commerce Backend API")


                                .description("""
    REST API for an E-Commerce application built using Spring Boot.

    DEMO ADMIN LOGIN
    ----------------
    Admin Email: admin@gmail.com
    Admin Password: Admin@123

    HOW TO TEST THE API
    1. Open the authentication/login endpoint.
    2. Enter the demo Admin email and password.
    3. Click Execute to receive a JWT token.
    4. Click Authorize at the top of Swagger UI.
    5. Enter the JWT token to access protected endpoints.
    """)


                                .version("1.0.0")

                                .contact(

                                        new Contact()

                                                .name("S. Sakthi")

                                                .email("ushasakthi2003@gmail.com")

                                                .url("https://github.com/your-github")

                                )

                                .license(

                                        new License()

                                                .name("MIT License")

                                )

                )

                .externalDocs(

                        new ExternalDocumentation()

                                .description("Project Repository")

                                .url("https://github.com/your-github/ecommerce-backend")

                )

                .components(

                        new Components()

                                .addSecuritySchemes(

                                        "bearerAuth",

                                        new SecurityScheme()

                                                .type(SecurityScheme.Type.HTTP)

                                                .scheme("bearer")

                                                .bearerFormat("JWT")

                                )

                )

                .addSecurityItem(

                        new SecurityRequirement()

                                .addList("bearerAuth")

                );
    }
}