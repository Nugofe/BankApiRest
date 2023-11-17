package com.api.bankapirest.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank
    @Pattern(regexp = "[0-9]{8}[A-Z]")
    private String nif;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}
