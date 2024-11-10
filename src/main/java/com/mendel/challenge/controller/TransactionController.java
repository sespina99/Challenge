package com.mendel.challenge.controller;


import com.mendel.challenge.dto.TransactionRequestDTO;
import com.mendel.challenge.dto.TransactionResponseDTO;
import com.mendel.challenge.interfaces.services.TransactionService;
import com.mendel.challenge.model.Transaction;
import com.mendel.challenge.service.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    //POST /transactions -> creo nueva transaction
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequestDTO transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction.getAmount(),transaction.getType(), transaction.getParentId());
        if (createdTransaction != null) {
            TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                    createdTransaction.getId(),
                    createdTransaction.getAmount(),
                    createdTransaction.getType(),
                    createdTransaction.getParentId()
            );
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.badRequest()
                .body("bad request");
    }

    @PutMapping("/{transaction_id}")
    public ResponseEntity<String> updateTransaction(@PathVariable long transaction_id, @RequestBody Transaction transaction) {
        transactionService.updateTransaction(transaction_id,transaction.getAmount(), transaction.getType(), transaction.getParentId());
        return ResponseEntity.ok("{\"status\": \"ok\"}");
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable String type) {
        List<Long> transactionIds = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactionIds);
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransactionSum(@PathVariable long transaction_id) {
        double sum = transactionService.getTotalSumForTransaction(transaction_id);
        return ResponseEntity.ok(Map.of("sum", sum));
    }



}
