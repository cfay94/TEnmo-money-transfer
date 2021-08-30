package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class AccountInfo {

    private BigDecimal balance;
    private int userId;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
