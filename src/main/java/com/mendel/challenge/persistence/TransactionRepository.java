package com.mendel.challenge.persistence;


import com.mendel.challenge.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions= new HashMap<>();
    private long nextId = 0;

    public Transaction createTransaction(double amount, String type, Long parentId) {
        if (parentId != null && !transactions.containsKey(parentId)) {
            return null;
        }
        Transaction toReturn =  new Transaction(nextId, amount, type, parentId);
        nextId++;
        transactions.put(toReturn.getId(), toReturn);
        return toReturn;
    }

    public Transaction updateTransaction(Long id, double amount, String type, Long parentId) {
        if (!transactions.containsKey(id)) {
            return null;
        }
        if (parentId != null && !transactions.containsKey(parentId)) {
            return null;
        }
        transactions.get(id).setAmount(amount);
        transactions.get(id).setType(type);
        transactions.get(id).setParentId(parentId);
        return transactions.get(id);
    }

}
