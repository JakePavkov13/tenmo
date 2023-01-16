package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.security.SecurityUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;


@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceByUsername(String username) {
        try{
        String sql = "Select balance FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.username = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
            return balance.setScale(2);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public int getAccountIDByUsername(String username) {
        try {
            int account;
            String sql = "Select account_id FROM account " +
                    "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                    "WHERE tenmo_user.username = ?;";
            Integer account_id = jdbcTemplate.queryForObject(sql, Integer.class, username);

            account = account_id;
            return account;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }

    }

    @Override
    public Account getAccount(String username) {
        Account account = null;
        String sql = "Select * FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    public String getUsernameByAccountID(int id) {
        String sql = "SELECT username FROM tenmo_user tu " +
                "JOIN account acc ON tu.user_id = acc.user_id " +
                "WHERE acc.account_id = ?;";
        String username = jdbcTemplate.queryForObject(sql, String.class, id);

        return username;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountID(rs.getInt("account_id"));
        account.setUserID(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
