package com.api.bankapirest.controllers;

import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class TransactionController {

    private final ITransactionService transactionsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionsService.findById(id);

        if(transaction == null) {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<?> getTransactions() {
        List<Transaction> transactions = transactionsService.findAll();

        if(transactions == null || transactions.size() <= 0 ) {
            return new ResponseEntity<>("Transactions not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

}
