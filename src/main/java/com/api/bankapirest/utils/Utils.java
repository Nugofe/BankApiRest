package com.api.bankapirest.utils;

import com.api.bankapirest.dtos.request.AccountRequest;
import com.api.bankapirest.dtos.request.TransactionRequest;
import com.api.bankapirest.dtos.response.AccountDTO;
import com.api.bankapirest.dtos.response.TransactionDTO;
import com.api.bankapirest.dtos.response.UserDTO;
import com.api.bankapirest.models.Account;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.models.User;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Utils {

    public static UserDTO buildUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .nif(user.getNif())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    public static Account buildAccount(AccountRequest request) {
        return Account.builder()
                .accountName(request.getAccountName())
                .money(request.getMoney() == null ? 0.0f : request.getMoney())
                .build();
    }

    public static AccountDTO buildAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .money(account.getMoney())
                .createdAt(account.getCreatedAt())
                .user(
                        Utils.buildUserDTO(account.getUser())
                )
                .build();
    }

    public static Transaction buildTransaction(TransactionRequest request) {
        return Transaction.builder()
                .money(request.getMoney() == null ? 0.0f : request.getMoney())
                .build();
    }

    public static TransactionDTO buildTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .emitterAccount(
                        Utils.buildAccountDTO(transaction.getEmitterAccount())
                )
                .receiverAccount(
                        Utils.buildAccountDTO(transaction.getReceiverAccount())
                )
                .money(transaction.getMoney())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static List<UserDTO> buildUsersDTOs(List<User> list) {
        List<UserDTO> dtos = new ArrayList<>();
        for(User elem : list) {
            dtos.add(Utils.buildUserDTO(elem));
        }
        return dtos;
    }

    public static List<AccountDTO> buildAccountsDTOs(List<Account> list) {
        List<AccountDTO> dtos = new ArrayList<>();
        for(Account elem : list) {
            dtos.add(Utils.buildAccountDTO(elem));
        }
        return dtos;
    }

    public static List<TransactionDTO> buildTransactionsDTOs(List<Transaction> list) {
        List<TransactionDTO> dtos = new ArrayList<>();
        for(Transaction elem : list) {
            dtos.add(Utils.buildTransactionDTO(elem));
        }
        return dtos;
    }

}
