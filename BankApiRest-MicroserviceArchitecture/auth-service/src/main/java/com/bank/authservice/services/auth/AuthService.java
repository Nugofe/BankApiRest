package com.bank.authservice.services.auth;

import com.bank.authservice.clients.IUserClient;
import com.bank.authservice.models.Role;
import com.bank.authservice.models.UserCredentials;
import com.bank.authservice.repositories.IRoleRepository;
import com.bank.authservice.repositories.IUserCredentialsRepository;
import com.bank.library.dtos.requests.AuthenticationRequest;
import com.bank.library.dtos.requests.RegisterRequest;
import com.bank.library.dtos.responses.AuthenticationResponse;
import com.bank.library.exceptions.ApiException;
import com.bank.library.models.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final IUserCredentialsRepository credentialsRepository;
    private final IRoleRepository roleRepository;
    private final IUserClient userService;

    @Transactional(readOnly = true)
    @Cacheable(value = "login", key = "#request.nif")
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UsernameNotFoundException, ApiException {
        // check if the user is already authenticated and stored in the database
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNif(),
                        request.getPassword()
                )
        );

        UserCredentials user = credentialsRepository.findByNif(request.getNif());

        // create the response
        return createResponse(user);
    }

    @Transactional
    @CacheEvict(value="users", allEntries=true)
    public AuthenticationResponse register(RegisterRequest request) throws ApiException {
        UserCredentials userCred = mapRegisterRequestToCredentials(request);

        // store the user in the user-service database
        request.setPassword(null);
        userService.createUser(request);

        // store the user credentials in the auth-service database
        UserCredentials credentials = credentialsRepository.save(userCred);

        // create the response
        return createResponse(credentials);
    }

    public boolean validateRequest(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new RuntimeException("missing authorization header");
        }
        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader == null && authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("missing authorization header content");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        // if the user is not already authenticated
        if(username == null) return false;

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // if the token is valid, then allow to interact with the app
            // (create an authToken with the UserDetails and request information and add it to the security context)
            if(jwtService.isTokenValid(token, userDetails)) {
                request.mutate()
                        .header("nif", username)
                        .header("roles", Arrays.toString(userDetails.getAuthorities().toArray()))
                        .build();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                /*authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );*/
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return true;
            }
            return false;
        }
        return true;
    }

    private AuthenticationResponse createResponse(UserCredentials user) {
        // create a response with the token for the user
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private List<Role> getRoles(List<ERole> rolesTypes) {
        List<Role> roles = new ArrayList<>();
        for (ERole r : rolesTypes) {
            // get the role and if found, add it to the list
            roleRepository.findByRolename(r.getName()).ifPresent(roles::add);
        }
        return roles;
    }

    public UserCredentials mapRegisterRequestToCredentials(RegisterRequest user) {
        return UserCredentials.builder()
                .nif(user.getNif())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(getRoles(user.getRoles()))
                .build();
    }
}
