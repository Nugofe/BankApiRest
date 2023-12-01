package com.bank.userservice.dtos.request;

import com.bank.userservice.models.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@EqualsAndHashCode(callSuper = true)
@Data
public class UserRequestDTO /*extends AuthenticationRequest*/ {

    @NotBlank(message = "{valid.user.nif.NotBlank}")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "{valid.user.nif.Pattern}")
    @Schema(example = "12345678A")
    private String nif;

    @NotBlank(message = "{valid.user.password.NotBlank}")
    @Size(min = 4, max = 20)
    @Schema(example = "MySecretPassword")
    private String password;

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

    public UserRequestDTO() {
        super();

        roles = new ArrayList<>();
        roles.add(0, ERole.USER);
    }

    public UserRequestDTO(String nif, String password, String firstname, String surname) {
        //super(nif, password);
        this.nif = nif;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;

        roles = new ArrayList<>();
        roles.add(0, ERole.USER);
    }

    public UserRequestDTO(String nif, String password, String firstname, String surname, List<ERole> roles) {
        //super(nif, password);
        this.nif = nif;
        this.password = password;
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
