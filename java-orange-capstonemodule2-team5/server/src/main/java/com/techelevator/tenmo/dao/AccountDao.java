package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDao {

    BigDecimal getBalanceByUsername(String username);

    int getAccountIDByUsername(String username);

    Account getAccount(String username);

    String getUsernameByAccountID(int id);

}

