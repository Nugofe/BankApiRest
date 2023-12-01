package com.bank.accountservice.services.account;

import com.bank.library.dtos.requests.AccountRequest;
import com.bank.accountservice.models.Account;
import com.bank.library.exceptions.ApiException;

import java.util.List;

public interface IAccountService {

    List<Account> findAll() throws ApiException;

    Account findById(Long id) throws ApiException;

    Account findByUser(Long userId, Long accountId) throws ApiException;

    List<Account> findAllByUser(Long userId) throws ApiException;

    Account create(AccountRequest accountRequest) throws ApiException;

    //Account update(Account accountDB, User userDB, AccountRequest accountRequest) throws ApiException;

    void delete(Long id);
}
