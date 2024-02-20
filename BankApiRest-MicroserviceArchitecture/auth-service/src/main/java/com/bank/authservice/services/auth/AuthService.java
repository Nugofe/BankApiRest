package com.bank.authservice.services.auth;

import com.bank.authservice.clients.IUserClient;
import com.bank.authservice.models.Role;
import com.bank.authservice.models.UserCredentials;
import com.bank.authservice.repositories.IRoleRepository;
import com.bank.authservice.repositories.IUserCredentialsRepository;
import com.bank.library.dtos.requests.LoginRequest;
import com.bank.library.dtos.requests.RegisterRequest;
import com.bank.library.dtos.responses.AuthenticationResponse;
import com.bank.library.dtos.responses.ValidationResponse;
import com.bank.library.exceptions.ApiException;
import com.bank.library.models.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final IUserCredentialsRepository credentialsRepository;
    private final IRoleRepository roleRepository;
    private final IUserClient userService;

    @Transactional(readOnly = true)
    @Cacheable(value = "login", key = "#request.nif")
    public AuthenticationResponse login(LoginRequest request) throws UsernameNotFoundException, ApiException {
        // check if the user is already authenticated and stored in the database
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNif(),
                        request.getPassword()
                )
        );

        UserCredentials user = credentialsRepository.findByNif(request.getNif());

        // create the response
        return createAuthenticationResponse(user);
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
        return createAuthenticationResponse(credentials);
    }

    @Transactional(readOnly = true)
    public ValidationResponse validate(String token) {
        String username = jwtService.extractUsername(token);
        if(username == null || !jwtService.isTokenValid(token, username)) return null;

        // if the user is not already authenticated
        UserCredentials user = credentialsRepository.findByNif(username);
        if(user == null) return null;

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            // if the token is valid, then allow to interact with the app
            // (create an authToken with the UserDetails and request information and add it to the security context)
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        return createValidationResponse(user);
    }

    private AuthenticationResponse createAuthenticationResponse(UserCredentials user) {
        // create a response with the token for the user
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private ValidationResponse createValidationResponse(UserCredentials user) {
        return ValidationResponse.builder()
                .nif(user.getNif())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> ERole.valueOf(role.getRolename()))
                        .collect(Collectors.toList())
                )
                .isAccountNonExpired(user.isAccountNonExpired())
                .isAccountNonLocked(user.isAccountNonLocked())
                .isCredentialsNonExpired(user.isCredentialsNonExpired())
                .isEnabled(user.isEnabled())
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