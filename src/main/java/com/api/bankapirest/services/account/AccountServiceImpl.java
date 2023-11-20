package com.api.bankapirest.services.account;

import com.api.bankapirest.models.Account;
import com.api.bankapirest.repositories.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames={"accounts"})
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #id }")
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Cacheable()
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId, #accountId }")
    public Account findByUser(Long userId, Long accountId) {
        return accountRepository.findByUser(userId, accountId).orElse(null);
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId }")
    public List<Account> findAllByUser(Long userId) {
        return accountRepository.findAllByUser(userId);
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
