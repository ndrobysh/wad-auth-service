package com.wad.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration Swagger/OpenAPI pour la documentation de l'API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")
                        .version("1.0.0")
                        .description("API d'authentification - Génération et validation de tokens")
                        .contact(new Contact()
                                .name("WAD Team")
                                .email("team@wad-gacha.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Serveur local")
                ));
    }
}
