package com.tutuka.reconciliation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
    private String ProfileName;
    private String TransactionDate;
    private String TransactionAmount;
    private String TransactionNarrative;
    private String TransactionDescription;
    private String TransactionID;
    private String TransactionType;
    private String WalletReference;

    public static String[] getHeaderColumns() {
        return new String[] {"ProfileName", "TransactionDate", "TransactionAmount",
                "TransactionNarrative", "TransactionDescription", "TransactionID",
                "TransactionType", "WalletReference"};
    }
}
