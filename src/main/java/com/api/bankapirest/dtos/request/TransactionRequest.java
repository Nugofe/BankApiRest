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
    private Long receiverAccountId;

    @DecimalMin("0.1")
    private Double money;

}
