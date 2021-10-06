package com.tutuka.reconciliation.dto;

import com.tutuka.reconciliation.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Records {
    private int totalRecords;
    private int matchingRecords;
    private int unmatchedRecords;
    private Set<Transaction> unmatchedTransactions;
}
