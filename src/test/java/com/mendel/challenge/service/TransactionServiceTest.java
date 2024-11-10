package com.mendel.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import com.mendel.challenge.model.Transaction;
import com.mendel.challenge.persistence.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceTest {

    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        TransactionRepository transactionRepository = new TransactionRepository();
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void createTransactions() {
        Transaction transaction = transactionService.createTransaction(5000, "cars", null);
        assertNotNull(transaction);
        assertEquals(5000, transaction.getAmount());
        assertEquals("cars", transaction.getType());
        assertNull(transaction.getParentId());
        Transaction transaction1 = transactionService.createTransaction(3000, "bus", 0L);
        assertNotNull(transaction1);
        assertEquals(3000, transaction1.getAmount());
        assertEquals("bus", transaction1.getType());
        assertEquals(0, transaction1.getParentId());
    }

    @Test
    public void updateTransactions() {
        Transaction transaction = transactionService.createTransaction(5000, "cars", null); //0L
        Transaction transaction1 = transactionService.createTransaction(3000, "bus", null); //1L
        assertNotNull(transaction);
        assertNull(transaction.getParentId());
        transactionService.updateTransaction(0L,3000, "bike", 1L);
        assertEquals(1L, transaction.getParentId());
        assertEquals(3000, transaction.getAmount());
        assertEquals("bike", transaction.getType());
    }

    @Test
    public void testGetTransactionsByType() {
        transactionService.createTransaction(5000, "cars", null);
        transactionService.createTransaction(10000, "shopping", null);

        List<Long> carsTransactions = transactionService.getTransactionsByType("cars");
        assertEquals(1, carsTransactions.size());
        assertTrue(carsTransactions.contains(0L));
        transactionService.createTransaction(10000, "shopping", null);
        transactionService.createTransaction(10000, "shopping", null);
        transactionService.createTransaction(10000, "shopping", null);
        List<Long> shoppingTransactions = transactionService.getTransactionsByType("shopping");
        assertEquals(4, shoppingTransactions.size());
        assertTrue(shoppingTransactions.contains(3L));

    }

    @Test
    public void testGetTotalSumForTransaction() {
        Transaction t1 = transactionService.createTransaction(5000, "shopping", null); // ID 1
        Transaction t2 = transactionService.createTransaction(10000, "shopping", 0L);
        Transaction t3 = transactionService.createTransaction(15000, "shopping", 1L);

        double sum = transactionService.getTotalSumForTransaction(0L);
        assertEquals(30000, sum);

        double sum1 = transactionService.getTotalSumForTransaction(1L);
        assertEquals(25000, sum1); // 5000 (t1) + 10000 (t2) + 15000 (t3)
    }
}
