package com.mendel.challenge.interfaces.services;

import com.mendel.challenge.model.Transaction;


import java.util.List;

public interface TransactionService {

    Transaction updateTransaction(Long id ,double amount, String type, Long parentId);

    Transaction createTransaction(double amount, String type, Long parentId);

    List<Long> getTransactionsByType(String type);

    Double getTotalSumForTransaction(Long transactionId);

}
