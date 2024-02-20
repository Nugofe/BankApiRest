package com.bank.apigateway.filters;

import com.bank.apigateway.configuration.RouteValidator;
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
public class AuthorizationFilter implements GatewayFilter {

    @Value("${app.authorization.enabled}")
    private boolean authEnabled;

    private final RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!authEnabled) {
            System.out.println("Authorization is disabled. To enable it, make " +
                    "\"app.authorization.enabled\" property as true");
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        if(routeValidator.needsAuthentication.test(request) && routeValidator.needsAuthorization.test(request)) {
            if(!routeValidator.isAuthorizationCorrect(request)) {
                return this.onError(exchange, "Invalid Authorization", HttpStatus.FORBIDDEN);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        System.out.println(error);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}