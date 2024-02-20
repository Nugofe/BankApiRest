package com.bank.apigateway.configuration;

import com.bank.apigateway.filters.AuthenticationFilter;
import com.bank.apigateway.filters.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${app.config.routes.auth-id}")
    public String authID;
    @Value("${app.config.routes.auth-uri}")
    public String authURI;
    @Value("${app.config.routes.auth-path}")
    public String authPATH;

    @Value("${app.config.routes.users-id}")
    public String usersID;
    @Value("${app.config.routes.users-uri}")
    public String usersURI;
    @Value("${app.config.routes.users-path}")
    public String usersPATH;

    @Value("${app.config.routes.accounts-id}")
    public String accountsID;
    @Value("${app.config.routes.users-uri}")
    public String accountsURI;
    @Value("${app.config.routes.users-path}")
    public String accountsPATH;

    private final AuthenticationFilter authenticationFilter;
    private final AuthorizationFilter authorizationFilter;

    @Bean("RouteLocator")
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(authID, r -> r.path(authPATH)
                        .filters(f -> f.filters(authenticationFilter, authorizationFilter))
                        .uri(authURI))
                .route(usersID, r -> r.path(usersPATH)
                        .filters(f -> f.filters(authenticationFilter, authorizationFilter))
                        .uri(usersURI))
                .route(accountsID, r -> r.path(accountsPATH)
                        .filters(f -> f.filters(authenticationFilter, authorizationFilter))
                        .uri(accountsURI))
                .build();
    }
}