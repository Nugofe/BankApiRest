package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AuthenticationRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.dtos.response.AuthenticationResponse;
import com.api.bankapirest.dtos.response.ErrorResponse;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.services.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Authentication", description = "Authentication Endpoints. Accessible without authorization.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authService;

    @Operation(
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
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest request) throws ApiException, UsernameNotFoundException {
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }

    @Operation(
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
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws ApiException {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }
}
