package com.mendel.challenge.dto;

import com.mendel.challenge.model.Transaction;

public class TransactionRequestDTO {

    private double amount;

    private String type;

    private Long parentId;


    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(double amount, String type, Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public TransactionRequestDTO(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
