package com.api.bankapirest.services.transaction;

import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;

import java.util.List;

public interface ITransactionService {

    public List<Transaction> findAll() throws ApiException;

    public Transaction findById(Long id) throws ApiException;

    public Transaction findByAccount(Long accountId, Long transactionId) throws ApiException;

    public List<Transaction> findAllByAccount(Long accountId) throws ApiException;

    public List<Transaction> findAllByEmitterAccount(Long accountId) throws ApiException;

    public List<Transaction> findAllByReceiverAccount(Long accountId) throws ApiException;

    public Transaction findByUser(Long userId, Long transactionId) throws ApiException;

    public List<Transaction> findAllByUser(Long userId) throws ApiException;

    public List<Transaction> findAllByEmitterUser(Long userId) throws ApiException;

    public List<Transaction> findAllByReceiverUser(Long userId) throws ApiException;

    public Transaction create(
            TransactionRequest transactionRequest,
            Account emitterAccount,
            Account receiverAccount
    ) throws ApiException;

}
