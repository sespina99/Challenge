package com.mendel.challenge.controller;


import com.mendel.challenge.dto.TransactionRequestDTO;
import com.mendel.challenge.dto.TransactionResponseDTO;
import com.mendel.challenge.interfaces.services.TransactionService;
import com.mendel.challenge.model.Transaction;
import com.mendel.challenge.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //POST /transactions -> creo nueva transaction
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction.getAmount(),transaction.getType(), transaction.getParentId());
        return createdTransaction == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(TransactionResponseDTO.generateDTO(createdTransaction));

    }

    @PutMapping("/{transaction_id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable long transaction_id, @RequestBody TransactionRequestDTO transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transaction_id, transaction.getAmount(), transaction.getType(), transaction.getParentId());
        return updatedTransaction == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(TransactionResponseDTO.generateDTO(updatedTransaction));
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable String type) {
        List<Long> transactionIds = transactionService.getTransactionsByType(type);
        return transactionIds.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionIds);
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransactionSum(@PathVariable long transaction_id) {
        Double sum = transactionService.getTotalSumForTransaction(transaction_id);
        return sum == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(Map.of("sum", sum));
    }
}
