package com.api.bankapirest.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @Schema(example =  "1")
    private Long emitterAccountId;

    @NotNull
    @NotNull(message = "{valid.transaction.receiverAccountId.NotNull}")
    @Schema(example =  "3")
    private Long receiverAccountId;

    @NotNull(message = "{valid.transaction.money.NotNull}")
    @DecimalMin(value = "0.1", message = "{valid.transaction.money.DecimalMin}")
    @Schema(example =  "500.0")
    private Double money;

}
