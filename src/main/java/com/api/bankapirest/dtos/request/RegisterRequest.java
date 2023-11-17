package com.api.bankapirest.dtos.request;

import com.api.bankapirest.models.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
//@Builder(builderMethodName = "RegisterRequestBuilder")
public class RegisterRequest extends AuthenticationRequest {
    /*@NotBlank
    @Pattern(regexp = "[0-9]{8}[A-Z]")
    private String nif;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;*/

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 60)
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
