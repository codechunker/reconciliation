package com.tutuka.reconciliation.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Transaction {
    private String ProfileName;
    private String TransactionDate;
    private String TransactionAmount;
    private String TransactionNarrative;
    private String TransactionDescription;
    private BigInteger TransactionID;
    private String TransactionType;
    private String WalletReference;

    public static String[] getHeaderColumns() {
        return new String[] {"ProfileName", "TransactionDate", "TransactionAmount",
                "TransactionNarrative", "TransactionDescription", "TransactionID",
                "TransactionType", "WalletReference"};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(this.ProfileName, that.ProfileName) &&
                Objects.equals(this.TransactionDate, that.TransactionDate) &&
                Objects.equals(this.TransactionAmount, that.TransactionAmount) &&
                Objects.equals(this.TransactionNarrative, that.TransactionNarrative) &&
                Objects.equals(this.TransactionDescription, that.TransactionDescription) &&
                Objects.equals(this.TransactionID, that.TransactionID) &&
                Objects.equals(this.TransactionType, that.TransactionType) &&
                Objects.equals(this.WalletReference, that.WalletReference);
    }


    @Override
    public int hashCode() {
        return Objects.hash(ProfileName, TransactionDate, TransactionAmount,
                TransactionNarrative, TransactionDescription, TransactionID,
                TransactionType, WalletReference);
    }
}
