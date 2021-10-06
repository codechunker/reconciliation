package com.tutuka.reconciliation.dto;

import com.tutuka.reconciliation.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationResponse {
    Records tutukaRecords;
    Records clientRecords;
    Set<Transaction> suggested;
    String filePath;
}
