package com.bank.apigateway.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Component("RouteValidator")
public class RouteValidator {

    public final List<String> openEndpoints;
    {
        openEndpoints = new ArrayList<>();
        openEndpoints.add("/api/v1/auth");
        openEndpoints.add("/api/v1/users/examples");
        openEndpoints.add("/swagger-ui");
        openEndpoints.add("/v3/api-docs");
        openEndpoints.add("/actuator");
    }

    public Predicate<ServerHttpRequest> needsAuthentication =
            request -> openEndpoints.stream()
                    .noneMatch(uri ->
                            request.getURI().getPath().contains(uri)
                    );

    public final List<RouteInfo> endpointAuthorizations = List.of(
            new SpecificRouteInfo(HttpMethod.GET, "/api/v1/users", new CheckFunction(CheckFunction.AdminRole)),
            new SpecificRouteInfo(HttpMethod.POST, "/api/v1/users", new CheckFunction(CheckFunction.AdminRole))
    );

    public Predicate<ServerHttpRequest> needsAuthorization =
            request -> endpointAuthorizations.stream()
                    .anyMatch(routeInfo -> {
                        if(!request.getURI().getPath().contains(routeInfo.getPath())) {
                            return false;
                        }

                        if(routeInfo instanceof SpecificRouteInfo) {
                            if (!request.getMethod().equals(((SpecificRouteInfo) routeInfo).getMethod())) {
                                return false;
                            }
                        }

                        return true;
                    });

    public boolean isAuthorizationCorrect(ServerHttpRequest request) {
        AtomicReference<RouteInfo> matchedRouteInfo = new AtomicReference<>();

        boolean needsAuth = endpointAuthorizations.stream()
                .anyMatch(routeInfo -> {
                    boolean matches = false;
                    if(routeInfo instanceof SpecificRouteInfo) {
                        if (request.getURI().getPath().contains(routeInfo.getPath()) &&
                            request.getMethod().equals(((SpecificRouteInfo) routeInfo).getMethod())) {
                            matches = true;
                        }
                    } else {
                        if (request.getURI().getPath().contains(routeInfo.getPath())) {
                            matches = true;
                        }
                    }

                    if (matches) {
                        matchedRouteInfo.set(routeInfo);
                    }
                    return matches;
                });

        if (needsAuth) {
            return matchedRouteInfo.get().getPreauthorize().perform(request.getHeaders());
        }
        return false;
    }

}