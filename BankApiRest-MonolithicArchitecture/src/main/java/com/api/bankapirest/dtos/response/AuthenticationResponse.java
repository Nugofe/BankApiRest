package com.api.bankapirest.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    @JsonProperty("access_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3OEEiLCJpYXQiOjE3MDA4MTEzNjQsImV4cCI6MTcwMDg5Nzc2NH0.OgB2BXrip911QVTIJZ-PdWgSktwp5warVTfFdIlynJY")
    private String accessToken;

    @JsonProperty("refresh_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3OEEiLCJpYXQiOjE3MDA4MTEzNjQsImV4cCI6MTcwMTQxNjE2NH0.ubIkAYeuGuJ05clBa9TdROC7uTODYEtZHmwfL6-Gj7c")
    private String refreshToken;
}
