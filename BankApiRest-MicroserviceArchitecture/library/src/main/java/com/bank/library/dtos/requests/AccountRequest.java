package com.bank.library.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class AccountRequest {

    @NotBlank(message = "{valid.account.accountName.NotBlank}")
    @Size(min = 2, max = 50, message = "{valid.account.accountName.Size}")
    @Schema(example = "Santander")
    private String accountName;

    @Schema(example = "2000.0")
    private Double money;

    @NotNull(message = "User id cannot be blank")
    @Schema(example = "1")
    private Long userId;

}
