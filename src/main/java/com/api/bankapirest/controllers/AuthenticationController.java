package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AuthenticationRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.models.User;
import com.api.bankapirest.services.authentication.AuthenticationService;
import com.api.bankapirest.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User userDB = userService.findByNif(request.getNif());

        if(userDB != null) {
            return new ResponseEntity<>("User already registered", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }
}
