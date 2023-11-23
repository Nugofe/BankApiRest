package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AccountRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.utils.Utils;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.models.User;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.services.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;
    private final IAccountService accountService;
    private final ITransactionService transactionService;

    // ---------------------------------- USERS ----------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> getUser(@PathVariable Long id) throws ApiException {
        User user = userService.findById(id);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUsers() throws ApiException {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(Utils.buildUsersDTOs(users), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequest userRequest) throws ApiException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterRequest userRequest) throws ApiException {
        User userDB = userService.findById(id);
        User user = userService.update(userDB, userRequest);
        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws ApiException {
        userService.findById(id);
        userService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    // NOTE: This method was added in order to implement a simple rest client and circuit breaker.
    // The schema used in the users retrieved from this endpoint is different from the one used in this API.
    @GetMapping("/examples")
    public ResponseEntity<?> getUserExamples() throws Throwable {
        return new ResponseEntity<>(userService.getUserExamples(), HttpStatus.OK);
    }

    // ---------------------------------- ACCOUNTS ----------------------------------
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

    @GetMapping("/{user_id}/accounts")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccounts(@PathVariable(value = "user_id") Long userId) throws ApiException {
        //userService.findById(userId);
        List<Account> accounts = accountService.findAllByUser(userId);
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

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

    @GetMapping("/{user_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserTransactions(@PathVariable(value = "user_id") Long userId) throws ApiException {
        userService.findById(userId);
        List<Transaction> transactions = transactionService.findAllByUser(userId);
        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

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