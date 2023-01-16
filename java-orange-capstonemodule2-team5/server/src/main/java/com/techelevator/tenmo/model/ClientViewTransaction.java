package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class ClientViewTransaction {

    private int id;

    private String sender;
    private String receiver;

    private BigDecimal amount;
    private String status = "APPROVED";

    public ClientViewTransaction(int id, String sender, String receiver, BigDecimal amount, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.status = status;
    }

    public ClientViewTransaction(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
