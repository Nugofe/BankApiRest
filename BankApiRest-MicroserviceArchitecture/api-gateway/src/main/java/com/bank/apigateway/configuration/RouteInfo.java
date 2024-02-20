package com.bank.apigateway.configuration;

import lombok.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Getter
@Setter
public class RouteInfo {

    private String path;
    private CheckFunction preauthorize;

    public RouteInfo(String path, CheckFunction preauthorize) {
        this.path = path;
        this.preauthorize = preauthorize;
    }

    public boolean matches(ServerHttpRequest request) {
        return request.getURI().getPath().startsWith(path);
    }
}