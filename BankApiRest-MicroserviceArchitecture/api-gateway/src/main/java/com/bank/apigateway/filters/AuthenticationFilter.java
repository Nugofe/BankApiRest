package com.bank.apigateway.filters;

import com.bank.apigateway.clients.AuthClient;
import com.bank.apigateway.configuration.RouteValidator;
import com.bank.library.dtos.requests.ValidationRequest;
import com.bank.library.dtos.responses.ValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
@RequiredArgsConstructor
public class
AuthenticationFilter implements GatewayFilter {

    @Value("${app.authentication.enabled}")
    private boolean authEnabled;

    private final RouteValidator routeValidator;

    private final AuthClient authClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!authEnabled) {
            System.out.println("Authentication is disabled. To enable it, make " +
                    "\"app.authentication.enabled\" property as true");
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        if(routeValidator.needsAuthentication.test(request)) {
            if (!isAuthHeaderValid(request)) {
                return this.onError(exchange,"Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            // check if the token is valid and extract information from it
            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeader.substring(7);
            ValidationRequest validRequest = ValidationRequest.builder().accessToken(token).build();
            ValidationResponse validationResponse = authClient.validate(validRequest);
            if(validationResponse == null) {
                return this.onError(exchange, "Invalid Authorization", HttpStatus.UNAUTHORIZED);
            }

            // add username (nif in this case) and roles to the headers
            exchange.getRequest()
                    .mutate()
                    .header("nif", validationResponse.getNif())
                    .header("roles", String.valueOf(validationResponse.getRoles()))
                    .build();
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        System.out.println(error);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthHeaderValid(ServerHttpRequest request) {
        try{
            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            return authHeader != null && authHeader.startsWith("Bearer ");
        } catch (Exception e) {
            return false;
        }
    }
}