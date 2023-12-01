package com.bank.accountservice.controllers;

import com.bank.accountservice.dtos.response.AccountDTO;
import com.bank.accountservice.dtos.response.ErrorResponse;
//import com.bank.accountservice.exceptions.ApiException;
import com.bank.accountservice.models.Account;
import com.bank.accountservice.services.account.IAccountService;
import com.bank.accountservice.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Account", description = "Account related Endpoints.")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class AccountController {

    private final IAccountService accountService;

    /*@Operation(
            operationId = "getOneAccount",
            summary = "Get an account by id",
            description = "Retrieve an account given its id. The expected response is the account.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The requested account", content = {
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId
    ) /*throws ApiException*/ {
        Account account;// = accountService.findById(id);
        if(userId == null) {
            account = accountService.findById(id);
        } else {
            account = accountService.findByUser(id, userId);
        }
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    /*@Operation(
            operationId = "getAllAccounts",
            summary = "Get all accounts",
            description = "Get all the accounts stored the in database. " +
                    "The expected response is al list with all the accounts.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of accounts", content = {
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
    @GetMapping()
    public ResponseEntity<List<AccountDTO>> getAccounts(
            @RequestParam(required = false) Long userId
    ) /*throws ApiException*/ {
        List<Account> accounts;// = accountService.findAll();
        if(userId == null) {
            accounts = accountService.findAll();
        } else {
            accounts = accountService.findAllByUser(userId);
        }
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountByUser(
            @PathVariable Long id,
            @RequestParam(required = true) Long userId
    ) {
        Account account = accountService.findByUser(userId, id);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<AccountDTO>> getAllAccountsByUser(
            @RequestParam(required = true) Long userId
    ) {
        List<Account> accounts = accountService.findAllByUser(userId);
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }*/
}
