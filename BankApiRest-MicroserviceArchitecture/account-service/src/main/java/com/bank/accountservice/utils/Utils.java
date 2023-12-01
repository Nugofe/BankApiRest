package com.bank.accountservice.utils;

import com.bank.accountservice.models.Account;
import com.bank.library.dtos.requests.AccountRequest;
import com.bank.library.dtos.responses.AccountResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Utils {

    public static Account mapRequestToAccount(AccountRequest request) {
        return Account.builder()
                .accountName(request.getAccountName())
                .money(request.getMoney() == null ? 0.0f : request.getMoney())
                .userId(request.getUserId())
                .build();
    }

    public static AccountResponse mapAccountToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .money(account.getMoney())
                .createdAt(account.getCreatedAt())
                .userId(account.getUserId())
                .build();
    }

    public static List<AccountResponse> mapAccountListToResponse(List<Account> list) {
        List<AccountResponse> dtos = new ArrayList<>();
        for(Account elem : list) {
            dtos.add(Utils.mapAccountToResponse(elem));
        }
        return dtos;
    }

}
