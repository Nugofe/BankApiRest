package com.api.bankapirest.repositories;

import com.api.bankapirest.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT a from Account a WHERE a.user.id = ?1 and a.id = ?2")
    Optional<Account> findByUser(Long userId, Long accountId);

    @Query(value = "SELECT a from Account a WHERE a.user.id = ?1")
    List<Account> findAllByUser(Long userId);
}
