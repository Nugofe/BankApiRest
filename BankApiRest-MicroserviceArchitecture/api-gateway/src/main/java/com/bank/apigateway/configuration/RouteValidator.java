package com.bank.apigateway.configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component("RouteValidator")
public class RouteValidator {

    public static final List<String> openEndpoints = List.of(
            "/api/v1/auth",
            "/api/v1/users/examples",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
