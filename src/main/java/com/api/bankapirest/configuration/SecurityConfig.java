package com.api.bankapirest.configuration;

import com.api.bankapirest.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String API_URL_PATTERN = "/api";

    // beans configured in ApplicationConfig.java
    private final AuthenticationProvider authenticatorProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> {
                    // login and register without security
                    req.requestMatchers(API_URL_PATTERN + "/v1/auth/**").permitAll()
                    // external rest client and circuit breaker without security, just to test easily
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers(API_URL_PATTERN + "/v1/users/examples").permitAll()
                    // rest of the api with security
                    .anyRequest().authenticated();
                })
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticatorProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                //.formLogin(Customizer.withDefaults())
                //.httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }
}
