package com.mendel.challenge.persistence;


import com.mendel.challenge.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions = new HashMap<>();
    private final Map<String, Set<Long>> typeIndex =  new HashMap<>();
    private final Map<Long, Double> transactionSums = new HashMap<>();

    private long nextId = 0;

    public Transaction createTransaction(double amount, String type, Long parentId) {
        if (parentId != null && !transactions.containsKey(parentId)) {
            return null;
        }
        Transaction toReturn =  new Transaction(nextId, amount, type, parentId);
        nextId++;

        transactions.put(toReturn.getId(), toReturn);
        //add to transaction sums
        addTransactionSums(toReturn.getId(), toReturn.getAmount());

        typeIndex.computeIfAbsent(type, k -> new HashSet<>()).add(toReturn.getId());
        return toReturn;
    }

    public Transaction updateTransaction(Long id, double amount, String type, Long parentId) {
        if (!transactions.containsKey(id)) {
            return null;
        }
        if (parentId != null && !transactions.containsKey(parentId)) {
            return null;
        }

        if (!type.equals(transactions.get(id).getType())) {
            //remove from old type set
            typeIndex.get(transactions.get(id).getType()).remove(id);
            //add to new type set
            typeIndex.computeIfAbsent(type, k -> new HashSet<>()).add(id);
        }

        //remove the old amount map before changing parentId
        removeTransactionSums(id, transactions.get(id).getAmount());


        //update the transaction
        transactions.get(id).setAmount(amount);
        transactions.get(id).setType(type);
        transactions.get(id).setParentId(parentId);

        //add to the new sums map after changing parentId
        addTransactionSums(id, amount);

        return transactions.get(id);
    }

    public List<Long> getTransactionsByType(String type) {
        return Optional.ofNullable(typeIndex.get(type))
                .map(list -> list.stream().toList())
                .orElse(null);
    }

    public double getTotalSumForTransaction(Long transactionId) {
        return transactionSums.get(transactionId);
    }

    private void addTransactionSums(Long transactionId, double amount) {
        transactionSums.compute(transactionId, (id, sum) -> (sum == null) ? amount : sum + amount);
        Optional<Long> parentId = Optional.ofNullable(transactions.get(transactionId).getParentId());
        parentId.ifPresent(aLong -> addTransactionSums(aLong, amount));
    }

    private void removeTransactionSums(Long transactionId, double amount) {
        transactionSums.compute(transactionId, (id, sum) -> sum - amount);
        Optional<Long> parentId = Optional.ofNullable(transactions.get(transactionId).getParentId());
        parentId.ifPresent(aLong -> removeTransactionSums(aLong, amount));
    }
}
