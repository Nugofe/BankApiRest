package com.bank.authservice.models;

import com.bank.library.models.ERole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credentials")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentials implements UserDetails {

    @Id
    @NotBlank(message = "{valid.user.nif.NotBlank}")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "{valid.user.nif.Pattern}")
    private String nif;

    @NotBlank(message = "{valid.user.password.NotBlank}")
    @Size(min = 4, max = 20)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_nif", referencedColumnName = "nif"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_nif", "role_id"})}
    )
    private List<Role> roles;

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
    }

    @Override
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
    }
}
