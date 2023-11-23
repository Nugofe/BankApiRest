package com.api.bankapirest.dtos.request;

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
    private Long emitterAccountId;

    @NotNull
    @NotNull(message = "{valid.transaction.receiverAccountId.NotNull}")
    private Long receiverAccountId;

    @NotNull(message = "{valid.transaction.money.NotNull}")
    @DecimalMin(value = "0.1", message = "{valid.transaction.money.DecimalMin}")
    private Double money;

}
