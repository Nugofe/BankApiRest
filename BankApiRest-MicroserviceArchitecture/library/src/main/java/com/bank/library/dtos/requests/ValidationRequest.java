package com.bank.library.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {

    @JsonProperty("access_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3OEEiLCJpYXQiOjE3MDA4MTEzNjQsImV4cCI6MTcwMDg5Nzc2NH0.OgB2BXrip911QVTIJZ-PdWgSktwp5warVTfFdIlynJY")
    private String accessToken;

}