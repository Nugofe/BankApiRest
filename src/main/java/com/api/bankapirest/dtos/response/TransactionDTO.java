package com.api.bankapirest.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    @Schema(example = "1")
    private Long id;

    private AccountDTO emitterAccount;

    private AccountDTO receiverAccount;

    @Schema(example = "500.0")
    private Double money;

    @Schema(type = "string", pattern = "yyyy-MM-dd", example = "2017-08-01")
    private Date createdAt;

}
