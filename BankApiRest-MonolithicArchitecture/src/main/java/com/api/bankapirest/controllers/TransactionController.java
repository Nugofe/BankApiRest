package com.api.bankapirest.controllers;

import com.api.bankapirest.dtos.response.ErrorResponse;
import com.api.bankapirest.dtos.response.TransactionDTO;
import com.api.bankapirest.exceptions.ApiException;
import com.api.bankapirest.models.Transaction;
import com.api.bankapirest.services.transaction.ITransactionService;
import com.api.bankapirest.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Transaction", description = "Only Transaction related Endpoints. Only accessible to users with role ADMIN.")
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class TransactionController {

    private final ITransactionService transactionsService;

    @Operation(
            operationId = "getOneTransaction",
            summary = "Get an transaction by id",
            description = "Retrieve an transaction given its id. The expected response is the transaction.",
            tags = { "accounts", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The requested transaction", content = {
                    @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) throws ApiException {
        Transaction transaction = transactionsService.findById(id);
        return new ResponseEntity<>(Utils.buildTransactionDTO(transaction), HttpStatus.OK);
    }

    @Operation(
            operationId = "getAllTransactions",
            summary = "Get all transactions",
            description = "Get all the transactions stored the in database. " +
                    "The expected response is al list with all the transactions.",
            tags = { "transactions", "GET" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of transactions", content = {
                    @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping()
    public ResponseEntity<?> getTransactions() throws ApiException {
        List<Transaction> transactions = transactionsService.findAll();
        return new ResponseEntity<>(Utils.buildTransactionsDTOs(transactions), HttpStatus.OK);
    }

}
