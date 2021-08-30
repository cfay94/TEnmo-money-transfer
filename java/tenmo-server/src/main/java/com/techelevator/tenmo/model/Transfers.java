package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountTo;
    private int accountFrom;
    private BigDecimal transferAmount;

    //CONSTRUCTOR
    public Transfers(int transferId, int transferTypeId, int transferStatusId, int accountTo, int accountFrom, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.transferAmount = transferAmount;
    }

    public Transfers(int accountTo, int accountFrom, BigDecimal amount) {
        this.transferTypeId = 1;
        this.transferStatusId = 1;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.transferAmount = amount;
    }

    public Transfers(){}

    //GETTERS AND SETTERS
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "Transfers{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountTo=" + accountTo +
                ", accountFrom=" + accountFrom +
                ", transferAmount=" + transferAmount +
                '}';
    }
}
