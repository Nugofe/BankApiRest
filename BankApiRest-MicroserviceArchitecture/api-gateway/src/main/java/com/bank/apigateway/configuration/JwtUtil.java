package com.bank.apigateway.configuration;

import com.bank.apigateway.clients.IAuthClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${app.config.secretKey}")
    private static String SECRET_KEY;

    /*@Value("${application.config.jwtExpiration}")
    private static long jwtExpiration;

    @Value("${application.config.refreshExpiration}")
    private static long refreshExpiration;*/

    private final IAuthClient authClient;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                // signature of the JWT token, used to verify the token has not been manipulated
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateRequest(ServerHttpRequest request) {
        return Boolean.TRUE.equals(authClient.validateRequest(request).getBody());
    }


}
