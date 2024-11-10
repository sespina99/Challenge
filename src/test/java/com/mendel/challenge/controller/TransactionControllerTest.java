package com.mendel.challenge.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendel.challenge.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTransactionSuccess() throws Exception {

        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000, "shopping");

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(5000))
                .andExpect(jsonPath("$.type").value("shopping"))
                .andExpect(jsonPath("$.parentId").isEmpty());


    }
    @Test
    void testCreateTransactionError() throws Exception {

        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000, "shopping", 10L);

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTransaction() throws Exception {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000, "shopping");
        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));
        TransactionRequestDTO putTransactionRequest = new TransactionRequestDTO(3000, "cars");
        String jsonPutRequest = objectMapper.writeValueAsString(putTransactionRequest);

        mockMvc.perform(put("/transactions/0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPutRequest))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Expecting JSON response
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.amount").value(3000))
                .andExpect(jsonPath("$.type").value("cars"));

        //id doesnt exist
        mockMvc.perform(put("/transactions/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPutRequest))
                .andExpect(status().isBadRequest());

        //parent id doesnt exist

        TransactionRequestDTO putTransactionRequestNoParent = new TransactionRequestDTO(3000, "cars", 10L);
        String jsonPutRequestNoParent = objectMapper.writeValueAsString(putTransactionRequestNoParent);

        mockMvc.perform(put("/transactions/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPutRequestNoParent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTransactionsByType() throws Exception {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000, "flying");

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        mockMvc.perform(get("/transactions/types/flying"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[2]").exists());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        mockMvc.perform(get("/transactions/types/flying"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[3]").exists());

        mockMvc.perform(get("/transactions/types/dining"))
                .andExpect(status().isBadRequest());
    }
}