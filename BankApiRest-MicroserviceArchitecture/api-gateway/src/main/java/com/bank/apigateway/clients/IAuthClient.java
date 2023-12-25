package com.bank.apigateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service", url = "${application.config.auth-url}")
public interface IAuthClient {

    @PostMapping("/validate")
    ResponseEntity<Boolean> validateRequest(ServerHttpRequest request);
}
