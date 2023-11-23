package com.api.bankapirest.controllers;

import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.services.account.IAccountService;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class AccountController {

    private final IAccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) throws ApiException {
        Account account = accountService.findById(id);
        return new ResponseEntity<>(Utils.buildAccountDTO(account), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAccounts() throws ApiException {
        List<Account> accounts = accountService.findAll();
        return new ResponseEntity<>(Utils.buildAccountsDTOs(accounts), HttpStatus.OK);
    }

}
