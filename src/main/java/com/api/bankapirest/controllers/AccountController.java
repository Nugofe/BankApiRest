package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.response.ErrorResponse;
import com.api.bankapirest.dtos.response.UserDTO;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Account", description = "Only Account related Endpoints. Only accessible to users with role ADMIN.")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class AccountController {

    private final IAccountService accountService;

    @Operation(
            summary = "Get an account by id",
            description = "Retrieve an account given its id. The expected response is the account.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) throws ApiException {
        Account account = accountService.findById(id);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all accounts",
            description = "Get all the accounts stored the in database. " +
                    "The expected response is al list with all the accounts.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping()
    public ResponseEntity<?> getAccounts() throws ApiException {
        List<Account> accounts = accountService.findAll();
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

}
