package com.bank.accountservice.services.account;

import com.bank.accountservice.models.Account;
import com.bank.accountservice.repositories.IAccountRepository;
import com.bank.accountservice.utils.Utils;
import com.bank.library.dtos.requests.AccountRequest;
import com.bank.library.exceptions.ApiException;
import com.bank.library.exceptions.NotFoundException;
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
    @Cacheable()
    public List<Account> findAll() throws ApiException {
        List<Account> accounts = accountRepository.findAll();
        if(accounts.isEmpty()) {
            throw new NotFoundException("Accounts");
        }
        return accounts;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #id }")
    public Account findById(Long id) throws ApiException {
        Account account = accountRepository.findById(id).orElse(null);
        if(account == null) {
            throw new NotFoundException("Account with id=" + id);
        }
        return account;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId, #accountId }")
    public Account findByUser(Long userId, Long accountId) throws ApiException {
        Account account = accountRepository.findByUser(userId, accountId).orElse(null);
        if(account == null) {
            throw new NotFoundException("Account for this user");
        }
        return account;
    }

    @Transactional(readOnly = true)
    @Cacheable(key="{ #root.methodName, #userId }")
    public List<Account> findAllByUser(Long userId) throws ApiException {
        List<Account> accounts = accountRepository.findAllByUser(userId);
        if(accounts == null || accounts.isEmpty()) {
            throw new NotFoundException("Accounts for this user");
        }
        return accounts;
    }

    @Transactional
    @CacheEvict(allEntries=true)
    public Account create(AccountRequest accountRequest) throws ApiException {
        Account account = Utils.mapRequestToAccount(accountRequest);
        return accountRepository.save(account);
    }

    /*@Transactional
    @CacheEvict(allEntries=true)
    public Account update(Account accountDB, User userDB, AccountRequest accountRequest) throws ApiException {
        Account account = Utils.mapRequestToAccount(accountRequest);
        account.setId(accountDB.getId());
        account.setCreatedAt(accountDB.getCreatedAt());
        //account.setUser(userDB);

        return accountRepository.save(account);
    }*/

    @Transactional
    @CacheEvict(allEntries=true)
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
