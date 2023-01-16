package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.ClientViewTransaction;
import com.techelevator.tenmo.model.Transaction;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;


    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Integer> listTransactionsByUser(int id) {
        List<Integer> allTransactions = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM transaction WHERE sender_id = ? OR receiver_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            Transaction transactions = mapRowToTransaction(results);
            allTransactions.add(transactions.getId());
        }
        return allTransactions;
    }


    @Override
    public Transaction getTransactionByID(int transaction_id) {
       Transaction transaction = null;

        String sql = "SELECT transaction_id, sender_id, receiver_id, " +
                     "amount, status FROM transaction WHERE transaction_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transaction_id);

        if (results.next()) {
            transaction = mapRowToTransaction(results);
        }

        return transaction;

    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTION (sender_id, receiver_id, amount) " +
                "VALUES (?, ?, ?) RETURNING transaction_id;";

        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, transaction.getSenderId(), transaction.getReceiverId(), transaction.getAmount());

        return getTransactionByID(newId);
    }

    @Override
    public BigDecimal addMoney(int receiverID, BigDecimal amount) {
        String balanceSQL = "SELECT balance from account where account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(balanceSQL, BigDecimal.class, receiverID);
        BigDecimal newBalance = balance.add(amount);
        String updateSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(updateSql, newBalance, receiverID);
        String getSql = "SELECT balance FROM account WHERE account_id = ?;";
        newBalance = jdbcTemplate.queryForObject(getSql, BigDecimal.class, receiverID);

        return newBalance;

    }

    @Override
    public BigDecimal subtractMoney(int senderID, BigDecimal amount) {

        String balanceSQL = "SELECT balance from account where account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(balanceSQL, BigDecimal.class, senderID);
        BigDecimal newBalance = balance.subtract(amount);
        String updateSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(updateSql, newBalance, senderID);
        String getSql = "SELECT balance FROM account WHERE account_id = ?;";
        newBalance = jdbcTemplate.queryForObject(getSql, BigDecimal.class, senderID);

        return newBalance;

    }

    private Transaction mapRowToTransaction(SqlRowSet rs) {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("transaction_id"));
        transaction.setSenderId(rs.getInt("sender_id"));
        transaction.setReceiverId(rs.getInt("receiver_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setStatus(rs.getString("status"));
        return transaction;
    }

}
