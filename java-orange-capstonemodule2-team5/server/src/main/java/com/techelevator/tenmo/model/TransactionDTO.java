package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class TransactionDTO {

    //sender is Principal currentUser
    @Value(" ")
    private String sender;
    @NotBlank(message = "please enter a receiver")
    private String receiver;
    @NotBlank(message = "please enter an amount above 0")
    private BigDecimal amount;


    public TransactionDTO(String sender, String receiver, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public TransactionDTO(){
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

}
