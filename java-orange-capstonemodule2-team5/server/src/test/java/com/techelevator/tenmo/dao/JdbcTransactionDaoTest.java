package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.ClientViewTransaction;
import com.techelevator.tenmo.model.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransactionDaoTest extends BaseDaoTests {

    private static final Transaction TRANSACTION_1 = new Transaction(3001, 2001, 2002, BigDecimal.valueOf(0.01), "APPROVED");

    private static final Transaction TRANSACTION_2 = new Transaction(3002, 2002, 2001, BigDecimal.valueOf(0.01), "APPROVED");



    private JdbcTransactionDao sut;


    private void checkTransaction(Transaction expected, Transaction actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSenderId(), actual.getSenderId());
        assertEquals(expected.getReceiverId(), actual.getReceiverId());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getStatus(), actual.getStatus());

    }

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransactionDao(jdbcTemplate);


    }

    @Test
    public void listTransactions_returns_expected_size() {
        //arrange
        int expected = 2;
        //act
        List<Integer> actual = sut.listTransactionsByUser(2001);
        //assert
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void listTransactions_byUserWithNone_returns_zero() {
        //arrange
        int expected = 0;
        //act
        List<Integer> actual = sut.listTransactionsByUser(2004);
        //assert
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void getTransactionByID_returns_correct() {


        //Act
        Transaction actual1 = sut.getTransactionByID(3001);
        Transaction actual2 = sut.getTransactionByID(3002);
        //Assert
        Assert.assertNotNull("Get transaction returned null", actual1);
        Assert.assertNotNull("Get transaction returned null", actual2);
        checkTransaction(TRANSACTION_1, actual1);
        checkTransaction(TRANSACTION_2, actual2);
    }



    @Test
    public void getTransactionByID_returns_null_if_invalid() {
        //Act
        Transaction transaction = sut.getTransactionByID(3099);
        //Assert
        Assert.assertNull(transaction);
    }

    @Test
    public void createTransaction_returns_id_and_expected_values() {
        //Arrange
        Transaction transaction5 = new Transaction(3003, 2002, 2003, BigDecimal.valueOf(0.01), "APPROVED");

        //Act
        Transaction createdTrans = sut.createTransaction(transaction5);
        Integer newId = transaction5.getId();
        //set transaction_id so it doesn't fail
        transaction5.setId(newId);

        //Assert
        checkTransaction(transaction5,createdTrans);
    }

    @Test
    public void createTransaction_returns_expected_values_when_retrieved() {
        //Arrange
        Transaction transaction5 = new Transaction(3003, 2002, 2003, BigDecimal.valueOf(0.01), "APPROVED");

        //Act
        Transaction createdTrans = sut.createTransaction(transaction5);
        Integer newId = transaction5.getId();
        //set transaction_id so it doesn't fail
        Transaction returnedTrans = sut.getTransactionByID(newId);

        //Assert
        checkTransaction(createdTrans,returnedTrans);
    }

    @Test
    public void addMoney_adds_correct_amount() {
        BigDecimal amount = new BigDecimal("0.01");
        BigDecimal compareTo = new BigDecimal("1000.01");

        BigDecimal actual = sut.addMoney(2001, amount);

        Assert.assertEquals(compareTo, actual);
    }

    @Test
    public void subtractMoney_subtracts_correct_amount() {
        BigDecimal amount = new BigDecimal("0.01");
        BigDecimal compareTo =  new BigDecimal("999.99");

        BigDecimal actual = sut.subtractMoney(2001, amount);
         Assert.assertEquals(compareTo, actual);
    }



}
