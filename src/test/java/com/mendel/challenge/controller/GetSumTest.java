package com.mendel.challenge.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendel.challenge.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSumTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DirtiesContext
    @Test
    void getSumTest() throws Exception {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(5000, "shopping");

        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(get("/transactions/sum/0"))
                .andExpect(status().isNotFound());


        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        mockMvc.perform(get("/transactions/sum/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(5000));

        TransactionRequestDTO transactionRequestChild = new TransactionRequestDTO(3000, "cars", 0L);
        String jsonRequestChild = objectMapper.writeValueAsString(transactionRequestChild);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestChild));

        mockMvc.perform(get("/transactions/sum/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(8000));

        TransactionRequestDTO transactionRequestThird = new TransactionRequestDTO(3000, "cars");
        String jsonRequestThird = objectMapper.writeValueAsString(transactionRequestThird);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestThird));


        TransactionRequestDTO transactionPutRequestChild = new TransactionRequestDTO(9000, "cars", 2L);
        String jsonPutRequestChild = objectMapper.writeValueAsString(transactionPutRequestChild);

        mockMvc.perform(put("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPutRequestChild));

        mockMvc.perform(get("/transactions/sum/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(5000));

        mockMvc.perform(get("/transactions/sum/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sum").value(12000));
    }
}
