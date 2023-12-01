package com.bank.accountservice.services.account;

import com.bank.accountservice.dtos.request.AccountRequest;
//import com.bank.accountservice.exceptions.ApiException;
import com.bank.accountservice.models.Account;
import com.bank.accountservice.models.User;

import java.util.List;

public interface IAccountService {

    public List<Account> findAll() /*throws ApiException*/;

    public Account findById(Long id) /*throws ApiException*/;

    public Account findByUser(Long userId, Long accountId) /*throws ApiException*/;

    public List<Account> findAllByUser(Long userId) /*throws ApiException*/;

    public Account create(AccountRequest accountRequest, User user) /*throws ApiException*/;

    public Account update(Account accountDB, User userDB, AccountRequest accountRequest) /*throws ApiException*/;

    public void delete(Long id);
}
