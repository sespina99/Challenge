package com.mendel.challenge.model;

public class Transaction {
    private Long id;
    private double amount;
    private String type;
    private Long parentId;

    public Transaction(Long transactionId, double amount, String type, Long parentId) {
        this.id = transactionId;
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public long getId() {
        return id;
    }

    public void setId(Long transactionId) {
        this.id = transactionId;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
