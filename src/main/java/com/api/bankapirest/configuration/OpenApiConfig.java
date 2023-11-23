package com.api.bankapirest.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${bankapirest.openapi.dev-url}")
    private String devUrl;

    @Value("${bankapirest.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        /*Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");*/

        Contact contact = new Contact();
        contact.setEmail("nugofe14@gmail.com");
        contact.setName("Nuria");
        contact.setUrl("https://nugofe.github.io/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Bank Rest Api")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage users, accounts and transactions.")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer/*, prodServer*/));
    }
}