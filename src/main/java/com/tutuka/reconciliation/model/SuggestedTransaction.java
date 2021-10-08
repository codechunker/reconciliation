package com.tutuka.reconciliation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class SuggestedTransaction {
    private Transaction suggested;
    private int score;
}
