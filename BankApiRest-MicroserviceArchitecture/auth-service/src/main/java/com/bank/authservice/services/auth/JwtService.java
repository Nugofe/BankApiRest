package com.bank.authservice.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.config.secretKey}")
    private String SECRET_KEY;

    @Value("${app.config.jwtExpiration}")
    private long jwtExpiration;

    @Value("${app.config.refreshExpiration}")
    private long refreshExpiration;

    public boolean isTokenValid(String token, String username) {
        String name = extractUsername(token);
        return name.equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        // the expiration date must be previous than the current date in order for the token to be expired
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        if(claims != null) {
            return claimsResolver.apply(claims);
        } else {
            return null;
        }
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                //.signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        try{
            return Jwts.parser()
                    // signature of the JWT token, used to verify the token has not been manipulated
                    //.setSigningKey(getSignInKey())
                    .verifyWith(getSignInKey())
                    .build()
                    //.parseClaimsJws(token)
                    .parseSignedClaims(token)
                    //.getBody();
                    .getPayload();
        } catch (Exception exception){
            return null;
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}