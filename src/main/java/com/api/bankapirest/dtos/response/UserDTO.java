package com.api.bankapirest.dtos.response;

import com.api.bankapirest.models.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    private String nif;
    private String firstname;
    private String surname;
    private Date createdAt;
    private List<Role> roles;

}
