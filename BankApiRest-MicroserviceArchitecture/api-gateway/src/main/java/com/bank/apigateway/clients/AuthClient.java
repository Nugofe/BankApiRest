package com.bank.apigateway.clients;

import com.bank.library.dtos.requests.ValidationRequest;
import com.bank.library.dtos.responses.ValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service", url = "${app.config.auth-url}")
public interface AuthClient {

    @PostMapping("/validate")
    ValidationResponse validate(ValidationRequest request);

}