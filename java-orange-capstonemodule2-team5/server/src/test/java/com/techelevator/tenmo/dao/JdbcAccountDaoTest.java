package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.00).setScale(2));
    private static final Account ACCOUNT_2 = new Account(2004, 1004, BigDecimal.valueOf(0.00).setScale(2));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalanceByUsername_returns_expected_balance() {
        //arrange
        BigDecimal expectedTom = BigDecimal.valueOf(1000.00).setScale(2);
        BigDecimal expectedBroke = BigDecimal.valueOf(0.00).setScale(2);

        //act
        BigDecimal actualTom = sut.getBalanceByUsername("tom");
        BigDecimal actualBroke = sut.getBalanceByUsername("broke");

        //assert
        Assert.assertNotNull(actualTom);
        Assert.assertNotNull(actualBroke);
        Assert.assertEquals(expectedTom,actualTom);
        Assert.assertEquals(expectedBroke, actualBroke);
    }



    @Test
    public void getBalanceByUsername_returns_null_when_username_not_found() {
        BigDecimal balance = sut.getBalanceByUsername("fake");
        Assert.assertNull(balance);
    }

    @Test
    public void getAccountByUsername_returns_expected_id() {
        //arrange
        int expectedID1 = 2001;
        int expectedID2 = 2004;

        //act
        int actualID1 = sut.getAccountIDByUsername("tom");
        int actualID2 = sut.getAccountIDByUsername("broke");

        //assert
        Assert.assertEquals(expectedID1, actualID1);
        Assert.assertEquals(expectedID2, actualID2);
    }

    /* EmptyResultDataAccessException:Incorrect result size: expected 1, actual 0
     * */
    @Test
    public void getAccountIDByUsername_returns_zero_when_name_not_found(){
        int expected= 0;
        int accountID = sut.getAccountIDByUsername("fakename");
        Assert.assertEquals(expected, accountID);
    }

    @Test
    public void getAccount_returns_expected_id_and_values() {
        //Act
        Account actual1 = sut.getAccount("tom");
        Account actual2 = sut.getAccount("broke");

        //Assert
        Assert.assertNotNull("Get transaction returned null", actual1);
        Assert.assertNotNull("Get transaction returned null", actual2);
        assertAccountsMatch(ACCOUNT_1, actual1);
        assertAccountsMatch(ACCOUNT_2, actual2);
    }

    @Test
    public void getAccount_returns_null_when_name_not_found() {
        Account account = sut.getAccount("fake");
        Assert.assertNull(account);

    }

    @Test
    public void getUsernameByAccountID_returns_expected_username() {
        //arrange
        String expected = "tom";
        //act
        String actual = sut.getUsernameByAccountID(2001);
        //assert
        Assert.assertEquals(expected, actual);

    }

    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountID(), actual.getAccountID());
        Assert.assertEquals(expected.getUserID(), actual.getUserID());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

}
