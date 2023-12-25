package com.bank.library.dtos.requests;

import com.bank.library.models.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequest extends AuthenticationRequest {

    @NotBlank(message = "{valid.user.firstname.NotBlank}")
    @Size(min = 2, max = 30, message = "{valid.user.firstname.Size}")
    @Schema(example = "Mario")
    private String firstname;

    @NotBlank(message = "{valid.user.surname.NotBlank}")
    @Size(min = 2, max = 60, message = "{valid.user.surname.Size}")
    @Schema(example = "Fernandez Garcia")
    private String surname;

    @Enumerated(EnumType.STRING)
    private List<ERole> roles;

    public RegisterRequest() {
        super();

        roles = new ArrayList<>();
        roles.add(0, ERole.USER);
    }

    public RegisterRequest(String nif, String password, String firstname, String surname) {
        super(nif, password);
        this.firstname = firstname;
        this.surname = surname;

        roles = new ArrayList<>();
        roles.add(0, ERole.USER);
    }

    public RegisterRequest(String nif, String password, String firstname, String surname, List<ERole> roles) {
        super(nif, password);
        this.firstname = firstname;
        this.surname = surname;

        Set<ERole> rolesSet = new HashSet<>(roles);
        setRoles(rolesSet);
    }

    private void setRoles(Set<ERole> roles) {
        this.roles = roles.stream().toList();
        this.roles.remove(ERole.USER);
        /*for(ERole r : roles) {
            if(r == ERole.USER) {
                this.roles.remove(r);
                break;
            }
        }*/
        this.roles.add(0, ERole.USER);
    }
}
