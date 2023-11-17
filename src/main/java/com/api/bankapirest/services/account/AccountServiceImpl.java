package com.api.bankapirest.services.account;

import com.api.bankapirest.models.Account;
import com.api.bankapirest.repositories.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByUser(Long userId, Long accountId) {
        return accountRepository.findByUser(userId, accountId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Account> findAllByUser(Long userId) {
        return accountRepository.findAllByUser(userId);
    }

    @Transactional
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
