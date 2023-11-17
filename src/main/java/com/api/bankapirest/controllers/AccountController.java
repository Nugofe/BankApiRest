package com.api.bankapirest.controllers;

import com.api.bankapirest.models.Account;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AccountController {

    private final IAccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        Account account = accountService.findById(id);

        if(account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAccounts() {
        List<Account> accounts = accountService.findAll();

        if(accounts == null || accounts.size() <= 0 ) {
            return new ResponseEntity<>("Accounts not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

}
