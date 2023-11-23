package com.api.bankapirest.services.transaction;

import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.exceptions.BadRequestException;
import com.api.bankapirest.exceptions.NotFoundException;
import com.api.bankapirest.exceptions.UnprocessableEntityException;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.repositories.IAccountRepository;
import com.api.bankapirest.repositories.ITransactionRepository;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"transactions"})
public class TransactionServiceImpl implements ITransactionService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    @Cacheable()
    public List<Transaction> findAll() throws ApiException {
        List<Transaction> transactions = transactionRepository.findAll();
        if(transactions.isEmpty()) {
            throw new NotFoundException("Transactions");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #id }")
    public Transaction findById(Long id) throws ApiException {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if(transaction == null) {
            throw new NotFoundException("Transaction");
        }
        return transaction;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #accountId, #transactionId }")
    public Transaction findByAccount(Long accountId, Long transactionId) throws ApiException {
        Transaction transaction = transactionRepository.findByAccount(accountId, transactionId).orElse(null);
        if(transaction == null) {
            throw new NotFoundException("Transaction for this account");
        }
        return transaction;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #accountId }")
    public List<Transaction> findAllByAccount(Long accountId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByAccount(accountId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this account");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #accountId }")
    public List<Transaction> findAllByEmitterAccount(Long accountId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByEmitterAccount(accountId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this emitter account");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #accountId }")
    public List<Transaction> findAllByReceiverAccount(Long accountId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByReceiverAccount(accountId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this receiver account");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId, #transactionId }")
    public Transaction findByUser(Long userId, Long transactionId) throws ApiException {
        Transaction transaction = transactionRepository.findByUser(userId, transactionId).orElse(null);
        if(transaction == null) {
            throw new NotFoundException("Transaction for this user");
        }
        return transaction;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId }")
    public List<Transaction> findAllByUser(Long userId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByUser(userId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this user");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId }")
    public List<Transaction> findAllByEmitterUser(Long userId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByEmitterUser(userId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this emitter user");
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId }")
    public List<Transaction> findAllByReceiverUser(Long userId) throws ApiException {
        List<Transaction> transactions = transactionRepository.findAllByReceiverUser(userId);
        if(transactions == null || transactions.isEmpty()) {
            throw new NotFoundException("Transactions for this receiver user");
        }
        return transactions;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value="transactions", allEntries=true),
            @CacheEvict(value="accounts", allEntries=true)
    })
    public Transaction create(
            TransactionRequest transactionRequest,
            Account emitterAccount,
            Account receiverAccount
    ) throws ApiException
    {
        if(Objects.equals(emitterAccount.getId(), receiverAccount.getId())) {
            throw new BadRequestException("The emitter and the receiver account cannot be the same");
        }
        double finalMoney = emitterAccount.getMoney() - transactionRequest.getMoney();
        if(finalMoney < 0) {
            throw new UnprocessableEntityException("Not enough money in emitter account to complete the transaction");
        }
        Transaction transaction = Utils.buildTransaction(transactionRequest);
        transaction.setEmitterAccount(emitterAccount);
        transaction.setReceiverAccount(receiverAccount);

        emitterAccount.setMoney(finalMoney);
        receiverAccount.setMoney( receiverAccount.getMoney() + transaction.getMoney() );
        accountRepository.save(emitterAccount);
        accountRepository.save(receiverAccount);

        return transactionRepository.save(transaction);
    }
}
