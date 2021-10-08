package com.tutuka.reconciliation.utils;

import com.tutuka.reconciliation.model.SuggestedTransaction;
import com.tutuka.reconciliation.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AppUtils {
    private static final String DELIMITER = ",";
    private static HashMap<Integer, Transaction> tranMap = new HashMap<>();
    private static final HashMap<Integer, Transaction> duplicateMap = new HashMap<>();
    private AppUtils() {
        //DO NOT INITIALIZE
    }

    /**
     * extract transactions in file and store in memory for comparison with another file
     * @param file
     */
    public static HashMap<Integer, Transaction> extractTransactionsFromFile(File file) {
        tranMap.clear();
        duplicateMap.clear();
        try {
            InputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            tranMap = bufferedReader.lines().skip(1)//skip header
                    .map(mapToTransaction)
                    .collect(HashMap::new, (map, transaction) -> map.put(transaction.hashCode(), transaction), Map::putAll);
            bufferedReader.close();
        }catch (IOException e) {
            throw new IllegalArgumentException("Error extracting transactions");
        }

        return tranMap;
    }

    /**
     * filter transaction row in csv not found with one already uploaded in memory
     * @param file
     */
    public static List<Transaction> filterTransactions(File file) {
        List<Transaction> unmatchedTransactions;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            unmatchedTransactions = bufferedReader.lines().skip(1)//skip header
                    .map(mapToTransaction).filter(isUnmatched()).distinct().collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error extracting Transactions from file");
        }
        return unmatchedTransactions;
    }

    /**
     * filter files that cannot be matched with those already uploaded.
     * for transaction in file and memory, if both have the same hashcode,
     * use equals for extra confirmation
     */
    private static Predicate<Transaction> isUnmatched() {
        return tranFromFile -> {
            int hashCode = tranFromFile.hashCode();
            if (tranMap.containsKey(hashCode) ) {
                Transaction tranInMemory = tranMap.get(hashCode);
                //There is a non-duplicate match, so do not filter
                return !tranFromFile.equals(tranInMemory) || duplicateMap.containsKey(hashCode);
            } else {
                return true;
            }
        };
    }

    private static  final Function<String, Transaction> mapToTransaction = (row) -> {
        Transaction transaction = new Transaction();
        try {
            String[] cells = row.split(DELIMITER);
            transaction.setProfileName(cells[0]);
            transaction.setTransactionDate(cells[1]);
            transaction.setTransactionAmount(cells[2]);
            transaction.setTransactionNarrative(cells[3]);
            transaction.setTransactionDescription(cells[4]);
            transaction.setTransactionID(new BigInteger((cells[5])));
            transaction.setTransactionType(cells[6]);
            transaction.setWalletReference(cells[7]);
            return transaction;
        } catch (Exception e) {
//            log.error("", e);
            return transaction;
        }
    };

    public static void suggestTransactions(List<Transaction> tran1, List<Transaction> tran2) {
        Collections.sort(tran2);
        for (Transaction transaction : tran1) {
            //Search with TransactionID
            if (transaction.getTransactionID() != null) {
                SuggestedTransaction found = search(tran2, transaction, transaction.getTransactionID());
                transaction.setSuggested(found);
            }
        }
    }

    private static SuggestedTransaction search(List<Transaction> transactions, Transaction transaction, BigInteger transactionID) {
        int left = 0, right = transactions.size() - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            Transaction foundTransaction = transactions.get(middle);
            if (Objects.equals(foundTransaction.getTransactionID(), transactionID)) {
                int score = score(transaction, foundTransaction);
                return new SuggestedTransaction(foundTransaction, score);
            }

            if (transactions.get(middle).getTransactionID().compareTo(transactionID) < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return null;
    }

    private static int score(Transaction transaction, Transaction foundTransaction) {

        int score = 0;
        if (Objects.equals(transaction.getTransactionID(), foundTransaction.getTransactionID())) {
            score += 25;

        }

        if (transaction.getWalletReference().equals(foundTransaction.getWalletReference())) {
            score += 20;
        }

        if (transaction.getProfileName().equalsIgnoreCase(foundTransaction.getProfileName())) {
            score += 15;
        }

        if (transaction.getTransactionAmount().equals(foundTransaction.getTransactionAmount())) {
            score += 15;
        }

        if (transaction.getTransactionDate().equals(foundTransaction.getTransactionDate())) {
            score += 10;
        }

        if (transaction.getTransactionType().equals(foundTransaction.getTransactionType())) {
            score += 5;
        }

        if (transaction.getTransactionDescription().equalsIgnoreCase(foundTransaction.getTransactionDescription())) {
            score += 5;
        }

        if (transaction.getTransactionNarrative().equalsIgnoreCase(foundTransaction.getTransactionNarrative())) {
            score += 5;
        }

        return score;
    }
}
