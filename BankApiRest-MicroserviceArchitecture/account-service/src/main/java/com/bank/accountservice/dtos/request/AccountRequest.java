package com.bank.accountservice.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotBlank(message = "{valid.account.accountName.NotBlank}")
    @Size(min = 2, max = 50, message = "{valid.account.accountName.Size}")
    @Schema(example = "Santander")
    private String accountName;

    @Schema(example = "2000.0")
    private Double money;

}
