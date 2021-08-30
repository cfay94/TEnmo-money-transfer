package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.AccountInfo;

import java.math.BigDecimal;

public interface AccountDAO {

    //GET BALANCE
    AccountInfo getBalance(int user);

    //UPDATE BALANCES
        //ADD
     BigDecimal addToBalance(BigDecimal addBalance, int accountId);
        //SUBTRACT
     BigDecimal subtractFromBalance(BigDecimal subtractBalance, int accountId);

}
