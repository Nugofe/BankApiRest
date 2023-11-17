package com.api.bankapirest.repositories;

import com.api.bankapirest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r from Role r WHERE r.rolename = ?1")
    Optional<Role> findByRolename(String rolename);
}
