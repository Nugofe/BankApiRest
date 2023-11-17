package com.api.bankapirest.services.transaction;

import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.repositories.IAccountRepository;
import com.api.bankapirest.repositories.ITransactionRepository;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final IAccountRepository accountRepository;
    private final ITransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Transaction findByAccount(Long accountId, Long transactionId) {
        return transactionRepository.findByAccount(accountId, transactionId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByAccount(Long accountId) {
        return transactionRepository.findAllByAccount(accountId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByEmitterAccount(Long accountId) {
        return transactionRepository.findAllByEmitterAccount(accountId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByReceiverAccount(Long accountId) {
        return transactionRepository.findAllByReceiverAccount(accountId);
    }

    @Transactional(readOnly = true)
    public Transaction findByUser(Long userId, Long transactionId) {
        return transactionRepository.findByUser(userId, transactionId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByUser(Long userId) {
        return transactionRepository.findAllByUser(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByEmitterUser(Long userId) {
        return transactionRepository.findAllByEmitterUser(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAllByReceiverUser(Long userId) {
        return transactionRepository.findAllByReceiverUser(userId);
    }

    @Transactional
    public ResponseEntity<?> save(Transaction transaction) {
        double finalMoney = transaction.getEmitterAccount().getMoney() - transaction.getMoney();
        if(finalMoney < 0) {
            return new ResponseEntity<>("Not enough money in emitter account to complete the transaction", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        transactionRepository.save(transaction);

        transaction.getEmitterAccount().setMoney(finalMoney);
        transaction.getReceiverAccount().setMoney( transaction.getReceiverAccount().getMoney() + transaction.getMoney() );
        accountRepository.save(transaction.getEmitterAccount());
        accountRepository.save(transaction.getReceiverAccount());

        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }
}