package com.tutuka.reconciliation.utils;

import com.tutuka.reconciliation.dto.ReconciliationResponse;
import com.tutuka.reconciliation.dto.Records;
import com.tutuka.reconciliation.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Mapper {

    private Mapper() {
        //DO NOT INITIALIZE
    }

    public static ReconciliationResponse mapTransactionsToReconciled(int tutukaTranCount, int clientTranCount,
                                                                     List<Transaction> tutukaUnmatched,
                                                                     List<Transaction> clientUnmatched) {
        Records tutukaRecord = new Records(tutukaTranCount, tutukaTranCount - tutukaUnmatched.size(), tutukaUnmatched.size(), tutukaUnmatched);
        Records clientRecord = new Records(clientTranCount, clientTranCount - clientUnmatched.size(), clientUnmatched.size(), clientUnmatched);

        return new ReconciliationResponse(tutukaRecord, clientRecord);
    }
}
