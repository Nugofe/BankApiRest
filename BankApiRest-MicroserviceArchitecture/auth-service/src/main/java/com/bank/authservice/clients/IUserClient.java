package com.bank.authservice.clients;

import com.bank.library.dtos.requests.RegisterRequest;
import com.bank.library.exceptions.ApiException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${application.config.users-url}")
public interface IUserClient {

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable Long id) throws ApiException;

    @GetMapping("/nif/{nif}")
    ResponseEntity<?> getUserDetails(@PathVariable String nif) throws ApiException;

    @GetMapping()
    ResponseEntity<?> createUser(@RequestBody RegisterRequest userRequest) throws ApiException;

}
