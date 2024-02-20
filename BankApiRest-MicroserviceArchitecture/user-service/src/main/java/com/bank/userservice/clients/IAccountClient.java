package com.bank.userservice.clients;

import com.bank.library.exceptions.ApiException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "${app.config.accounts-url}")
public interface IAccountClient {

    @GetMapping("/{id}")
    ResponseEntity<?> getAccount(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId
    ) throws ApiException;

    @GetMapping()
    ResponseEntity<?> getAccounts(
            @RequestParam(required = false) Long userId
    ) throws ApiException;
}