package com.api.bankapirest.repositories;

import com.api.bankapirest.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t from Transaction t WHERE (t.emitterAccount.id = ?1 or t.receiverAccount.id = ?1) and t.id = ?2")
    Optional<Transaction> findByAccount(Long accountId, Long transactionId);

    @Query(value = "SELECT t from Transaction t WHERE (t.emitterAccount.id = ?1 or t.receiverAccount.id = ?1)")
    List<Transaction> findAllByAccount(Long accountId);

    @Query(value = "SELECT t from Transaction t WHERE t.emitterAccount.id = ?1")
    List<Transaction> findAllByEmitterAccount(Long accountId);

    @Query(value = "SELECT t from Transaction t WHERE t.receiverAccount.id = ?1")
    List<Transaction> findAllByReceiverAccount(Long accountId);

    @Query(value = "SELECT t from Transaction t WHERE (t.emitterAccount.user.id = ?1 or t.receiverAccount.user.id = ?1) and t.id = ?2")
    Optional<Transaction> findByUser(Long userId, Long transactionId);

    @Query(value = "SELECT t from Transaction t WHERE (t.emitterAccount.user.id = ?1 or t.receiverAccount.user.id = ?1)")
    List<Transaction> findAllByUser(Long userId);

    @Query(value = "SELECT t from Transaction t WHERE t.emitterAccount.user.id = ?1")
    List<Transaction> findAllByEmitterUser(Long userId);

    @Query(value = "SELECT t from Transaction t WHERE t.receiverAccount.user.id = ?1")
    List<Transaction> findAllByReceiverUser(Long userId);
}
