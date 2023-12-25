package com.bank.apigateway.configuration;

import com.bank.apigateway.filters.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AuthFilter authFilter;

    @Value("${app.config.routes.auth-id}")
    private String authID;
    @Value("${app.config.routes.auth-uri}")
    private String authURI;
    @Value("${app.config.routes.auth-path}")
    private String authPATH;

    @Value("${app.config.routes.users-id}")
    private String usersID;
    @Value("${app.config.routes.users-uri}")
    private String usersURI;
    @Value("${app.config.routes.users-path}")
    private String usersPATH;

    @Value("${app.config.routes.accounts-id}")
    private String accountsID;
    @Value("${app.config.routes.users-uri}")
    private String accountsURI;
    @Value("${app.config.routes.users-path}")
    private String accountsPATH;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder/*, AuthFilter authFilter*/) {
        return builder.routes()
                .route(authID, r -> r.path("/api/v1/auth/login")
                        .and().method("POST")
                        .filters(f -> f.filters(authFilter))
                        .uri(authURI))
                .route(authID, r -> r.path("/api/v1/auth/validate")
                        .and().method("POST")
                        .filters(f -> f.filters(authFilter))
                        .uri(authURI))
                .route(authID, r -> r.path("/api/v1/auth/register")
                        .and().method("POST")
                        .filters(f -> f.filters(authFilter))
                        .uri(authURI))
                .route(usersID, r -> r.path(usersPATH)
                        //.and().method("POST")
                        //.filters(f -> f.filters(authFilter.apply(new AuthFilter.Config())))
                        .filters(f -> f.filters(authFilter)).uri(usersURI))
                .route(accountsID, r -> r.path(accountsPATH)
                        //.and().method("POST")
                        //.filters(f -> f.filters(authFilter.apply(new AuthFilter.Config())))
                        .filters(f -> f.filters(authFilter)).uri(accountsURI))
                .build();
    }
}
