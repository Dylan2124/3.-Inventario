package com.example.inventario.client;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI inventarioOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Inventario")
                        .version("1.0.0")
                        .description("Microservicio encargado de gestionar el stock de piezas y movimientos de Hardware.")
                        .contact(new Contact()
                                .name("Matias Tiznado")
                                .email("m.tiznado@duocuc.cl")))
                .servers(List.of(
                        // Entrada por el Gateway (Puerto 8080)
                        new Server().url("http://localhost:8080").description("API Gateway"),
                        // Entrada directa al microservicio (Inventario es 8083)
                        new Server().url("http://localhost:8083").description("Microservicio Inventario - Local")
                ));
    }
}