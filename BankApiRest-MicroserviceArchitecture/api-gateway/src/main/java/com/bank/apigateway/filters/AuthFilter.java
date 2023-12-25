package com.bank.apigateway.filters;

import com.bank.apigateway.clients.IAuthClient;
import com.bank.apigateway.configuration.JwtUtil;
import com.bank.apigateway.configuration.RouteValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilter {

    @Value("${app.authentication.enabled}")
    private boolean authEnabled;

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!authEnabled) {
            System.out.println("Authentication is disabled. To enable it, make \"app.authentication.enabled\" property as true");
            return chain.filter(exchange);
        }
        //String token ="";
        ServerHttpRequest request = exchange.getRequest();

        if(routeValidator.isSecured.test(request)) {
            System.out.println("validating authentication token");
            /*if(this.isCredsMissing(request)) {
                System.out.println("in error");
                return this.onError(exchange,"Credentials missing", HttpStatus.UNAUTHORIZED);
            }*/

            /*if (request.getHeaders().containsKey("nif") && request.getHeaders().containsKey("roles")) {
                token = authUtil.getToken(
                        request.getHeaders().get("userName").toString(),
                        request.getHeaders().get("role").toString()
                );
            } else {
                token = request.getHeaders().get("Authorization").toString().split(" ")[1];
            }

            if(jwtUtil.isInvalid(token)) {
                return this.onError(exchange,"Invalid token",HttpStatus.UNAUTHORIZED);
            }
            else {
                System.out.println("Authentication successful");
            }

            this.populateRequestWithHeaders(exchange, token);*/

            if(!jwtUtil.validateRequest(request)) {
                return this.onError(exchange,"Invalid Authorization", HttpStatus.UNAUTHORIZED);
            } else {
                System.out.println("Successful Authentication");
            }

            /*HttpEntity<ServerHttpRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(
                    "http://localhost:8090/api/v1/auth/validate",
                    requestEntity,
                    Boolean.class
            );

            if(responseEntity.getBody() == null || !responseEntity.getBody()){
                return this.onError(exchange,"Invalid Authorization", HttpStatus.UNAUTHORIZED);
            } else {
                System.out.println("Successful Authentication");
            }*/
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isCredsMissing(ServerHttpRequest request) {
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        return  authHeader == null ||
                !authHeader.startsWith("Bearer ") ||
                !request.getHeaders().containsKey("nif") ||
                !request.getHeaders().containsKey("roles");

        /*return !(request.getHeaders().containsKey("nif") && request.getHeaders().containsKey("roles")) &&
                !request.getHeaders().containsKey("Authorization");*/
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest()
                .mutate()
                .header("nif", String.valueOf(claims.get("nif")))
                .header("roles", String.valueOf(claims.get("roles")))
                .build();
    }
}
