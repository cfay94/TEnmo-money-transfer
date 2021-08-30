package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    void userTransfers(Transfers transfer);

    //TRANSFER HISTORY
    List<Transfers> seeTransferHistory();

    //DETAILED TRANSFER BY ID
    Transfers getDetailsByTransferId(long transferId);

}
