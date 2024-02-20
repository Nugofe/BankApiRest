package com.bank.apigateway.configuration;

import lombok.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;

@Getter
@Setter
public class SpecificRouteInfo extends RouteInfo {

    private HttpMethod method;

    public SpecificRouteInfo(HttpMethod method, String path, CheckFunction preauthorize) {
        super(path, preauthorize);
        this.method = method;
    }

    @Override
    public boolean matches(ServerHttpRequest request) {
        return request.getURI().getPath().startsWith(super.getPath()) &&
                Objects.equals(request.getMethod(), method);
    }

}