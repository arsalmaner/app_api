package com.app_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "IT",
                        email = "it@enretag.com",
                        url = "https://enretag.com"),
                description = "OpenApi documentation for Enretag API",
                title = "OpenApi specification - Enretag",
                version = "1.0",
                license = @License(
                        name = "Licence",
                        url = "https://enretag.com"),
                termsOfService = "Terms of service"),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"),
                @Server(
                        description = "PROD ENV",
                        url = "https://app.enretag.com")
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth")
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
}
