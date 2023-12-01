package com.bank.accountservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data    // shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
@Builder // using the Builder pattern without writing boilerplate code
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User /*implements UserDetails*/ { // UserDetails already implements Serializable

    private Long id;

    @NotBlank(message = "{valid.user.nif.NotBlank}")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "{valid.user.nif.Pattern}")
    private String nif;

    @NotBlank(message = "{valid.user.firstname.NotBlank}")
    @Size(min = 2, max = 30, message = "{valid.user.firstname.Size}")
    private String firstname;

    @NotBlank(message = "{valid.user.surname.NotBlank}")
    @Size(min = 2, max = 60, message = "{valid.user.surname.Size}")
    private String surname;

    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Valid
    private Date createdAt;

    //private List<Role> roles;
/*
    *//*public void setRoles(List<Role> roles) {
        Set<Role> rolesSet = new HashSet<>(roles);

        this.roles = rolesSet.stream().toList();
        Role userRole = new Role((long) ERole.USER.ordinal(), ERole.USER.name());
        this.roles.remove(userRole);
        this.roles.add(0, userRole);
    }*//*

    public void addRoles(List<Role> roles) {
        for(Role r : roles) {
            addRole(r);
        }
    }

    public void addRole(Role role) {
        // only add if not already present
        for(Role r : roles) {
            if (Objects.equals(r.getId(), role.getId())) {
                return;
            }
        }
        roles.add(role);
    }

    public void removeRole(Role role) {
        if(Objects.equals(role.getRolename(), ERole.USER.getName())) { // user role must always be present
            return;
        }

        for(Role r : roles) {
            if (Objects.equals(r.getId(), role.getId())) {
                roles.remove(role);
                break;
            }
        }
    }*/

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roleNames = new ArrayList<>();
        for(Role r : roles) {
            roleNames.add(new SimpleGrantedAuthority(r.getRolename()));
        }

        return roleNames;
    }

    @Override
    public String getUsername() {
        return nif;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }*/
}
