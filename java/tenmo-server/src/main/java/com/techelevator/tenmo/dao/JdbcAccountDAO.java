package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.AccountInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    //GET USERS AND BALANCE
    @Override
    public AccountInfo getBalance(int userId) {
        //                          ^ actually an accountId
        // transform that accountId into a userId

        AccountInfo currentBalance = new AccountInfo();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userId);

        while(result.next()){
        currentBalance = mapRowToBalance(result);
        }
        return currentBalance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal addBalance, int accountId) {
        int accountTo = getUserIdFromAccountId(accountId);
        AccountInfo currentAccount = getBalance(accountTo);
        BigDecimal newBalance = currentAccount.getBalance().add(addBalance);
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);

       return newBalance;
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal subtractBalance, int accountId) {
        int accountFrom = getUserIdFromAccountId(accountId);
        AccountInfo currentAccount = getBalance(accountFrom);
        BigDecimal newBalance = currentAccount.getBalance().subtract(subtractBalance);
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);

      return newBalance;
    }

    // HELPER METHOD
    public int getUserIdFromAccountId (int accountId) {
        String sql = "SELECT users.user_id " +
                "FROM users " +
                "JOIN accounts ON users.user_id = accounts.user_id " +
                "WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        int userId = -1; //create user id that will never be true
        if (result.next()) {
            userId = result.getInt("user_id");
        }
        return userId;
    }

    private AccountInfo mapRowToBalance(SqlRowSet rowSet){
        AccountInfo userAccount = new AccountInfo();
        userAccount.setBalance(rowSet.getBigDecimal("balance"));
        userAccount.setUserId(rowSet.getInt("user_id"));
        return userAccount;
    }
}
