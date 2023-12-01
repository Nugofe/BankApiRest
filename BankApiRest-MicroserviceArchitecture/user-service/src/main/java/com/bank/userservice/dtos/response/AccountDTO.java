package com.bank.userservice.dtos.response;

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
public class AccountDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Santander")
    private String accountName;

    @Schema(example = "2000.0")
    private Double money;

    @Schema(example = "1")
    private Long userId;

    @Schema(type = "string", pattern = "yyyy-MM-dd", example = "2017-08-01")
    private Date createdAt;

}
