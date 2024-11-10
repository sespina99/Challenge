package com.mendel.challenge.persistence;


import com.mendel.challenge.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions = new HashMap<>();
    private final Map<String, Set<Long>> typeIndex =  new HashMap<>();
    private long nextId = 0;

    public Transaction createTransaction(double amount, String type, Long parentId) {
        if (parentId != null && !transactions.containsKey(parentId)) {
            return null;
        }
        Transaction toReturn =  new Transaction(nextId, amount, type, parentId);
        nextId++;
        typeIndex.computeIfAbsent(type, k -> new HashSet<>()).add(toReturn.getId());
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
        //remove from old type set
        typeIndex.get(transactions.get(id).getType()).remove(id);

        //update the transaction
        transactions.get(id).setAmount(amount);
        transactions.get(id).setType(type);
        transactions.get(id).setParentId(parentId);

        //add to new type set
        typeIndex.computeIfAbsent(type, k -> new HashSet<>()).add(id);

        return transactions.get(id);
    }

    public List<Long> getTransactionsByType(String type) {
        return typeIndex.get(type).stream().toList();
    }

}
