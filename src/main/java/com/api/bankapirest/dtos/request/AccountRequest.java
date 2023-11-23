package com.api.bankapirest.dtos.request;

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
    //@JsonProperty("account_name")
    private String accountName;

    private Double money;

}
