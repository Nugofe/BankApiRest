package com.api.bankapirest.controllers;

import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class TransactionController {

    private final ITransactionService transactionsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) throws ApiException {
        Transaction transaction = transactionsService.findById(id);
        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<?> getTransactions() throws ApiException {
        List<Transaction> transactions = transactionsService.findAll();
        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

}
