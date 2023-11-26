package com.api.bankapirest.dtos.response;

import com.api.bankapirest.models.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

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

    private List<Role> roles;

}
