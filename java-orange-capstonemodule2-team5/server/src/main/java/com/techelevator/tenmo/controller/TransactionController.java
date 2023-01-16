package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.checker.TransactionCheck;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

/*
 * TODO: Issues found:
 *  1. on transfer, if receiver not in db server returns 500 error (server) - should return client error 4XX
 *  2. on transfer, if sender not in db server returns 500 error (server) - should return client error 4XX
 */
@RequestMapping("/transactions")
@PreAuthorize("isAuthenticated()")
@RestController
public class TransactionController {

    private TransactionDao dao;
    private AccountDao accountDao;
    private TransactionCheck checker;

    public TransactionController(TransactionDao dao, AccountDao accountDao, TransactionCheck checker) {
        this.dao = dao;
        this.accountDao = accountDao;
        this.checker = checker;
    }

    //GET list of all user transactions (single user)
    @RequestMapping(method = RequestMethod.GET)
    public List<Integer> getAllTrans(Principal currentUser) {
        String username = currentUser.getName();
        int account;
        account = accountDao.getAccountIDByUsername(username);
        return dao.listTransactionsByUser(account);
    }


    //method to provide transaction details while keeping User IDs secure.
    @RequestMapping(path = "/{transactionid}", method = RequestMethod.GET)
    public ClientViewTransaction getTransactionByID(@Valid @PathVariable("transactionid") int id, Principal currentUser) {
        String username = currentUser.getName();
        Transaction trans;
        trans = dao.getTransactionByID(id);

        if (trans == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid transaction information entered. Schmuck.");
        }

        /*
         * TODO: Client view code below should be placed in a method that's called here
         *  it would make the code more readable and maintainable
         */
        ClientViewTransaction clientView;
        clientView = new ClientViewTransaction(trans.getId(), accountDao.getUsernameByAccountID(trans.getSenderId()), accountDao.getUsernameByAccountID(trans.getReceiverId()), trans.getAmount(), trans.getStatus());

        if (!username.equals(clientView.getSender())) {
            if (!username.equals(clientView.getReceiver())) {
                throw new SecurityException("You are not associated with this transaction. Nice try Mr. Robot.");
            }
        }
        return clientView;

    }


    //POST new transaction and add/subtract from accounts
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public ClientViewTransaction createTransaction(@RequestBody TransactionDTO tranDto, Principal currentUser) {
        /*
         * TODO: From a design perspective, the code in this method should be,
         *  at the very least, broken up into smaller methods. Doing so would
         *  greatly improve the readability and maintainability. Currently,
         *  the code here has too many responsibilities. Speak with me if you
         *  want some examples/demo.
         */

        String sender = currentUser.getName(); //Refactor??? is Sender null
        if (!tranDto.getSender().equals(sender)) {
            throw new SecurityException("Not Authorized to send money on another's Behalf. You Dope.");
        }
        //gets senderID from sendername, and receiverID from receivername
        int senderID = accountDao.getAccountIDByUsername(sender);
        int receiverID = accountDao.getAccountIDByUsername(tranDto.getReceiver());

        //Has to pass all transaction checks
        checker.transactionIsValid(tranDto);

        //subtract from sender account
        dao.subtractMoney(senderID, tranDto.getAmount());

        //add money to receiver account
        dao.addMoney(receiverID, tranDto.getAmount());

      //createTransaction(transaction)
        Transaction transaction = new Transaction(senderID, receiverID, tranDto.getAmount());
        transaction = dao.createTransaction(transaction);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not made");
        }
        //returns ClientViewTransaction to view details + "APPROVED" status
        return getTransactionByID(transaction.getId(), currentUser);
    }


}
