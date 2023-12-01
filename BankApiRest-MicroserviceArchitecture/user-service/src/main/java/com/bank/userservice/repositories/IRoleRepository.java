package com.bank.userservice.repositories;

import com.bank.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r from Role r WHERE r.rolename = ?1")
    Optional<Role> findByRolename(String rolename);
}
