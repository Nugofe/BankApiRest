package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.request.AccountRequest;
import com.api.bankapirest.dtos.request.RegisterRequest;
import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.utils.Utils;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.models.User;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IAccountService accountService;
    private final ITransactionService transactionService;

    // ---------------------------------- USERS ----------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.findById(id);

        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.findAll();

        if(users == null || users.size() <= 0) {
            return new ResponseEntity<>("Users not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildUsersDTOs(users), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest userRequest) {
        User userDB = userService.findByNif(userRequest.getNif());

        if(userDB != null) {
            return new ResponseEntity<>("User already created", HttpStatus.CONFLICT);
        }

        User user = userService.buildUser(userRequest);
        userService.save(user);

        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequest userRequest) {
        User userDB = userService.findById(id);

        if(userDB == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userService.buildUser(userRequest);
        user.setId(userDB.getId());
        user.setCreatedAt(userDB.getCreatedAt());
        userService.save(user);

        return new ResponseEntity<>(Utils.buildUserDTO(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);

        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        userService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    // ---------------------------------- ACCOUNTS ----------------------------------
    @GetMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findByUser(userId, accountId);
        if(account == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        //account.setUser(null);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/accounts")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserAccounts(@PathVariable(value = "user_id") Long userId) {
        List<Account> accounts = accountService.findAllByUser(userId);

        if(accounts == null || accounts.size() <= 0) {
            return new ResponseEntity<>("Accounts not found for this user", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

    @PostMapping("/{user_id}/accounts")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> createUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @RequestBody AccountRequest accountRequest
    ) {
        User userDB = userService.findById(userId);

        if(userDB == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = Utils.buildAccount(accountRequest);
        account.setUser(userDB);

        accountService.save(account);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @PutMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> updateUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @RequestBody AccountRequest accountRequest
    ) {
        User userDB = userService.findById(userId);
        if(userDB == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account accountDB = accountService.findByUser(userId, accountId);
        if(accountDB == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        Account account = Utils.buildAccount(accountRequest);
        account.setId(accountDB.getId());
        account.setCreatedAt(accountDB.getCreatedAt());
        account.setUser(userDB);

        accountService.save(account);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/accounts/{account_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> deleteUserAccount(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findByUser(userId, accountId);
        if(account == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        accountService.delete(accountId);
        return new ResponseEntity<>("User account successfully deleted", HttpStatus.OK);
    }

    // ---------------------------------- TRANSACTIONS ----------------------------------
    @GetMapping("/{user_id}/transactions/{transaction_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "transaction_id") Long transactionId
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Transaction transaction = transactionService.findByUser(userId, transactionId);
        if(transaction == null) {
            return new ResponseEntity<>("Transaction not found for this user", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getUserTransactions(@PathVariable(value = "user_id") Long userId) {
        List<Transaction> transactions = transactionService.findAllByUser(userId);

        if(transactions == null || transactions.size() <= 0) {
            return new ResponseEntity<>("Transactions not found for this user", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/accounts/{account_id}/transactions/{transaction_id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getAccountTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @PathVariable(value = "transaction_id") Long transactionId
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findByUser(userId, accountId);
        if(account == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        Transaction transaction = transactionService.findByAccount(accountId, transactionId);
        if(transaction == null) {
            return new ResponseEntity<>("Transaction not found for this user account", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/accounts/{account_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> getAccountTransactions(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findById(accountId);
        if(account == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        List<Transaction> transactions = transactionService.findAllByAccount(accountId);
        if(transactions == null || transactions.size() <= 0) {
            return new ResponseEntity<>("Transactions not found for this user account", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

    @PostMapping("/{user_id}/accounts/{account_id}/transactions")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<?> createTransaction(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "account_id") Long accountId,
            @RequestBody TransactionRequest transactionRequest
    ) {
        User user = userService.findById(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountService.findByUser(userId, accountId);
        if(account == null) {
            return new ResponseEntity<>("Account not found for this user", HttpStatus.NOT_FOUND);
        }

        Account receiverAccount = accountService.findById(transactionRequest.getReceiverAccountId());
        if(receiverAccount == null) {
            return new ResponseEntity<>("Receiver account not found", HttpStatus.NOT_FOUND);
        }

        if(Objects.equals(account.getId(), receiverAccount.getId())) {
            return new ResponseEntity<>("The emitter and the receiver account cannot be the same", HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = Utils.buildTransaction(transactionRequest);
        transaction.setEmitterAccount(account);
        transaction.setReceiverAccount(receiverAccount);
        return transactionService.save(transaction);
    }
}