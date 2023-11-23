package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AccountRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.dtos.response.AccountDTO;
import com.api.bankapirest.dtos.response.ErrorResponse;
import com.api.bankapirest.dtos.response.UserDTO;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.utils.Utils;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.models.User;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.services.user.IUserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final IAccountService accountService;
    private final ITransactionService transactionService;

    // ---------------------------------- USERS ----------------------------------
    @Operation(
            summary = "Get a user by id",
            description = "Retrieve a user given its id. The expected response is the user.",
            tags = { "users", "GET" })
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
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> getUser(@PathVariable Long id) throws ApiException {
        User user = userService.findById(id);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all users",
            description = "Get all the users stored the in database. The expected response is al list with all the users.",
            tags = { "users", "GET" })
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUsers() throws ApiException {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(Utils.buildUsersDTOs(users), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a user",
            description = "Create a new user and store it in the database. The expected response is the new user.",
            tags = { "users", "POST" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "409", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequest userRequest) throws ApiException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user",
            description = "Update an existent user in the database. The expected response is the updated user.",
            tags = { "users", "PUT" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
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
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterRequest userRequest) throws ApiException {
        User userDB = userService.findById(id);
        User user = userService.update(userDB, userRequest);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete an existent user from the database. " +
                    "The expected response is a message informing that the user was deleted successfully.",
            tags = { "users", "DELETE" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = String.class), mediaType = "application/json")
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
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws ApiException {
        userService.findById(id);
        userService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    // NOTE: This method was added in order to implement a simple rest client and circuit breaker.
    // The schema used in the users retrieved from this endpoint is different from the one used in this API.
    @Operation(
            summary = "Get user examples from external API",
            description = "Get user examples from an external API. " +
                    "This method was added in order to implement a simple rest client and circuit breaker. " +
                    "The schema used in the users retrieved from this endpoint is different from the one used in this API.",
            tags = { "users", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json")
            }),
            /*@ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),*/
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/examples")
    public ResponseEntity<?> getUserExamples() throws Throwable {
        return new ResponseEntity<>(userService.getUserExamples(), HttpStatus.OK);
    }

    // ---------------------------------- ACCOUNTS ----------------------------------
    @Operation(
            summary = "Get a user account",
            description = "Retrieve the account of a user given the account id and the user id. " +
                    "The expected response is the user account.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) throws ApiException {
        //userService.findById(userId);
        Account account = accountService.findByUser(userId, accountId);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all user accounts.",
            description = "Retrieve all the user accounts given the user id. " +
                    "The expected response is the list of accounts of the user.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccounts(@PathVariable(value = "user_id") Long userId) throws ApiException {
        //userService.findById(userId);
        List<Account> accounts = accountService.findAllByUser(userId);
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a user account.",
            description = "Create a new user account given the user id, and store it in the database. " +
                    "The expected response is the new user account.",
            tags = { "accounts", "POST" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "409", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @PostMapping("/{user_id}/accounts")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> createUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @Valid @RequestBody AccountRequest accountRequest
    ) throws ApiException {
        User user = userService.findById(userId);
        Account account = accountService.create(accountRequest, user);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user account.",
            description = "Update an existent user account given the account id and the user id. " +
                    "The expected response is the updated user account.",
            tags = { "accounts", "PUT" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
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
    @PutMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> updateUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @Valid @RequestBody AccountRequest accountRequest
    ) throws ApiException {
        User userDB = userService.findById(userId);
        Account accountDB = accountService.findByUser(userId, accountId);
        Account account = accountService.update(accountDB, userDB, accountRequest);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user account.",
            description = "Delete an existent user account given the account id and the user id. " +
                    "The expected response is a message informing the account was deleted successfully.",
            tags = { "accounts", "DELETE" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
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
    @DeleteMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> deleteUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) throws ApiException {
        userService.findById(userId);
        accountService.findByUser(userId, accountId);
        accountService.delete(accountId);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }

    // ---------------------------------- TRANSACTIONS ----------------------------------
    @Operation(
            summary = "Get a user transaction.",
            description = "Retrieve a transaction given the transaction id and the user id of one of the users involved in the transaction. " +
                    "The expected response is the user transaction.",
            tags = { "transactions", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/transactions/{transaction_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "transaction_id") Long transactionId
    )  throws ApiException {
        userService.findById(userId);
        Transaction transaction = transactionService.findByUser(userId, transactionId);
        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user transactions.",
            description = "Retrieve all the transaction of a user given the user id. " +
                    "The expected response is a list with the user transactions.",
            tags = { "transactions", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserTransactions(@PathVariable(value = "user_id") Long userId) throws ApiException {
        userService.findById(userId);
        List<Transaction> transactions = transactionService.findAllByUser(userId);
        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

    @Operation(
            summary = "Get an account transaction.",
            description = "Retrieve a transaction given the transaction id and the account and user ids of one of the users involved in the transaction. " +
                    "The expected response is the account transaction.",
            tags = { "transactions", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts/{account_id}/transactions/{transaction_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getAccountTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @PathVariable(value = "transaction_id") Long transactionId
    ) throws ApiException {
        userService.findById(userId);
        accountService.findByUser(userId, accountId);
        Transaction transaction = transactionService.findByAccount(accountId, transactionId);
        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }

    @Operation(
            summary = "Get an account transaction.",
            description = "Retrieve a transaction given the transaction id and the account and user ids of one of the users involved in the transaction. " +
                    "The expected response is the account transaction.",
            tags = { "transactions", "GET" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
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
    @GetMapping("/{user_id}/accounts/{account_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getAccountTransactions(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) throws ApiException {
        userService.findById(userId);
        accountService.findByUser(userId, accountId);
        List<Transaction> transactions = transactionService.findAllByAccount(accountId);
        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a transaction.",
            description = "Create a new transaction given the user id and the account id that will emit the transaction, and store it in the database. " +
                    "The expected response is the new transaction.",
            tags = { "transactions", "POST" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "503", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @PostMapping("/{user_id}/accounts/{account_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> createTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @Valid @RequestBody TransactionRequest transactionRequest
    ) throws ApiException {
        userService.findById(userId);
        Account emitterAccount = accountService.findByUser(userId, accountId);
        Account receiverAccount = accountService.findById(transactionRequest.getReceiverAccountId());
        Transaction transaction = transactionService.create(transactionRequest, emitterAccount, receiverAccount);
        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }
}