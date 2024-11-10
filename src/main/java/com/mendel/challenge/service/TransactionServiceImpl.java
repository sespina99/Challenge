package com.mendel.challenge.service;

import com.mendel.challenge.interfaces.services.TransactionService;
import com.mendel.challenge.model.Transaction;
import com.mendel.challenge.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {



    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(double amount, String type, Long parentId) {
        return transactionRepository.createTransaction(amount, type, parentId);
    }

    @Override
    public Transaction updateTransaction(Long id, double amount, String type, Long parentId) {
        return transactionRepository.updateTransaction(id, amount, type, parentId);
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