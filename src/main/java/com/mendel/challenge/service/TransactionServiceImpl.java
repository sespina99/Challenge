package com.mendel.challenge.service;

import com.mendel.challenge.interfaces.services.TransactionService;
import com.mendel.challenge.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Transaction modifyOrCreateTransaction(Long id, double amount, String type, Long parentId) {
        return null;
    }

    @Override
    public Transaction createTransaction(double amount, String type, Long parentId) {
        return null;
    }

    @Override
    public List<Long> getTransactionsByType(String type) {
        return List.of();
    }

    @Override
    public double getTotalSumForTransaction(Long transactionId) {
        return 0;
    }
}
