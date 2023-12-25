package com.bank.library.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotBlank(message = "{valid.user.nif.NotBlank}")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "{valid.user.nif.Pattern}")
    @Schema(example = "12345678A")
    private String nif;

    @NotBlank(message = "{valid.user.password.NotBlank}")
    @Size(min = 4, max = 20)
    @Schema(example = "MySecretPassword")
    private String password;
}
