package com.mendel.challenge.service;

import static org.junit.jupiter.api.Assertions.*;

import com.mendel.challenge.interfaces.services.TransactionService;
import com.mendel.challenge.model.Transaction;
import com.mendel.challenge.persistence.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceTest {

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        TransactionRepository transactionRepository = new TransactionRepository();
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void createTransactions() {
        //test
        Transaction transaction = transactionService.createTransaction(5000, "cars", null);
        Transaction transaction1 = transactionService.createTransaction(3000, "bus", 0L);

        //assert
        assertNotNull(transaction);
        assertEquals(5000, transaction.getAmount());
        assertEquals("cars", transaction.getType());
        assertNull(transaction.getParentId());
        assertNotNull(transaction1);
        assertEquals(3000, transaction1.getAmount());
        assertEquals("bus", transaction1.getType());
        assertEquals(0, transaction1.getParentId());
    }

    @Test
    public void createTransactionSameParent() {
        //test
        Transaction transaction = transactionService.createTransaction(5000, "cars", 0L);
        assertNull(transaction);
    }

    @Test
    public void updateTransactions() {
        //setup
        Transaction transaction = transactionService.createTransaction(5000, "cars", null); //0L
        transactionService.createTransaction(3000, "bus", null); //1L

        //test
        transactionService.updateTransaction(0L,3000, "bike", 1L);

        //assert
        assertEquals(1L, transaction.getParentId());
        assertEquals(3000, transaction.getAmount());
        assertEquals("bike", transaction.getType());
    }


    @Test
    public void updateTransactionSameParent() {
        //setup
        Transaction transaction = transactionService.createTransaction(5000, "cars", null); //0L
        transactionService.createTransaction(3000, "bus", null); //1L

        //test
        transaction = transactionService.updateTransaction(0L,3000, "bike", 0L);

        //assert
        assertNull(transaction);
    }




    @Test
    public void testGetTransactionsByTypeOnCreate() {
        //setup
        transactionService.createTransaction(5000, "cars", null);
        transactionService.createTransaction(10000, "shopping", null);


        //test
        List<Long> carsTransactions = transactionService.getTransactionsByType("cars");

        //assert
        assertEquals(1, carsTransactions.size());
        assertTrue(carsTransactions.contains(0L));
    }

    @Test
    public void testGetTransactionsByTypeOnChange() {
        //setup
        transactionService.createTransaction(5000, "cars", null);
        transactionService.createTransaction(10000, "shopping", null);
        transactionService.createTransaction(10000, "shopping", null);
        transactionService.createTransaction(10000, "shopping", null);
        transactionService.createTransaction(10000, "shopping", null);

        //test
        List<Long> shoppingTransactions = transactionService.getTransactionsByType("shopping");

        //assert
        assertEquals(4, shoppingTransactions.size());
        assertTrue(shoppingTransactions.contains(3L));
    }


    @Test
    public void testGetTotalSumForTransactionOnCreate() {

        //setup
        transactionService.createTransaction(5000, "shopping", null); // ID0
        transactionService.createTransaction(10000, "shopping", 0L);
        transactionService.createTransaction(15000, "shopping", 1L);

        //test
        double sum = transactionService.getTotalSumForTransaction(0L);
        double sum1 = transactionService.getTotalSumForTransaction(1L);


        //assert
        assertEquals(30000, sum);
        assertEquals(25000, sum1); // 5000 (t1) + 10000 (t2) + 15000 (t3)

    }

    @Test
    public void testGetTotalSumForTransactionOnChange() {

        //setup
        transactionService.createTransaction(5000, "shopping", null); // ID0
        transactionService.createTransaction(10000, "shopping", 0L);
        transactionService.createTransaction(15000, "shopping", 1L);
        transactionService.getTotalSumForTransaction(0L);
        transactionService.getTotalSumForTransaction(1L);
        transactionService.updateTransaction(2L,15000, "shopping", 0L);

        //test
        double sum2 = transactionService.getTotalSumForTransaction(0L);
        double sum3 = transactionService.getTotalSumForTransaction(1L);

        //assert
        assertEquals(30000, sum2);
        assertEquals(10000, sum3); // 5000 (t1) + 10000 (t2) + 15000 (t3)
    }
}
