package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;


@RestController
@RequestMapping("/users/balance")
@PreAuthorize("isAuthenticated()")
    public class AccountController{

    private AccountDao dao;

    public AccountController(AccountDao dao){
        this.dao = dao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getBalance(Principal current) {
        String username = current.getName();
        return "Balance: " + dao.getBalanceByUsername(username) + " TE Bucks";
    }

}


