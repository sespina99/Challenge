package com.mendel.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import com.mendel.challenge.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceTest {


    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    public void createTransactions() {
        Transaction transaction = transactionService.createTransaction(5000, "cars", null);
        assertNotNull(transaction);
        assertEquals(5000, transaction.getAmount());
        assertEquals("cars", transaction.getType());
        assertNull(transaction.getParentId());

        Transaction transaction1 = transactionService.createTransaction(5000, "cars", 0L);
        assertNotNull(transaction);
        assertEquals(5000, transaction.getAmount());
        assertEquals("cars", transaction.getType());
        assertEquals(0L, transaction.getParentId());
    }

    @Test
    public void updateTransactions() {
        Transaction transaction = transactionService.createTransaction(5000, "cars", null); //0L
        Transaction transaction1 = transactionService.createTransaction(5000, "cars", null); //1L
        assertNotNull(transaction);
        assertEquals(5000, transaction.getAmount());
        assertEquals("cars", transaction.getType());
        assertNull(transaction.getParentId());
        transactionService.modifyOrCreateTransaction(0L,5000, "cars", 1L);
        assertEquals(1L, transaction.getParentId());

        Transaction transaction3 = transactionService.modifyOrCreateTransaction(5L, 5000, "bus", 0L);
        assertNotNull(transaction3);
        assertEquals(5000, transaction3.getAmount());
        assertEquals("bus", transaction3.getType());
        assertEquals(0L, transaction3.getParentId());
    }

    @Test
    public void testGetTransactionsByType() {
        transactionService.createTransaction(5000, "cars", null);
        transactionService.createTransaction(10000, "shopping", null);

        List<Long> carsTransactions = transactionService.getTransactionsByType("cars");
        assertEquals(1, carsTransactions.size());
        assertTrue(carsTransactions.contains(0L));
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
