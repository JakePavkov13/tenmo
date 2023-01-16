package com.techelevator.tenmo.checker;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
@Component
public class TransactionCheck {

    @Autowired
    AccountDao accountDao;


    public TransactionCheck(AccountDao accountDao) {

        this.accountDao = accountDao;

    }

    public TransactionCheck() {
    }

    public boolean senderIsNotReceiver(String sender, String receiver){

        return !sender.equals(receiver);
    }

    public boolean isProperNumber(BigDecimal amount){

        return amount.compareTo(BigDecimal.valueOf(0.009)) == 1 ;
    }

    public boolean hasSufficientFunds(BigDecimal balance, BigDecimal amountSent){

        return balance.compareTo(amountSent) >= 0 ;
    }

    public boolean transactionIsValid(TransactionDTO dto) {
        String sender = dto.getSender();
        String receiver = dto.getReceiver();
        BigDecimal amountSent = dto.getAmount();
        BigDecimal senderBalance = accountDao.getBalanceByUsername(sender);

        if (accountDao.getAccount(receiver).equals(null)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enter a valid receiver");
        }
        if (amountSent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enter an amount");
        }
        if (!senderIsNotReceiver(sender, receiver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't send to yourself dummy");
        }
        if (!isProperNumber(amountSent)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Amount must be at least 0.01");
        }
        if (!hasSufficientFunds(senderBalance, amountSent)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stop being so poor");
        }
        return true;
    }





}
