package com.mendel.challenge.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendel.challenge.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000.0, "shopping");

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(5000.0))
                .andExpect(jsonPath("$.type").value("shopping"))
                .andExpect(jsonPath("$.parentId").isEmpty());


    }
    @Test
    void testCreateTransactionError() throws Exception {

        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000L, "shopping", 10L);

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("bad request"));
    }
}