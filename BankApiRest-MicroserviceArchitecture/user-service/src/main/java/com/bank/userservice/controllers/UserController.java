package com.bank.userservice.controllers;

import com.bank.library.dtos.requests.UserRequest;
import com.bank.library.exceptions.ApiException;
import com.bank.userservice.clients.IAccountClient;
import com.bank.userservice.utils.Utils;
import com.bank.userservice.models.User;
import com.bank.userservice.services.user.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User related Endpoints")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;
    private final IAccountClient accountService;

    // ---------------------------------- USERS ----------------------------------
    /*@Operation(
            operationId = "getOneUser",
            summary = "Get a user by id",
            description = "Retrieve a user given its id. The expected response is the user.",
            tags = { "users", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The requested user", content = {
                    @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")
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
    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> getUser(@PathVariable Long id) throws ApiException {
        User user = userService.findById(id);
        return new ResponseEntity<>(Utils.mapUserToResponse(user), HttpStatus.OK);
    }

    /*@Operation(
            operationId = "getAllUsers",
            summary = "Get all users",
            description = "Get all the users stored the in database. The expected response is al list with all the users.",
            tags = { "users", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of users", content = {
                    @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")
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
    @GetMapping()
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUsers() throws ApiException {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(Utils.mapUserListToResponse(users), HttpStatus.OK);
    }

    /*@Operation(
            operationId = "createOneUser",
            summary = "Create a user",
            description = "Create a new user and store it in the database. The expected response is the new user.",
            tags = { "users", "POST" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The created user", content = {
                    @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")
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
                    responseCode = "409",
                    description = "Conflict", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @PostMapping()
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) throws ApiException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(Utils.mapUserToResponse(user), HttpStatus.CREATED);
    }

    /*@Operation(
            operationId = "updateOneUser",
            summary = "Update a user",
            description = "Update an existent user in the database. The expected response is the updated user.",
            tags = { "users", "PUT" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The updated user", content = {
                    @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")
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
            }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest
    ) throws ApiException {
        User userDB = userService.findById(id);
        User user = userService.update(userDB, userRequest);
        return new ResponseEntity<>(Utils.mapUserToResponse(user), HttpStatus.OK);
    }

    /*@Operation(
            operationId = "deleteOneUser",
            summary = "Delete a user",
            description = "Delete an existent user from the database. " +
                    "The expected response is a message informing that the user was deleted successfully.",
            tags = { "users", "DELETE" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "A message informing the requested user was deleted successfully", content = {
                    @Content(schema = @Schema(example = "User deleted successfully"), mediaType = "application/json")
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
            }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws ApiException {
        userService.findById(id);
        userService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    // NOTE: This method was added in order to implement a simple rest client and circuit breaker.
    // The schema used in the users retrieved from this endpoint is different from the one used in this API.
    /*@Operation(
            operationId = "getExternalUserExamples",
            summary = "Get user examples from external API",
            description = "Get user examples from an external API. " +
                    "This method was added in order to implement a simple rest client and circuit breaker. " +
                    "The schema used in the users retrieved from this endpoint is different from the one used in this API.",
            tags = { "users", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of user examples", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })*/
    @GetMapping("/examples")
    public ResponseEntity<?> getUserExamples() throws Throwable {
        return new ResponseEntity<>(userService.getUserExamples(), HttpStatus.OK);
    }

    // ---------------------------------- ACCOUNTS ----------------------------------
    /*@Operation(
            operationId = "getOneUserAccount",
            summary = "Get a user account by id",
            description = "Retrieve the account of a user given the account id and the user id. " +
                    "The expected response is the user account.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The requested user account", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts/{account_id}")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) throws ApiException {
        userService.findById(userId);
        return accountService.getAccount(userId, accountId);
    }

    /*@Operation(
            operationId = "getAllUserAccounts",
            summary = "Get all user accounts.",
            description = "Retrieve all the user accounts given the user id. " +
                    "The expected response is the list of accounts of the user.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of accounts that belong to the user", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts")
    //@PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccounts(@PathVariable(value = "user_id") Long userId) throws ApiException {
        userService.findById(userId);
        return accountService.getAccounts(userId);
    }

}