package com.bank.userservice.clients;

import com.bank.userservice.dtos.response.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "account-service", url = "{application.config.accounts-url}")
public interface IAccountClient {

    @GetMapping("/{id}")
    ResponseEntity<?> getAccount(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId
    ) /*throws ApiException*/;

    @GetMapping()
    ResponseEntity<List<AccountDTO>> getAccounts(
            @RequestParam(required = false) Long userId
    ) /*throws ApiException*/;
}
