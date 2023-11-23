package com.api.bankapirest.services.authentication;

import com.api.bankapirest.dtos.request.AuthenticationRequest;
import com.api.bankapirest.dtos.response.AuthenticationResponse;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.User;
import com.api.bankapirest.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtAuthService jwtAuthService;
    private final IUserService userService;

    @Transactional(readOnly = true)
    @Cacheable(value = "login", key = "#request.nif")
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UsernameNotFoundException, ApiException  {
        // check if the user is already authenticated and stored in the database
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNif(),
                        request.getPassword()
                )
        );

        User user = userService.findByNif(request.getNif());

        // create the response
        return createResponse(user);
    }

    @Transactional
    @CacheEvict(value="users", allEntries=true)
    public AuthenticationResponse register(RegisterRequest request) throws ApiException {
        // create the new user and store it in database
        User user = userService.create(request);

        // create the response
        return createResponse(user);
    }

    private AuthenticationResponse createResponse(User user) {
        // create a response with the token for the user
        var jwtToken = jwtAuthService.generateToken(user);
        var refreshToken = jwtAuthService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
