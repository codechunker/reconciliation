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

    public static ReconciliationResponse mapTransactionsToReconciled(Set<Transaction> tutukaTransactions, Set<Transaction> clientTransactions,
                                                                     Set<Transaction> tutukaReconciled, Set<Transaction> clientReconciled,
                                                                     Set<Transaction> suggested, String filePath) {
        Records tutukaRecord = new Records(tutukaTransactions.size(), tutukaTransactions.size() - tutukaReconciled.size(), tutukaReconciled.size(), tutukaReconciled);
        Records clientRecord = new Records(clientTransactions.size(), clientTransactions.size() - clientReconciled.size(), clientReconciled.size(), clientReconciled);

        return new ReconciliationResponse(tutukaRecord, clientRecord, suggested, filePath);
    }
}
