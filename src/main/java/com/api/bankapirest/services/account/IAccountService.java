package com.api.bankapirest.services.account;

import com.api.bankapirest.models.Account;

import java.util.List;

public interface IAccountService {

    public Account findById(Long id);

    public List<Account> findAll();

    public Account findByUser(Long userId, Long accountId);

    public List<Account> findAllByUser(Long userId);

    public void save(Account account);

    public void delete(Long id);
}
