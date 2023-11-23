package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AuthenticationRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.services.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest request) throws ApiException, UsernameNotFoundException {
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws ApiException {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }
}
