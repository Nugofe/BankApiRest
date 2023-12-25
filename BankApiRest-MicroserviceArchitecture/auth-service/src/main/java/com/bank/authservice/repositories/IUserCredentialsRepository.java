package com.bank.authservice.repositories;

import com.bank.authservice.models.Role;
import com.bank.authservice.models.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    @Query(value = "SELECT u from UserCredentials u WHERE u.nif = ?1")
    UserCredentials findByNif(String nif);
}
