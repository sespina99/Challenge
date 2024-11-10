package com.mendel.challenge.dto;

public class TransactionRequestDTO {

    private double amount;

    private String type;

    private Long parentId; // Opcional, puede ser null


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



    // Getters y setters
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
