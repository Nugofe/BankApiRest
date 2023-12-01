package com.bank.library.dtos.responses;

import com.bank.library.models.ERole;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "12345678A")
    private String nif;

    @Schema(example = "Mario")
    private String firstname;

    @Schema(example = "Fernandez Garcia")
    private String surname;

    @Schema(type = "string", pattern = "yyyy-MM-dd", example = "2017-08-01")
    private Date createdAt;

    @Schema(allowableValues = {"USER, ADMIN"}, example = "USER")
    private List<ERole> roles;

}
