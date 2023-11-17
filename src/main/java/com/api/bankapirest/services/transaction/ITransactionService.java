package com.api.bankapirest.services.transaction;

import com.api.bankapirest.models.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITransactionService {
    public Transaction findById(Long id);

    public List<Transaction> findAll();

    public Transaction findByAccount(Long accountId, Long transactionId);

    public List<Transaction> findAllByAccount(Long accountId);

    public List<Transaction> findAllByEmitterAccount(Long accountId);

    public List<Transaction> findAllByReceiverAccount(Long accountId);

    public Transaction findByUser(Long userId, Long transactionId);

    public List<Transaction> findAllByUser(Long userId);

    public List<Transaction> findAllByEmitterUser(Long userId);

    public List<Transaction> findAllByReceiverUser(Long userId);

    public ResponseEntity<?> save(Transaction account);

}
