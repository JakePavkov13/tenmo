package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.ClientViewTransaction;
import com.techelevator.tenmo.model.Transaction;


import java.math.BigDecimal;
import java.util.List;


public interface TransactionDao {

    List<Integer> listTransactionsByUser(int account_id);


    Transaction getTransactionByID(int transaction_id);

    Transaction createTransaction (Transaction transaction);

    BigDecimal addMoney(int receiverID, BigDecimal amount);

    BigDecimal subtractMoney(int senderID, BigDecimal amount);

}
