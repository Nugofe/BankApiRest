package com.api.bankapirest.repositories;

import com.api.bankapirest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// 2 types of repositories from JPA
// - CrudRepository: more simple, without pagination
// - JpaRepository: all the methods in CrudRepository + pagination
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories

public interface IUserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u from User u WHERE u.nif = ?1")
    Optional<User> findByNif(String nif);
}
