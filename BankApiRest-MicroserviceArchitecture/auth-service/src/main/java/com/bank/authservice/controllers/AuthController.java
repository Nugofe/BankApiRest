 package com.bank.authservice.controllers;

import com.bank.authservice.services.auth.AuthService;
import com.bank.library.dtos.requests.LoginRequest;
import com.bank.library.dtos.requests.RegisterRequest;
import com.bank.library.dtos.requests.ValidationRequest;
import com.bank.library.dtos.responses.ValidationResponse;
import com.bank.library.exceptions.ApiException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication Endpoints. Accessible without authorization.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated// extract the jwt token and the username in the token
public class AuthController {

    private final AuthService authService;

    /*@Operation(
            operationId = "login",
            summary = "User login",
            description = "Retrieve a user given its id. The expected response is a JWT token.",
            tags = { "users", "auth", "POST" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "An authentication response with an access token", content = {
                    @Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws ApiException, UsernameNotFoundException {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    /*@Operation(
            operationId = "register",
            summary = "User registry",
            description = "Register a new user in the database. The expected response is a JWT token.",
            tags = { "users", "auth", "POST" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "An authentication response with an access token", content = {
                    @Content(schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws ApiException {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ValidationResponse validate(@Valid @RequestBody ValidationRequest request) {
        return authService.validate(request.getAccessToken());
    }
}
